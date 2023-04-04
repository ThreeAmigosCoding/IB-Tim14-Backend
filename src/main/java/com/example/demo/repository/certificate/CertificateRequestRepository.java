package com.example.demo.repository.certificate;

import com.example.demo.model.certificate.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Integer> {

    List<CertificateRequest> findAllByOwnerId(Integer id);

}
