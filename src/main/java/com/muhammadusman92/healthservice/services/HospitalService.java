package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.HospitalDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

public interface HospitalService {
    HospitalDto createHospital(HospitalDto hospitalDto);
    HospitalDto updateHospital(HospitalDto hospitalDto,Integer hospitalId);
    HospitalDto getById(Integer hospitalId);
    PageResponse<HospitalDto> getAllHospitals(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteHospital(Integer hospitalId);
}
