package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.ReportDto;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @PostMapping("/prescriptionId/{prescriptionId}/laboratoryId/{laboratoryId}")
    public ResponseEntity<Response> createReport(
            @RequestBody ReportDto reportDto,
            @PathVariable Integer prescriptionId,
            @PathVariable Integer laboratoryId){
        ReportDto savedReport = reportService.createReport(prescriptionId,laboratoryId,reportDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Report is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedReport)
                .build(), CREATED);
    }
    @PutMapping("/{reportId}")
    public ResponseEntity<Response> updateReport(@RequestBody ReportDto reportDto,
                                                       @PathVariable Integer reportId){
        ReportDto updateReport = reportService.updateReport(reportDto,reportId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Report is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updateReport)
                .build(),OK);
    }
    @DeleteMapping("/{reportId}")
    public ResponseEntity<Response> deleteReport(@PathVariable Integer reportId){
        reportService.deleteReport(reportId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Report deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/prescriptionId/{prescriptionId}")
    public ResponseEntity<Response> getAllReportOfDisease(
            @PathVariable Integer prescriptionId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<ReportDto> pageResponse = reportService.getAllReports(prescriptionId,pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Report are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{reportId}")
    public ResponseEntity<Response> getReportById(@PathVariable Integer reportId){
        ReportDto reportDto=reportService.getById(reportId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Report with id "+reportId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(reportDto)
                .build(),OK);
    }
}
