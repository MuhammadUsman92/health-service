package com.muhammadusman92.healthservice.services.impl;

import com.muhammadusman92.healthservice.config.ConversionDtos;
import com.muhammadusman92.healthservice.entity.Laboratory;
import com.muhammadusman92.healthservice.entity.Prescription;
import com.muhammadusman92.healthservice.entity.Report;
import com.muhammadusman92.healthservice.exception.ResourceNotFoundException;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.ReportDto;
import com.muhammadusman92.healthservice.repo.LaboratoryRepo;
import com.muhammadusman92.healthservice.repo.PrescriptionRepo;
import com.muhammadusman92.healthservice.repo.ReportRepo;
import com.muhammadusman92.healthservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ConversionDtos conversionDtos;
    @Autowired
    private ReportRepo reportRepo;
    @Autowired
    private LaboratoryRepo laboratoryRepo;
    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Override
    public ReportDto createReport(ReportDto reportDto, String laboratoryId) {
        Report report = conversionDtos.reportDtoToReport(reportDto);
        Laboratory laboratory=laboratoryRepo.findById(laboratoryId).orElseThrow(
                ()->new ResourceNotFoundException("Laboratory","LaboratoryId",laboratoryId));
        report.setLaboratory(laboratory);
        Report save = reportRepo.save(report);
        ReportDto reportDto1 = conversionDtos.reportToReportDto(save);
        return reportData(save,reportDto1);
    }

    @Override
    public ReportDto createReport(Integer prescriptionId, String laboratoryId, ReportDto reportDto) {
        Report report = conversionDtos.reportDtoToReport(reportDto);
        Prescription prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        report.setPrescription(prescription);
        Laboratory laboratory=laboratoryRepo.findById(laboratoryId).orElseThrow(
                ()->new ResourceNotFoundException("Laboratory","LaboratoryId",laboratoryId));
        report.setLaboratory(laboratory);
        Report save = reportRepo.save(report);
        ReportDto reportDto1 = conversionDtos.reportToReportDto(save);
        return reportData(save,reportDto1);
    }

    @Override
    public ReportDto updateReport(ReportDto reportDto, Integer reportId) {
        Report report = conversionDtos.reportDtoToReport(reportDto);
        Report reportFind = reportRepo.findById(reportId).orElseThrow(
                ()->new ResourceNotFoundException("Report","ReportId",reportId));
        report.setId(reportFind.getId());
        report.setLaboratory(reportFind.getLaboratory());
        report.setPrescription(reportFind.getPrescription());
        Report save = reportRepo.save(report);
        ReportDto reportDto1 = conversionDtos.reportToReportDto(save);
        return reportData(save,reportDto1);
    }

    @Override
    public ReportDto getById(Integer reportId) {
        Report reportFind = reportRepo.findById(reportId).orElseThrow(
                ()->new ResourceNotFoundException("Report","ReportId",reportId));
        ReportDto reportDto = conversionDtos.reportToReportDto(reportFind);
        return reportData(reportFind,reportDto);
    }

    @Override
    public PageResponse<ReportDto> getAllReports(Integer prescriptionId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort= (sortDir.equalsIgnoreCase("asc"))?Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize, sort);
        Prescription prescription = prescriptionRepo.findById(prescriptionId).orElseThrow(
                ()->new ResourceNotFoundException("Prescription","PrescriptionId",prescriptionId));
        Page<Report> reportPage = reportRepo.findByPrescription(prescription,pageable);
        PageResponse<ReportDto> reportDtoPageResponse=new PageResponse<>();
        List<ReportDto> reportDtoList = reportPage.getContent().stream().map(
                report -> conversionDtos.reportToReportDto(report)).toList();
        reportDtoPageResponse.setContent(reportDtoList);
        reportDtoPageResponse.setTotalPage(reportPage.getTotalPages());
        reportDtoPageResponse.setPageNumber(reportPage.getNumber());
        reportDtoPageResponse.setPageSize(reportPage.getSize());
        reportDtoPageResponse.setLast(reportPage.isLast());
        reportDtoPageResponse.setTotalElements(reportPage.getTotalElements());
        return reportDtoPageResponse;
    }
    @Override
    public void deleteReport(Integer reportId) {
        Report reportFind = reportRepo.findById(reportId).orElseThrow(
                ()->new ResourceNotFoundException("Report","ReportId",reportId));
        reportRepo.delete(reportFind);
    }
    private ReportDto reportData(Report report,ReportDto reportDto){
        reportDto.setLaboratoryDto(conversionDtos.laboratoryToLaboratoryDto(report.getLaboratory()));
        reportDto.setPrescription(conversionDtos.prescriptionToPrescriptionDto(report.getPrescription()));
        return reportDto;
    }
}
