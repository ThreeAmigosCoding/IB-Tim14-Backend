package com.example.demo.controller.certificate;

import com.example.demo.dto.ErrorDTO;
import com.example.demo.dto.certificate.CertificateDTO;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.service.certificate.CertificateRequestService;
import com.example.demo.service.certificate.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateRequestService certificateRequestService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/create-request", consumes = "application/json")
    public ResponseEntity<?> createRequest(@RequestBody CertificateRequestDTO certificateRequestDTO) {
        try {
            return new ResponseEntity<>(certificateService.createRequest(certificateRequestDTO), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping(value = "/create-certificate/{id}")
    public ResponseEntity<?> createCertificate(@PathVariable Integer id) throws Exception {
//        try {
            CertificateRequest certificateRequest = certificateRequestService.findById(id);
            Certificate certificate = certificateService.issueCertificate(certificateRequest);
            return new ResponseEntity<>(new CertificateDTO(certificate), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
//        }
    }

}
