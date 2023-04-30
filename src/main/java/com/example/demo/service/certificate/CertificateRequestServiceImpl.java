package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.repository.certificate.CertificateRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateRequestServiceImpl implements CertificateRequestService{

    @Autowired
    private CertificateRequestRepository certificateRequestRepository;

    @Override
    public CertificateRequest findById(Integer id) {
        return certificateRequestRepository.findById(id).orElseThrow();
    }

    @Override
    public CertificateRequestDTO rejectCertificateRequest(Integer userId, Integer requestId) {
        CertificateRequest certificateRequest = certificateRequestRepository.findById(requestId).orElseThrow();
        certificateRequest.setApproved(false);
        return new CertificateRequestDTO(certificateRequestRepository.save(certificateRequest));
    }

    @Override
    public List<CertificateRequestDTO> getAllUserRequests(Integer id) {
        return certificateRequestRepository.findAllByOwnerId(id).stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CertificateRequestDTO> findAllRequests() {
        return certificateRequestRepository.findAll().stream()
                .map(CertificateRequestDTO::new).collect(Collectors.toList());
    }
}
