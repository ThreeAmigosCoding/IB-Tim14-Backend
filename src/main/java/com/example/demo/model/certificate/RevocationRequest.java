package com.example.demo.model.certificate;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "revocation_requests")
public class RevocationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "issuer_id")
    private Certificate issuer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "revocation_certificate_id")
    private Certificate revocationCertificate;

    @Column
    private String reason;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Column
    private Boolean approved;

    //region Constructors
    public RevocationRequest(Integer id, Certificate issuer, Certificate revocationCertificate,
                             String reason, LocalDate requestDate, Boolean approved) {
        this.id = id;
        this.issuer = issuer;
        this.revocationCertificate = revocationCertificate;
        this.reason = reason;
        this.requestDate = requestDate;
        this.approved = approved;
    }

    public RevocationRequest() {
    }

    //endregion

    //region Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Certificate getIssuer() {
        return issuer;
    }

    public void setIssuer(Certificate issuer) {
        this.issuer = issuer;
    }

    public Certificate getRevocationCertificate() {
        return revocationCertificate;
    }

    public void setRevocationCertificate(Certificate revocationCertificate) {
        this.revocationCertificate = revocationCertificate;
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
