package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location,Integer> {
}
