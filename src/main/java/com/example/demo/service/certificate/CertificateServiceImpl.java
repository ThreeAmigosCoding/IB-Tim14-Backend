package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateDTO;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.dto.certificate.DownloadDto;
import com.example.demo.dto.certificate.RevocationRequestDto;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.certificate.CertificateType;
import com.example.demo.model.certificate.RevocationRequest;
import com.example.demo.model.user.Role;
import com.example.demo.model.user.User;
import com.example.demo.repository.certificate.CertificateRepository;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.repository.certificate.RevocationRequestRepository;
import com.example.demo.service.role.RoleService;
import com.example.demo.service.user.UserService;
import org.apache.commons.io.FilenameUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
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
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Autowired
    private RevocationRequestRepository revocationRequestRepository;

    private static final String certDir = "src/certificates";

    @Override
    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) throws Exception {
        CertificateRequest certificateRequest = new CertificateRequest(certificateRequestDTO);
        certificateRequest.setApproved(null);
        certificateRequest.setOwner(userService.findById(certificateRequestDTO.getOwnerId()).orElseThrow());
        certificateRequest.setRequestDate(LocalDate.now());
        Role admin = roleService.findByName("ROLE_ADMIN");

        if (certificateRequest.getType().equals(CertificateType.ROOT) &&
                !certificateRequest.getOwner().getAuthorities().contains(admin))
            throw new Exception("No permission for this certificate type.");

        Certificate issuer = null;
        if (!certificateRequest.getType().equals(CertificateType.ROOT)){
            issuer = certificateRepository.findBySerialNumber(certificateRequestDTO.getIssuerSerialNumber())
                    .orElseThrow();
            checkValidity(issuer.getSerialNumber());
        }

        certificateRequest.setIssuer(issuer);

        if (certificateRequest.getOwner().getAuthorities().contains(admin) ||
                (issuer != null && certificateRequest.getOwner().equals(issuer.getOwner()))) {
            certificateRequest.setApproved(true);
            certificateRequestRepository.save(certificateRequest);
            this.generateCertificate(certificateRequest);
            return new CertificateRequestDTO(certificateRequest);
        }

        certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestDTO(certificateRequest);
    }

    @Override
    public Certificate issueCertificate(CertificateRequest certificateRequest, Integer userId) throws Exception {
        this.validateCertificateCreation(userId, certificateRequest.getId());
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

        GeneralName generalName = new GeneralName(GeneralName.dNSName, "localhost"); // replace "localhost" with your domain
        GeneralNames subjectAltName = new GeneralNames(generalName);
        certificateBuilder.addExtension(Extension.subjectAlternativeName, false, subjectAltName);

        X509CertificateHolder certHolder = certificateBuilder.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate newCertificate = certConverter.getCertificate(certHolder);
        String alias = newCertificate.getSerialNumber() + "_" + certificateRequest.getOwner().getName() + "_" +
                certificateRequest.getOwner().getSurname();

        addCertificate(alias, newCertificate, keyPair.getPrivate());

        Certificate certificate = new Certificate(certificateRequest, alias, serialNumber, fromDateToLocalDate(from),
                fromDateToLocalDate(to));

        certificate.setRevoked(false);
        certificateRepository.save(certificate);
        certificateRequest.setApproved(true);
        certificateRequestRepository.save(certificateRequest);

        return certificate;
    }

    @Override
    public X509Certificate loadCertificate(String alias) throws Exception {
        validateAlias(alias);
        Path certPath = Paths.get(certDir, alias + ".crt");
        try (FileInputStream certIn = new FileInputStream(certPath.toFile())) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(certIn);
        }
    }

    @Override
    public PrivateKey loadKey(String alias) throws Exception {
        validateAlias(alias);
        Path keyPath = Paths.get(certDir, alias + ".key");
        try (FileInputStream keyIn = new FileInputStream(keyPath.toFile())) {
            byte[] keyBytes = keyIn.readAllBytes();
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    @Override
    public void addCertificate(String alias, X509Certificate certificate, PrivateKey privateKey) throws Exception {
        validateAlias(alias);

        Path certPath = Paths.get(certDir, alias + ".crt");
        Path keyPath = Paths.get(certDir, alias + ".key");

        try (PemWriter certWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(certPath.toFile())));
             PemWriter keyWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(keyPath.toFile())))) {
            // Write the certificate
            PemObject certPemObject = new PemObject("CERTIFICATE", certificate.getEncoded());
            certWriter.writeObject(certPemObject);

            // Write the private key
            PemObject keyPemObject = new PemObject("PRIVATE KEY", privateKey.getEncoded());
            keyWriter.writeObject(keyPemObject);
        }
    }

    @Override
    public BigInteger generateSerialNumber() {
        SecureRandom random = new SecureRandom();
        byte[] serialNumberBytes = new byte[8];
        random.nextBytes(serialNumberBytes);
        return new BigInteger(1, serialNumberBytes);
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

        if (certificate.isRevoked()) throw new Exception("Issuer certificate is not valid!");

        x509Certificate.checkValidity();
    }

    @Override
    public void checkValidityFromCopy(MultipartFile certificate) throws Exception {

        String extension = FilenameUtils.getExtension(certificate.getOriginalFilename());
        if (extension != null && !extension.equals("crt")){
            throw new Exception("File type must be certificate!");
        }

        CertificateFactory factory = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) factory.generateCertificate(certificate.getInputStream());


        checkValidity(cert.getSerialNumber());
    }

    @Override
    public DownloadDto getCertificateForDownload(String alias, Integer userId) throws Exception {
        validateAlias(alias);
        Path certPath = Paths.get(certDir, alias + ".crt");

        Resource certificateResource = new FileSystemResource(certPath.toFile());
        String contentType = Files.probeContentType(Paths.get(certificateResource.getFile().getPath()));
        String fileName = alias + ".crt";

        Certificate certificate = certificateRepository.findByAlias(alias).orElseThrow(()
                -> new Exception("Certificate with this alias doesn't exist!"));
        if (certificate.getOwner().getId().equals(userId)){
            Path keyPath = Paths.get(certDir, alias + ".key");
            Resource keyResource = new FileSystemResource(keyPath.toFile());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            ZipEntry certificateZipEntry = new ZipEntry(alias + ".crt");
            zos.putNextEntry(certificateZipEntry);
            Files.copy(certificateResource.getFile().toPath(), zos);
            zos.closeEntry();

            ZipEntry keyZipEntry = new ZipEntry(alias + ".key");
            zos.putNextEntry(keyZipEntry);
            Files.copy(keyResource.getFile().toPath(), zos);
            zos.closeEntry();

            zos.close();

            certificateResource = new ByteArrayResource(baos.toByteArray());
            fileName = "certificateAndKey.zip";
            contentType = "application/zip";
        }

        return new DownloadDto(certificateResource, contentType, fileName);
    }

    @Override
    public void validateCertificateCreation(Integer userId, Integer requestId) throws Exception {
        CertificateRequest request = this.certificateRequestRepository.findById(requestId)
                .orElseThrow(() -> new Exception("Request does not exist!"));
        Role admin = roleService.findByName("ROLE_ADMIN");
        User user = userService.findById(userId)
                .orElseThrow(() -> new Exception("User does not exist!"));
        if (user.getAuthorities().contains(admin))
            return;
        if (request.getIssuer() != null && !Objects.equals(request.getIssuer().getOwner().getId(), userId))
            throw new Exception("You can not approve this request!");
    }

    @Override
    public void revokeCertificateChain(Integer revocationRequestId, Integer userId) throws Exception {
        RevocationRequest request = revocationRequestRepository.findById(revocationRequestId).orElseThrow(
                () -> new Exception("Revocation request doesn't exist!")
        );

        validateRevocation(userId, request.getIssuer());

        revokeAllInChain(request.getRevocationCertificate());
        request.setApproved(true);
        revocationRequestRepository.save(request);
    }

    @Override
    public RevocationRequestDto createRevocationRequest(RevocationRequestDto revocationRequestDto, Integer userId) throws Exception {
        System.out.println(revocationRequestDto.getRevocationCertificateSerialNumber());
        RevocationRequest revocationRequest = new RevocationRequest();
        revocationRequest.setRequestDate(LocalDate.now());
        revocationRequest.setReason(revocationRequestDto.getReason());

        Certificate revocationCertificate = certificateRepository.findBySerialNumber(revocationRequestDto
                .getRevocationCertificateSerialNumber()).orElseThrow(() -> new Exception("Revocation certificate doesn't exist!"));
        revocationRequest.setRevocationCertificate(revocationCertificate);

        validateRevocation(userId, revocationCertificate);

        Certificate issuer = revocationCertificate.getIssuer();
        revocationRequest.setIssuer(issuer);

        User user = userService.findById(userId).orElseThrow(() -> new Exception("User doesn't exist!"));

        if (user.getAuthorities().contains(roleService.findByName("ROLE_ADMIN")) ||
                (issuer != null && issuer.getOwner().equals(revocationCertificate.getOwner()))) {
            revocationRequest.setApproved(true);
            revocationRequestRepository.save(revocationRequest);
            revokeAllInChain(revocationCertificate);
            return new RevocationRequestDto(revocationRequest);
        }

        return new RevocationRequestDto(revocationRequestRepository.save(revocationRequest));
    }

    @Override
    public List<RevocationRequestDto> getRevocationRequests(Integer userId) throws Exception {
        User user = userService.findById(userId).orElseThrow(() -> new Exception("User doesn't exist!"));
        if (user.getAuthorities().contains(roleService.findByName("ROLE_ADMIN"))) {
            return revocationRequestRepository.findAll().stream()
                    .map(RevocationRequestDto::new)
                    .collect(Collectors.toList());
        }
        return revocationRequestRepository.findAllByUserId(userId).stream()
                .map(RevocationRequestDto::new)
                .collect(Collectors.toList());
    }

    private void validateRevocation(Integer userId, Certificate certificate) throws Exception {
        User user = userService.findById(userId).orElseThrow(() -> new Exception("User doesn't exist!"));
        if (!user.getAuthorities().contains(roleService.findByName("ROLE_ADMIN")) &&
                !Objects.equals(userId, certificate.getOwner().getId()))
            throw new Exception("You are not allowed to revoke this certificate!");
    }

    private void revokeAllInChain(Certificate certificate){
        //Potencijalno promentiti da bude neki enum da se zna da li je revoked
        certificate.setRevoked(true);
        certificateRepository.save(certificate);

        List<Certificate> childCertificates = certificateRepository.findAllByIssuer(certificate);
        if (certificate.getType().equals(CertificateType.END) || childCertificates.isEmpty())
            return;

        for (Certificate childCertificate: childCertificates){
            revokeAllInChain(childCertificate);
        }
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
        System.out.println(flags);
        String[] flagTokens = flags.split(",");
        int cumulativeFlag = 0;
        for (String token: flagTokens) {
            int flag = Integer.parseInt(token);
            flag = 1 << (7 - flag);  // Subtract flag from 7 before shifting
            cumulativeFlag |= flag;
        }
        return new KeyUsage(cumulativeFlag);
    }

    private X500Name generateX500Name(User owner) {
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        //builder.addRDN(BCStyle.CN, owner.getName() + " " + owner.getSurname());
        builder.addRDN(BCStyle.CN, "localhost");
        builder.addRDN(BCStyle.SURNAME, owner.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, owner.getName());
        return builder.build();
    }

    private void validateAlias(String alias) throws Exception {
        // Validate the alias to prevent path traversal attacks
        if (alias.contains("..")) {
            throw new Exception("Invalid alias");
        }
    }
}
