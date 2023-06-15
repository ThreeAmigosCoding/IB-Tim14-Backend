package com.example.demo.dto.certificate;


import com.example.demo.model.certificate.RevocationRequest;
import jakarta.validation.constraints.Pattern;

import java.math.BigInteger;
import java.time.LocalDate;

public class RevocationRequestDto {

    private Integer id;
    private Integer revocationCertificateId;

    @Pattern(regexp = "[a-zA-Z0-9. ]+")
    private String reason;
    private LocalDate requestDate;
    private Boolean approved;
    private BigInteger revocationCertificateSerialNumber;

    //region Constructors


    public RevocationRequestDto(Integer id, Integer revocationCertificateId, String reason,
                                LocalDate requestDate, Boolean approved, BigInteger revocationCertificateSerialNumber) {
        this.id = id;
        this.revocationCertificateId = revocationCertificateId;
        this.reason = reason;
        this.requestDate = requestDate;
        this.approved = approved;
        this.revocationCertificateSerialNumber = revocationCertificateSerialNumber;
    }

    public RevocationRequestDto() {
    }

    public RevocationRequestDto(RevocationRequest request) {
        this.id = request.getId();
        this.revocationCertificateId = request.getRevocationCertificate().getId();
        this.reason = request.getReason();
        this.requestDate = request.getRequestDate();
        this.approved = request.getApproved();
        this.revocationCertificateSerialNumber = request.getRevocationCertificate().getSerialNumber();
    }

    //endregion

    //region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRevocationCertificateId() {
        return revocationCertificateId;
    }

    public void setRevocationCertificateId(Integer revocationCertificateId) {
        this.revocationCertificateId = revocationCertificateId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public BigInteger getRevocationCertificateSerialNumber() {
        return revocationCertificateSerialNumber;
    }

    public void setRevocationCertificateSerialNumber(BigInteger revocationCertificateSerialNumber) {
        this.revocationCertificateSerialNumber = revocationCertificateSerialNumber;
    }

    //endregion

}
