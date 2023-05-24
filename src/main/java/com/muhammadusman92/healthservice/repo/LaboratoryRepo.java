package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratoryRepo extends JpaRepository<Laboratory,String> {
}
