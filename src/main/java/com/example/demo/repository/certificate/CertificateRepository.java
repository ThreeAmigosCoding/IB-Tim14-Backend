package com.example.demo.repository.certificate;

import com.example.demo.model.certificate.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Optional<Certificate> findBySerialNumber(BigInteger serialnumber);

    List<Certificate> findAllByIssuer(Certificate issuer);

    Optional<Certificate> findByAlias(String alias);

}
