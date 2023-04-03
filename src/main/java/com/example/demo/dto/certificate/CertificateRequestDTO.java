package com.example.demo.dto.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.certificate.CertificateType;

import java.time.LocalDate;

public class CertificateRequestDTO {

    private String signatureAlgorithm;
    private Integer issuerId;
    private LocalDate requestDate;
    private CertificateType type;
    private Integer ownerId;
    private Boolean approved;

    // region Constructors

    public CertificateRequestDTO() { }

    public CertificateRequestDTO(String signatureAlgorithm, Integer issuerId, LocalDate requestDate,
                                 CertificateType type, Integer ownerId, Boolean approved) {
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerId = issuerId;
        this.requestDate = requestDate;
        this.type = type;
        this.ownerId = ownerId;
        this.approved = approved;
    }

    public CertificateRequestDTO(CertificateRequest certificateRequest) {
        this.signatureAlgorithm = certificateRequest.getSignatureAlgorithm();
        this.issuerId = certificateRequest.getIssuer().getId();
        this.requestDate = certificateRequest.getRequestDate();
        this.type = certificateRequest.getType();
        this.ownerId = certificateRequest.getOwner().getId();
        this.approved = certificateRequest.isApproved();
    }

    // endregion

    // region Getters and Setters

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public Integer getIssuerId() {
        return issuerId;
    }

    public void setIssuerId(Integer issuerId) {
        this.issuerId = issuerId;
    }

    public LocalDate getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public CertificateType getType() {
        return type;
    }

    public void setType(CertificateType type) {
        this.type = type;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    // endregion

}
