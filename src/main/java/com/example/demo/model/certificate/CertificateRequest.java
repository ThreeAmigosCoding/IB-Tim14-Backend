package com.example.demo.model.certificate;

import com.example.demo.dto.certificate.CertificateRequestDTO;
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
    private Certificate issuer;

    @Column(nullable = false)
    private LocalDate requestDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "certificate_type")
    private CertificateType type;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column
    private Boolean approved;

    @Column
    private String flags;

    // region Constructors

    public CertificateRequest() {}

    public CertificateRequest(Integer id, String signatureAlgorithm, Certificate issuer, LocalDate requestDate,
                              CertificateType type, User owner, Boolean approved, String flags) {
        this.id = id;
        this.signatureAlgorithm = signatureAlgorithm;
        this.issuer = issuer;
        this.requestDate = requestDate;
        this.type = type;
        this.owner = owner;
        this.approved = approved;
        this.flags = flags;
    }

    public CertificateRequest(CertificateRequestDTO certificateRequestDTO) {
        this.signatureAlgorithm = certificateRequestDTO.getSignatureAlgorithm();
        this.requestDate = certificateRequestDTO.getRequestDate();
        this.type = certificateRequestDTO.getType();
        this.approved = certificateRequestDTO.getApproved();
        this.flags = certificateRequestDTO.getFlags();
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

    public Certificate getIssuer() {
        return issuer;
    }

    public void setIssuer(Certificate issuer) {
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

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    // endregion
}
