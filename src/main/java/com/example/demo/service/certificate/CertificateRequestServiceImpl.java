package com.example.demo.service.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CertificateRequestServiceImpl implements CertificateRequestService{

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Override
    public CertificateRequest findById(Integer id) {
        return certificateRequestRepository.findById(id).orElseThrow();
    }
}
