package com.example.demo.dto.certificate;


import com.example.demo.model.certificate.RevocationRequest;

import java.time.LocalDate;

public class RevocationRequestDto {

    private Integer id;
    private Integer issuerId;
    private Integer revocationCertificateId;
    private String reason;
    private LocalDate requestDate;
    private Boolean approved;

    //region Constructors

    public RevocationRequestDto(Integer id, Integer issuerId, Integer revocationCertificateId,
                                String reason, LocalDate requestDate, Boolean approved) {
        this.id = id;
        this.issuerId = issuerId;
        this.revocationCertificateId = revocationCertificateId;
        this.reason = reason;
        this.requestDate = requestDate;
        this.approved = approved;
    }

    public RevocationRequestDto() {
    }

    public RevocationRequestDto(RevocationRequest request) {
        this.id = request.getId();
        this.issuerId = request.getIssuer().getId();
        this.revocationCertificateId = request.getRevocationCertificate().getId();
        this.reason = request.getReason();
        this.requestDate = request.getRequestDate();
        this.approved = request.getApproved();
    }

    //endregion

    //region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Integer issuerId) {
        this.issuerId = issuerId;
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

    //endregion

}
