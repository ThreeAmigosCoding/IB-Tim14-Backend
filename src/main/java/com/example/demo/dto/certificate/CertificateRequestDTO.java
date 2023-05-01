package com.example.demo.dto.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import com.example.demo.model.certificate.CertificateType;

import java.math.BigInteger;
import java.time.LocalDate;

public class CertificateRequestDTO {

    private Integer id;
    private String signatureAlgorithm;
    private Integer issuerId;
    private Integer issuerOwnerId;
    private BigInteger issuerSerialNumber;
    private LocalDate requestDate;
    private CertificateType type;
    private Integer ownerId;
    private String ownerName;
    private Boolean approved;
    private String flags;

    // region Constructors

    public CertificateRequestDTO() { }

    public CertificateRequestDTO(Integer id, String signatureAlgorithm, Integer issuerId, Integer issuerOwnerId,
                                 BigInteger issuerSerialNumber, LocalDate requestDate, CertificateType type,
                                 Integer ownerId, String ownerName, Boolean approved, String flags) {
        this.id = id;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerId = issuerId;
        this.issuerOwnerId = issuerOwnerId;
        this.issuerSerialNumber = issuerSerialNumber;
        this.requestDate = requestDate;
        this.type = type;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.approved = approved;
        this.flags = flags;
    }

    public CertificateRequestDTO(CertificateRequest certificateRequest) {
        this.id = certificateRequest.getId();
        this.signatureAlgorithm = certificateRequest.getSignatureAlgorithm();
        if (certificateRequest.getIssuer() != null) {
            this.issuerId = certificateRequest.getIssuer().getId();
            this.issuerOwnerId = certificateRequest.getIssuer().getOwner().getId();
            this.issuerSerialNumber = certificateRequest.getIssuer().getSerialNumber();
        }
        this.requestDate = certificateRequest.getRequestDate();
        this.type = certificateRequest.getType();
        this.ownerId = certificateRequest.getOwner().getId();
        this.ownerName = certificateRequest.getOwner().getName() + " " + certificateRequest.getOwner().getSurname();
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

    public Integer getIssuerOwnerId() {
        return issuerOwnerId;
    }

    public void setIssuerOwnerId(Integer issuerOwnerId) {
        this.issuerOwnerId = issuerOwnerId;
    }

    public BigInteger getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(BigInteger issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
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
