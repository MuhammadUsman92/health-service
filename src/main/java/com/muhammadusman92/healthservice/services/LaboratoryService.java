package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.LaboratoryDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

public interface LaboratoryService {
    LaboratoryDto createLaboratory(LaboratoryDto laboratoryDto);
    LaboratoryDto updateLaboratory(LaboratoryDto laboratoryDto,Integer laboratoryId);
    LaboratoryDto getById(Integer laboratoryId);
    PageResponse<LaboratoryDto> getAllLaboratories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteLaboratory(Integer laboratoryId);
}
