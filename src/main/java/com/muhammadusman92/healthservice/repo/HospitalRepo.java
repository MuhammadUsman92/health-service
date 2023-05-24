package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepo extends JpaRepository<Hospital,String> {
}
