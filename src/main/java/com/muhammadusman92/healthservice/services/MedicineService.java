package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.MedicineDto;
import com.muhammadusman92.healthservice.payload.PageResponse;

public interface MedicineService {
    MedicineDto createMedicine(MedicineDto medicineDto);
    MedicineDto createMedicine(Integer prescriptionId,MedicineDto medicineDto);
    MedicineDto updateMedicine(MedicineDto medicineDto,Integer medicineId);
    MedicineDto getById(Integer medicineId);
    PageResponse<MedicineDto> getAllMedicines(Integer prescriptionId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteMedicine(Integer medicineId);
}
