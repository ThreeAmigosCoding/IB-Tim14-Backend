package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.repository.certificate.CertificateRepository;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    @Override
    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) {
        CertificateRequest certificateRequest = new CertificateRequest(certificateRequestDTO);
        certificateRequest.setIssuer(certificateRepository.findById(certificateRequestDTO.getIssuerId()).orElseThrow());
        certificateRequest.setOwner(userService.findById(certificateRequestDTO.getOwnerId()).orElseThrow());
        certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestDTO(certificateRequest);
    }

    @Override
    public Certificate issueCertificate(CertificateRequest certificateRequest) {
        this.validate(certificateRequest);
        X509Certificate x509Certificate = this.generateCertificate(certificateRequest);
        return exportGeneratedCertificate(x509Certificate);
    }

    @Override
    public Certificate exportGeneratedCertificate(X509Certificate certificate) {
        return null;
    }

    @Override
    public void validate(CertificateRequest certificateRequest) {

    }

    @Override
    public X509Certificate parseFlags(String keyUsageFlags) {
        return null;
    }

    @Override
    public X509Certificate generateCertificate(CertificateRequest certificateRequest) {
        return null;
    }
}
