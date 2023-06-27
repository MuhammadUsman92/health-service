package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.PatientDto;

import java.util.List;

public interface LocationService {
    List<PatientDto> getAllPatientsWithInLocation(double latitude, double longitude, double radius);

}
