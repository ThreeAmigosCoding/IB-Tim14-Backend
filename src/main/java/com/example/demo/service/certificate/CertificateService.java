package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface CertificateService {

    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO);

}
