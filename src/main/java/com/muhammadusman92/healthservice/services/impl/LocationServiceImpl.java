package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Patient;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PatientDto;
import com.muhammadusman92.healthservice.repo.LocationRepo;
import com.muhammadusman92.healthservice.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepo locationRepo;
    @Autowired
    private ConversionDtos conversionDtos;
    @Override
    public List<PatientDto> getAllPatientsWithInLocation(double latitude, double longitude, double radius) {
        double radiusInDegrees = radius / 111120.0;

        double minLatitude = latitude - radiusInDegrees;
        double maxLatitude = latitude + radiusInDegrees;

        double degreesPerLongitude = Math.cos(Math.toRadians(latitude)) * 111120.0;
        double radiusLongitude = radius / degreesPerLongitude;

        double minLongitude = longitude - radiusLongitude;
        double maxLongitude = longitude + radiusLongitude;
        List<Patient> patients = locationRepo.findAllPatientsWithinLocation(minLatitude, maxLatitude, minLongitude, maxLongitude);

        List<PatientDto> patientDtoList =new ArrayList<>();
        for(Patient patient:patients){
            PatientDto patientDto = conversionDtos.patientToPatientDto(patient);
            patientDto.setDiseaseSet(patient.getDiseaseSet().stream().map(disease->conversionDtos.diseaseToDiseaseDto(disease)).collect(Collectors.toSet()));
//            patientDto.setLocation(conversionDtos.locationToLocationDto(patient.getLocation()));
            patientDtoList.add(patientDto);
        }
        return patientDtoList;
    }
}
