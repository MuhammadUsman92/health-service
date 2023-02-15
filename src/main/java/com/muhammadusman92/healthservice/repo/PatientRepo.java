package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Patient;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepo extends JpaRepository<Patient,String> {
//    @Query("select  from Car c where lower(c.model) like lower(:model)",nativeQuery=true)
    @Query(value = "SELECT COUNT(CNIC) FROM Patient WHERE CNIC =:cnic",nativeQuery=true)
    long existsCNIC(@Param("cnic") String cnic);
}
