package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import com.example.demo.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Autowired
    private UserService userService;

    @Override
    public CertificateRequestDTO createRequest(CertificateRequestDTO certificateRequestDTO) {
        CertificateRequest certificateRequest = new CertificateRequest(certificateRequestDTO);
        certificateRequest.setIssuer(userService.findById(certificateRequestDTO.getIssuerId()).orElseThrow());
        certificateRequest.setOwner(userService.findById(certificateRequestDTO.getOwnerId()).orElseThrow());
        certificateRequestRepository.save(certificateRequest);
        return new CertificateRequestDTO(certificateRequest);
    }
}
