package com.example.demo.repository.certificate;

import com.example.demo.model.certificate.RevocationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RevocationRequestRepository extends JpaRepository<RevocationRequest, Integer> {

    @Query(value = "SELECT r FROM RevocationRequest r INNER JOIN r.issuer i ON r.issuer.id = i.id " +
            "INNER JOIN r.revocationCertificate c ON r.revocationCertificate.id = c.id " +
            "WHERE i.owner.id = :userId OR c.owner.id = :userId")
    List<RevocationRequest> findAllByUserId(Integer userId);


}
