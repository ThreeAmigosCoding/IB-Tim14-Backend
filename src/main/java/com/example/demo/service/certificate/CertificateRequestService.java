package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.CertificateRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CertificateRequestService {

    CertificateRequest findById(Integer id);

    CertificateRequestDTO rejectCertificateRequest(Integer userId, Integer requestId) throws Exception;

    CertificateRequest validateRequestRejection(Integer userId, Integer requestId) throws Exception;

    List<CertificateRequestDTO> getAllUserRequests(Integer id);

    List<CertificateRequestDTO> findAllRequests();
}
