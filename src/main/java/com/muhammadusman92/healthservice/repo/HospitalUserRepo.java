package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.HospitalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HospitalUserRepo extends JpaRepository<HospitalUser,Integer> {

    Optional<HospitalUser> findByEmail(String email);
}
