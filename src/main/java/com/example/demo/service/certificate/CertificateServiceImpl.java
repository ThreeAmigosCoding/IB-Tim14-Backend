package com.example.demo.service.certificate;

import ch.qos.logback.core.net.ssl.KeyStoreFactoryBean;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.repository.certificate.CertificateRepository;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.user.UserService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private KeyStoreFactoryBean keyStoreFactory;

    @Override
    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) {
        CertificateRequest certificateRequest = new CertificateRequest(certificateRequestDTO);
        certificateRequest.setIssuer(certificateRepository.findById(certificateRequestDTO.getIssuerId()).orElseThrow());
        certificateRequest.setOwner(userService.findById(certificateRequestDTO.getOwnerId()).orElseThrow());
        certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestDTO(certificateRequest);
    }

    @Override
    public Certificate issueCertificate(CertificateRequest certificateRequest) throws Exception {
        X509Certificate x509Certificate = this.generateCertificate(certificateRequest);
        return exportGeneratedCertificate(x509Certificate);
    }

    @Override
    public Certificate exportGeneratedCertificate(X509Certificate certificate) {
        return null;
    }

    @Override
    public X509Certificate parseFlags(String keyUsageFlags) {
        return null;
    }

    @Override
    public X509Certificate generateCertificate(CertificateRequest certificateRequest) throws Exception {
        X509Certificate issuerCertificate = null;
        if (certificateRequest.getIssuer() != null)
            issuerCertificate = loadCertificate(certificateRequest.getIssuer().getAlias());

        if (!(certificateRequest.getIssuer().getValidFrom().isBefore(LocalDate.now()) &&
            certificateRequest.getIssuer().getValidTo().isAfter(LocalDate.now())))
            throw new Exception("Issuer not valid!");

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");
        ContentSigner contentSigner = builder.build(loadKey(certificateRequest.getIssuer().getAlias()));

        KeyPair keyPair = generateKeyPair();
        BigInteger serialNumber = generateSerialNumber();
        Date from = new Date();
        Date to = new Date();

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                X500Name.getInstance(issuerCertificate.getSubjectX500Principal()),
                serialNumber,
                from,
                to,
                new X500Name(certificateRequest.getOwner().getName()),
                keyPair.getPublic());

        X509CertificateHolder certHolder = certificateBuilder.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate newCertificate = certConverter.getCertificate(certHolder);
        String alias = newCertificate.getSerialNumber() + "_" + certificateRequest.getOwner().getName() + "_" +
                certificateRequest.getOwner().getSurname();
        addCertificate(alias, newCertificate);
        Certificate certificate = new Certificate(certificateRequest);
        certificate.setAlias(alias);
        certificate.setSerialNumber(serialNumber);
        certificate.setValidFrom(fromDateToLocalDate(from));
        certificate.setValidTo(fromDateToLocalDate(to));

        certificateRepository.save(certificate);

        // TODO: save certificate private key

        return certConverter.getCertificate(certHolder);
    }

    @Override
    public X509Certificate loadCertificate(String alias) throws Exception {
        KeyStore keyStore = keyStoreFactory.createKeyStore();
        return (X509Certificate) keyStore.getCertificate(alias);
    }

    @Override
    public PrivateKey loadKey(String alias) throws NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
            UnrecoverableKeyException {
        return (PrivateKey) keyStoreFactory.createKeyStore().getKey(alias, "keystore-password".toCharArray());
    }

    @Override
    public void addCertificate(String alias, X509Certificate certificate) throws Exception {
        KeyStore keyStore = keyStoreFactory.createKeyStore();
        KeyStore.Entry entry = new KeyStore.TrustedCertificateEntry(certificate);
        keyStore.setEntry(alias, entry, null);
    }

    @Override
    public BigInteger generateSerialNumber() throws NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException {
        return BigInteger.ONE;
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

    private LocalDate fromDateToLocalDate(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.toLocalDate();
    }
}
