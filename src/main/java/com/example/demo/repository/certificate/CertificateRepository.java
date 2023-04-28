package com.example.demo.repository.certificate;

import com.example.demo.model.certificate.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    public Optional<Certificate> findBySerialNumber(BigInteger serialnumber);

}
