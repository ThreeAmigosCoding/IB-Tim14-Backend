package com.example.demo.controller.certificate;

import com.example.demo.dto.ErrorDTO;
import com.example.demo.dto.certificate.CertificateDTO;
import com.example.demo.dto.certificate.CertificateRequestDTO;
import com.example.demo.dto.certificate.DownloadDto;
import com.example.demo.dto.certificate.RevocationRequestDto;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.service.certificate.CertificateRequestService;
import com.example.demo.service.certificate.CertificateService;
import com.example.demo.service.certificate.RevocationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateRequestService certificateRequestService;

    @Autowired
    private RevocationRequestService revocationRequestService;

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
    @PostMapping(value = "/create-certificate/{user-id}/{request-id}")
    public ResponseEntity<?> createCertificate(@PathVariable("user-id") Integer userId,
                                               @PathVariable("request-id") Integer requestId) {
        try {
            CertificateRequest certificateRequest = certificateRequestService.findById(requestId);
            Certificate certificate = certificateService.issueCertificate(certificateRequest, userId);
            return new ResponseEntity<>(new CertificateDTO(certificate), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(value = "/reject-certificate/{user-id}/{request-id}")
    public ResponseEntity<?> rejectCertificateRequest(@PathVariable("user-id") Integer userId,
                                                      @PathVariable("request-id") Integer requestId) {

        //TODO Validirano, proveriti da li je to to
        try {
            CertificateRequestDTO certificateRequestDTO = certificateRequestService.rejectCertificateRequest(userId, requestId);
            return new ResponseEntity<>(certificateRequestDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/certificates")
    public ResponseEntity<?> getCertificates() {
        try {
            List<CertificateDTO> certificateDTOS = certificateService.getAllCertificates();
            return new ResponseEntity<>(certificateDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/certificate-requests/{id}")
    public ResponseEntity<?> getCertificateRequests(@PathVariable Integer id) {
        try {
            List<CertificateRequestDTO> certificateRequestDTOS = certificateRequestService.getAllUserRequests(id);
            return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/certificate-requests")
    public ResponseEntity<?> getAllCertificateRequests() {
        try {
            List<CertificateRequestDTO> certificateRequestDTOS = certificateRequestService.findAllRequests();
            return new ResponseEntity<>(certificateRequestDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping(value = "/validity/{serialNumber}")
    public ResponseEntity<?> getValidity(@PathVariable BigInteger serialNumber) {
        try {
            certificateService.checkValidity(serialNumber);
            return new ResponseEntity<>("Certificate is valid.", HttpStatus.OK);
        } catch (CertificateExpiredException e) {
            return new ResponseEntity<>("Certificate is expired.", HttpStatus.OK);
        } catch (CertificateNotYetValidException e) {
            return new ResponseEntity<>("Certificate is not yet valid.", HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping(value = "/validity")
    public ResponseEntity<?> getValidityFromCopy(@RequestParam("certificate") MultipartFile certificate){
        try {
            certificateService.checkValidityFromCopy(certificate);
            return new ResponseEntity<>("Certificate is valid.", HttpStatus.OK);
        } catch (CertificateExpiredException e) {
            return new ResponseEntity<>("Certificate is expired.", HttpStatus.OK);
        } catch (CertificateNotYetValidException e) {
            return new ResponseEntity<>("Certificate is not yet valid.", HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/download/{alias}/{userId}")
    public ResponseEntity<?> downloadFile(@PathVariable("alias") String alias, @PathVariable Integer userId) {
        try {
            DownloadDto downloadDto = certificateService.getCertificateForDownload(alias, userId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(downloadDto.getContentType()))
                    .contentLength(downloadDto.getDownloadResource().contentLength())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                            downloadDto.getFileName() + "\"")
                    .body(downloadDto.getDownloadResource());
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/revocation/{requestId}/{userId}")
    public ResponseEntity<?> acceptRevocationRequest(@PathVariable Integer requestId, @PathVariable Integer userId){
        try {
            certificateService.revokeCertificateChain(requestId, userId);
            return new ResponseEntity<>("Certificate chain revoked!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/revocation/{requestId}/{userId}")
    public ResponseEntity<?> rejectRevocationRequest(@PathVariable Integer requestId, @PathVariable Integer userId){
        try {
            return new ResponseEntity<>(revocationRequestService.rejectRevocationRequest(userId, requestId),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/revocation/create/{userId}")
    public ResponseEntity<?> createRevocationRequest(@RequestBody RevocationRequestDto revocationRequestDto,
                                                     @PathVariable Integer userId){
        try {
            return new ResponseEntity<>(certificateService.createRevocationRequest(revocationRequestDto, userId),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
