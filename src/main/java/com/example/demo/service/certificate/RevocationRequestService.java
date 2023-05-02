package com.example.demo.service.certificate;

import com.example.demo.dto.certificate.RevocationRequestDto;
import com.example.demo.model.certificate.RevocationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RevocationRequestService {

    RevocationRequest findById(Integer id) throws Exception;

    RevocationRequestDto rejectRevocationRequest(Integer userId, Integer requestId) throws Exception;

    List<RevocationRequestDto> getAllUserRequests(Integer userId);

}
