package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepo extends JpaRepository<Doctor,String> {
}
