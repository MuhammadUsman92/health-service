package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
public interface DiseaseService {
    DiseaseDto createDisease(String userEmail,String patientId,DiseaseDto diseaseDto);
    DiseaseDto updateDisease(DiseaseDto diseaseDto,Integer diseaseId);
    DiseaseDto getById(String authorities,String email,Integer diseaseId);
    PageResponse<DiseaseDto> getAllDiseases(String authorities,String userEmail,String patientId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteDisease(Integer diseaseId);
}
