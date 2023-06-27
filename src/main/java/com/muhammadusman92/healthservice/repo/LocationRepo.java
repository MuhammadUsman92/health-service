package com.muhammadusman92.healthservice.repo;

import com.muhammadusman92.healthservice.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import com.muhammadusman92.healthservice.entity.Patient;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepo extends JpaRepository<Location,Integer> {
    @Query("SELECT DISTINCT l.patient FROM Location l WHERE l.latitude BETWEEN :minLatitude AND :maxLatitude AND l.longitude BETWEEN :minLongitude AND :maxLongitude")
    List<Patient> findAllPatientsWithinLocation(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);

}
