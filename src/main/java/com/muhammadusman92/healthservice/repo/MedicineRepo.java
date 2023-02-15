package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Medicine;
import com.muhammadusman92.healthservice.entity.Prescription;
import com.muhammadusman92.healthservice.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepo extends JpaRepository<Medicine,Integer> {
    Page<Medicine> findByPrescription(Prescription prescription, Pageable pageable);
}
