package com.example.demo.model.certificate;

import com.example.demo.model.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "certificate_requests")
public class CertificateRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String signatureAlgorithm;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "issuer_id")
    private User issuer;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Enumerated(value = EnumType.STRING)
    private CertificateType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private Boolean approved;

    // region Constructors

    public CertificateRequest() {}

    public CertificateRequest(Integer id, String signatureAlgorithm, User issuer, LocalDate requestDate,
                              CertificateType type, User owner, Boolean approved) {
        this.id = id;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuer = issuer;
        this.requestDate = requestDate;
        this.type = type;
        this.owner = owner;
        this.approved = approved;
    }

    // endregion Constructors

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

    public User getIssuer() {
        return issuer;
    }

    public void setIssuer(User issuer) {
        this.issuer = issuer;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Boolean isApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    // endregion
}
