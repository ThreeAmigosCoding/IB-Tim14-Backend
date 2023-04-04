package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateDTO;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public interface CertificateService {

    CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) throws Exception;

    Certificate issueCertificate(CertificateRequest certificateRequest) throws Exception;

    Certificate generateCertificate(CertificateRequest certificateRequest) throws Exception;

    void addCertificate(String alias, X509Certificate certificate, PrivateKey privateKey) throws Exception;

    X509Certificate loadCertificate(String alias) throws Exception;

    PrivateKey loadKey(String alias) throws Exception;

    BigInteger generateSerialNumber() throws NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException;

    KeyPair generateKeyPair();

    List<CertificateDTO> getAllCertificates();

    void checkValidity(Integer id) throws Exception;

}
