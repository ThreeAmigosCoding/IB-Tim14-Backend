package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;

@Service
public interface CertificateService {

    CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO);

    Certificate issueCertificate(CertificateRequest certificateRequest) throws Exception;

    Certificate exportGeneratedCertificate(X509Certificate certificate);

    X509Certificate parseFlags(String keyUsageFlags);

    X509Certificate generateCertificate(CertificateRequest certificateRequest) throws Exception;

    void addCertificate(String alias, X509Certificate certificate) throws Exception;

    X509Certificate loadCertificate(String alias) throws Exception;

    PrivateKey loadKey(String alias) throws NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException,
            UnrecoverableKeyException;

    BigInteger generateSerialNumber() throws NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException;

    KeyPair generateKeyPair();
}
