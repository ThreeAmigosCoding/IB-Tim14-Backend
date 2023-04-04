package com.example.demo.model.certificate;

import com.example.demo.model.user.User;
import jakarta.persistence.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private BigInteger serialNumber;

    @Column(nullable = false)
    private String signatureAlgorithm;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "issuer_id")
    private Certificate issuer;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column
    private Boolean valid;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "certificate_type")
    private CertificateType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(nullable = false)
    private String alias;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "certificate_request_id")
    private CertificateRequest certificateRequest;

    @Column(nullable = false)
    private String flags;

    // region Constructors

    public Certificate() { }

    public Certificate(Integer id, BigInteger serialNumber, String signatureAlgorithm, Certificate issuer,
                       LocalDate validFrom, LocalDate validTo, Boolean valid, CertificateType type,
                       User owner, String alias, CertificateRequest certificateRequest, String flags) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuer = issuer;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.valid = valid;
        this.type = type;
        this.owner = owner;
        this.alias = alias;
        this.certificateRequest = certificateRequest;
        this.flags = flags;
    }

    public Certificate(CertificateRequest certificateRequest, String alias, BigInteger serialNumber,
                       LocalDate validFrom, LocalDate validTo) {
        this.signatureAlgorithm = certificateRequest.getSignatureAlgorithm();
        this.issuer = certificateRequest.getIssuer();
        this.type = certificateRequest.getType();
        this.owner = certificateRequest.getOwner();
        this.certificateRequest = certificateRequest;
        this.flags = certificateRequest.getFlags();
        this.alias = alias;
        this.serialNumber = serialNumber;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.valid = true;
    }

    // endregion

    // region Getters and Setters

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

    public Certificate getIssuer() {
        return issuer;
    }

    public void setIssuer(Certificate issuer) {
        this.issuer = issuer;
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

    public Boolean isValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public CertificateType getType() {
        return type;
    }

    public void setType(CertificateType type) {
        this.type = type;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public CertificateRequest getCertificateRequest() {
        return certificateRequest;
    }

    public void setCertificateRequest(CertificateRequest certificateRequest) {
        this.certificateRequest = certificateRequest;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    // endregion
}
