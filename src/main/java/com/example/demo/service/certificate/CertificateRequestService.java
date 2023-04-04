package com.example.demo.service.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface CertificateRequestService {

    public CertificateRequest findById(Integer id);

}
