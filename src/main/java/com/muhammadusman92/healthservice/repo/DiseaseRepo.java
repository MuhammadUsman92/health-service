package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiseaseRepo extends JpaRepository<Disease, Integer> {
    Page<Disease> findByPatient(Patient patient, Pageable pageable);
}
