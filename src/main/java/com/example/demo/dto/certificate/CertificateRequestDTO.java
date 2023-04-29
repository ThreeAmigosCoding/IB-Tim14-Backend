package com.example.demo.dto.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.certificate.CertificateType;

import java.time.LocalDate;

public class CertificateRequestDTO {

    private Integer id;
    private String signatureAlgorithm;
    private Integer issuerId;
    private LocalDate requestDate;
    private CertificateType type;
    private Integer ownerId;
    private Boolean approved;
    private String flags;

    // region Constructors

    public CertificateRequestDTO() { }

    public CertificateRequestDTO(Integer id, String signatureAlgorithm, Integer issuerId, LocalDate requestDate,
                                 CertificateType type, Integer ownerId, Boolean approved, String flags) {
        this.id = id;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerId = issuerId;
        this.requestDate = requestDate;
        this.type = type;
        this.ownerId = ownerId;
        this.approved = approved;
        this.flags = flags;
    }

    public CertificateRequestDTO(CertificateRequest certificateRequest) {
        this.id = certificateRequest.getId();
        this.signatureAlgorithm = certificateRequest.getSignatureAlgorithm();
        if (certificateRequest.getIssuer() != null)
            this.issuerId = certificateRequest.getIssuer().getId();
        this.requestDate = certificateRequest.getRequestDate();
        this.type = certificateRequest.getType();
        this.ownerId = certificateRequest.getOwner().getId();
        this.approved = certificateRequest.isApproved();
        this.flags = certificateRequest.getFlags();
    }

    // endregion

    // region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    // endregion

}
