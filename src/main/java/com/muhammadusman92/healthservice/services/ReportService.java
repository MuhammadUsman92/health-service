package com.muhammadusman92.healthservice.services;

import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.ReportDto;

public interface ReportService {
    ReportDto createReport(ReportDto reportDto,String laboratoryId);
    ReportDto createReport(Integer prescriptionId,String laboratoryId,ReportDto reportDto);
    ReportDto updateReport(ReportDto reportDto,Integer reportId);
    ReportDto getById(Integer reportId);
    PageResponse<ReportDto> getAllReports(Integer prescriptionId,Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    void deleteReport(Integer reportId);
}
