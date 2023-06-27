package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.PatientDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

public interface PatientService {
    PatientDto createPatient(String userEmail,PatientDto patientDto);
    PatientDto updatePatient(PatientDto patientDto,String patientId);
    PatientDto getById(String authorities,String userEmail,String patientId);
    PatientDto getByEmail(String authorities,String userEmail);
    PageResponse<PatientDto> getAllPatients(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deletePatient(String patientId);
}
