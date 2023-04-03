package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {

    CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO);

    Certificate issueCertificate(CertificateRequest certificateRequest);

    Certificate exportGeneratedCertificate(X509Certificate certificate);

    void validate(CertificateRequest certificateRequest);

    X509Certificate parseFlags(String keyUsageFlags);

    X509Certificate generateCertificate(CertificateRequest certificateRequest);
}
