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
        List<PatientDto> patientDtoList = new ArrayList<>();
        for (Patient patient : patients) {
            double patientLatitude = patient.getLocation().getLatitude();
            double patientLongitude = patient.getLocation().getLongitude();

            double distance = calculateDistance(latitude, longitude, patientLatitude, patientLongitude, radiusInDegrees);
            if (distance <= radiusInDegrees) {
                PatientDto patientDto = conversionDtos.patientToPatientDto(patient);
                patientDto.setDiseaseSet(patient.getDiseaseSet().stream().map(disease -> conversionDtos.diseaseToDiseaseDto(disease)).collect(Collectors.toSet()));
                patientDtoList.add(patientDto);
            }
        }
        return patientDtoList;
    }
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2, double radius) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c;
        return distance;
    }

}
