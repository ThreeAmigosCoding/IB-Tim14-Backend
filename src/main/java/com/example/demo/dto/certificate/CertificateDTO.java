package com.example.demo.dto.certificate;

import com.example.demo.dto.user.UserDTO;
import com.example.demo.model.certificate.Certificate;
import com.example.demo.model.certificate.CertificateType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigInteger;
import java.time.LocalDate;

public class CertificateDTO {

    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigInteger serialNumber;

    @Pattern(regexp = "[A-Za-z0-9]+")
    @Size(max = 15)
    private String signatureAlgorithm;

    @Pattern(regexp = "^[A-Za-z0-9_]*(?!/|\\.\\.)[A-Za-z0-9_]*$")
    @Size(max = 100)
    private String issuerAlias;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Boolean revoked;

    private CertificateType type;

    private UserDTO owner;

    @Pattern(regexp = "^[A-Za-z0-9_]*(?!/|\\.\\.)[A-Za-z0-9_]*$")
    @Size(max = 100)
    private String alias;

    @Pattern(regexp = "[0-9,]+")
    @Size(max = 17)
    private String flags;

    //region Constructors

    public CertificateDTO() {}

    public CertificateDTO(Integer id, BigInteger serialNumber, String signatureAlgorithm, String issuerAlias,
                          LocalDate validFrom, LocalDate validTo, Boolean revoked, CertificateType type, UserDTO owner,
                          String alias, String flags) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuerAlias = issuerAlias;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.revoked = revoked;
        this.type = type;
        this.owner = owner;
        this.alias = alias;
        this.flags = flags;
    }

    public CertificateDTO(Certificate certificate) {
        this.id = certificate.getId();
        this.serialNumber = certificate.getSerialNumber();
        this.signatureAlgorithm = certificate.getSignatureAlgorithm();
        if(certificate.getIssuer() != null)
            this.issuerAlias = certificate.getIssuer().getAlias();
        this.validFrom = certificate.getValidFrom();
        this.validTo = certificate.getValidTo();
        this.revoked = certificate.getRevoked();
        this.type = certificate.getType();
        this.owner = new UserDTO(certificate.getOwner());
        this.alias = certificate.getAlias();
        this.flags = certificate.getFlags();
    }


    //endregion

    //region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getIssuerAlias() {
        return issuerAlias;
    }

    public void setIssuerAlias(String issuerAlias) {
        this.issuerAlias = issuerAlias;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public Boolean getRevoked() {
        return revoked;
    }

    public void setRevoked(Boolean revoked) {
        this.revoked = revoked;
    }

    public CertificateType getType() {
        return type;
    }

    public void setType(CertificateType type) {
        this.type = type;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    //endregion

}
