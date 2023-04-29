package com.example.demo.service.certificate;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import com.example.demo.dto.certificate.CertificateDTO;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.dto.certificate.DownloadDto;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.certificate.CertificateType;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.repository.certificate.CertificateRepository;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.role.RoleService;
import com.example.demo.service.user.UserService;
import com.example.demo.util.TokenUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static final String certDir = "src/certificates";

    @Override
    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) throws Exception {
        CertificateRequest certificateRequest = new CertificateRequest(certificateRequestDTO);
        certificateRequest.setOwner(userService.findById(certificateRequestDTO.getOwnerId()).orElseThrow());
        Role admin = roleService.findByName("ROLE_ADMIN");
        if (certificateRequest.getType() == CertificateType.ROOT &&
                certificateRequest.getOwner().getAuthorities().contains(admin)) {
            certificateRequest.setIssuer(null);
        }
        else if (certificateRequest.getType() == CertificateType.ROOT &&
                !certificateRequest.getOwner().getAuthorities().contains(admin)) {
            throw new Exception("No permission for this certificate type.");
        }
        else {
            certificateRequest.setIssuer(certificateRepository.findById(certificateRequestDTO.getIssuerId())
                    .orElseThrow());
        }

        certificateRequest.setRequestDate(LocalDate.now());

        certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestDTO(certificateRequest);
    }

    @Override
    public Certificate issueCertificate(CertificateRequest certificateRequest) throws Exception {
        return generateCertificate(certificateRequest);
    }

    @Override
    public Certificate generateCertificate(CertificateRequest certificateRequest) throws Exception {
        X509Certificate issuerCertificate;
        Certificate issuer = certificateRequest.getIssuer();
        X500Name issuerX500Name = generateX500Name(certificateRequest.getOwner());

        KeyPair keyPair = generateKeyPair();
        JcaContentSignerBuilder builder = new JcaContentSignerBuilder(certificateRequest.getSignatureAlgorithm());
        builder = builder.setProvider("BC");
        ContentSigner contentSigner = builder.build(keyPair.getPrivate());

        BigInteger serialNumber = generateSerialNumber();
        Date from = new Date();
        Date to = fromLocalDateToDate(LocalDate.now().plusWeeks(2));

        if (issuer != null) {
            if (issuer.getType() == CertificateType.END) throw new Exception("End certificates can't sign other certificates!");
            issuerCertificate = loadCertificate(issuer.getAlias());
            System.out.println(issuerCertificate.getSubjectX500Principal().getClass());
            issuerCertificate.checkValidity();
            issuerX500Name = new X500Name(issuerCertificate.getSubjectX500Principal().getName());
            contentSigner = builder.build(loadKey(issuer.getAlias()));
        }

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuerX500Name,
                serialNumber,
                from,
                to,
                generateX500Name(certificateRequest.getOwner()),
                keyPair.getPublic());
        KeyUsage keyUsage = parseFlags(certificateRequest.getFlags());
        certificateBuilder.addExtension(Extension.keyUsage,true, keyUsage);

        X509CertificateHolder certHolder = certificateBuilder.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate newCertificate = certConverter.getCertificate(certHolder);
        String alias = newCertificate.getSerialNumber() + "_" + certificateRequest.getOwner().getName() + "_" +
                certificateRequest.getOwner().getSurname();

        addCertificate(alias, newCertificate, keyPair.getPrivate());

        Certificate certificate = new Certificate(certificateRequest, alias, serialNumber, fromDateToLocalDate(from),
                fromDateToLocalDate(to));

        certificateRepository.save(certificate);
        certificateRequest.setApproved(true);
        certificateRequestRepository.save(certificateRequest);

        return certificate;
    }

    @Override
    public X509Certificate loadCertificate(String alias) throws Exception {
        try (FileInputStream certIn = new FileInputStream(certDir + "/" + alias + ".crt")) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(certIn);
        }
    }

    @Override
    public PrivateKey loadKey(String alias) throws Exception {
        try (FileInputStream keyIn = new FileInputStream(certDir + "/" + alias + ".key")) {
            byte[] keyBytes = keyIn.readAllBytes();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    @Override
    public void addCertificate(String alias, X509Certificate certificate, PrivateKey privateKey) throws Exception {
        try (FileOutputStream certOut = new FileOutputStream(certDir + "/" + alias + ".crt");
             FileOutputStream keyOut = new FileOutputStream(certDir + "/" + alias + ".key")) {
            certOut.write(certificate.getEncoded());
            keyOut.write(privateKey.getEncoded());
        }
    }

    @Override
    public BigInteger generateSerialNumber() {
        byte[] bytes = new byte[16];
        UUID uuid = UUID.randomUUID();
        ByteBuffer.wrap(bytes)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());
        return new BigInteger(bytes);
    }

    @Override
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CertificateDTO> getAllCertificates() {
         return certificateRepository.findAll().stream()
                .map(CertificateDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void checkValidity(BigInteger serialNumber) throws Exception {
        Certificate certificate = certificateRepository.findBySerialNumber(serialNumber).orElseThrow();
        X509Certificate x509Certificate = loadCertificate(certificate.getAlias());

        x509Certificate.checkValidity();
    }

    @Override
    public void checkValidityFromCopy(MultipartFile certificate) throws Exception {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) factory.generateCertificate(certificate.getInputStream());

        System.out.println(cert.getSerialNumber());

        checkValidity(cert.getSerialNumber());
    }

    @Override
    public DownloadDto getCertificateForDownload(String alias) throws IOException {

        Resource resource = new FileSystemResource(certDir + "/" + alias + ".crt");

        String contentType = Files.probeContentType(Paths.get(resource.getFile().getPath()));

        return new DownloadDto(resource, contentType);
    }


    private LocalDate fromDateToLocalDate(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    private Date fromLocalDateToDate(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atStartOfDay();
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private KeyUsage parseFlags(String flags) {
        String[] flagTokens = flags.split(",");
        int cumulativeFlag = 0;
        for (String token: flagTokens) {
            int flag = Integer.parseInt(token);
            flag = 2 ^ flag;
            cumulativeFlag |= flag;
        }
        return new KeyUsage(cumulativeFlag);
    }

    private X500Name generateX500Name(User owner) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, owner.getName() + " " + owner.getSurname());
        builder.addRDN(BCStyle.SURNAME, owner.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, owner.getName());
        return builder.build();
    }
}
