package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.entity.Patient;
import com.muhammadusman92.healthservice.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrescriptionRepo extends JpaRepository<Prescription,Integer> {
    Page<Prescription> findByDisease(Disease disease, Pageable pageable);

}
