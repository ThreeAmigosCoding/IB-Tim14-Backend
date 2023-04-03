package com.example.demo.repository.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Integer> {



}
