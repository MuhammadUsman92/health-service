package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.DoctorDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

public interface DoctorService {
    DoctorDto createDoctor(DoctorDto doctorDto);
    DoctorDto updateDoctor(DoctorDto doctorDto,Integer doctorId);
    DoctorDto getById(Integer doctorId);
    PageResponse<DoctorDto> getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteDoctor(Integer doctorId);
}
