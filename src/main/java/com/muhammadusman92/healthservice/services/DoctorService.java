package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.DoctorDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

import java.util.List;

public interface DoctorService {
    DoctorDto createDoctor(String userEmail,DoctorDto doctorDto);
    DoctorDto updateDoctor(DoctorDto doctorDto,String doctorId);
    DoctorDto getById(String doctorId);
    PageResponse<DoctorDto> getAllDoctors(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    List<DoctorDto> getAllDoctors(String userEmail);
    void deleteDoctor(String doctorId);
}
