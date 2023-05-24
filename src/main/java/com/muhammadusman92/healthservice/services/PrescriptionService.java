package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PrescriptionDto;

public interface PrescriptionService {
    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto);
    PrescriptionDto createPrescription(PrescriptionDto prescriptionDto,Integer diseaseId,String doctorId,String hospitalUserEmail);
    PrescriptionDto updatePrescription(PrescriptionDto prescriptionDto,Integer prescriptionId);
    PrescriptionDto getById(Integer prescriptionId);
    PageResponse<PrescriptionDto> getAllPrescriptions(Integer diseaseId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deletePrescription(Integer prescriptionId);
}
