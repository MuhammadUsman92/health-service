package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.ReportDto;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.FileService;
import com.muhammadusman92.healthservice.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

import static java.time.LocalDateTime.now;
import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    private FileService fileService;

    @PostMapping("/prescriptionId/{prescriptionId}/laboratoryId/{laboratoryId}")
    public ResponseEntity<Response> createReport(
            @RequestHeader("authorities") String authorities,
            @RequestBody ReportDto reportDto,
            @PathVariable Integer prescriptionId,
            @PathVariable String laboratoryId){
        if (authorities.contains("HOSPITAL_USER")) {
            ReportDto savedReport = reportService.createReport(prescriptionId,laboratoryId,reportDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Report is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedReport)
                    .build(), CREATED);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }

    }
    @PutMapping("/{reportId}")
    public ResponseEntity<Response> updateReport(@RequestHeader("authorities") String authorities,
                                                 @RequestBody ReportDto reportDto,
                                                 @PathVariable Integer reportId){
        if (authorities.contains("RESCUE_ADMIN")) {
            ReportDto updateReport = reportService.updateReport(reportDto,reportId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Report is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updateReport)
                    .build(),OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }
    }
    @DeleteMapping("/{reportId}")
    public ResponseEntity<Response> deleteReport(@RequestHeader("authorities") String authorities,
                                                 @PathVariable Integer reportId){
        if (authorities.contains("RESCUE_ADMIN")) {
            reportService.deleteReport(reportId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Report deleted successfully")
                    .status(OK)
                    .statusCode(OK.value())
                    .build(),OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }

    }
    @GetMapping("/prescriptionId/{prescriptionId}")
    public ResponseEntity<Response> getAllReportOfDisease(
            @RequestHeader("authorities") String authorities,
            @PathVariable Integer prescriptionId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_USER")) {
            PageResponse<ReportDto> pageResponse = reportService.getAllReports(prescriptionId,pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Report are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(pageResponse)
                    .build(),OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }
    }
    @GetMapping("/{reportId}")
    public ResponseEntity<Response> getReportById(@RequestHeader("authorities") String authorities,
                                                  @PathVariable Integer reportId){
        if (authorities.contains("RESCUE_USER")) {
            ReportDto reportDto=reportService.getById(reportId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Report with id "+reportId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(reportDto)
                    .build(),OK);
        } else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }
    }
    @PostMapping("/file/upload/")
    public ResponseEntity<Response> uploadFile(
            @RequestParam("file") MultipartFile file) throws IOException {
        String fileName = this.fileService.uploadFile(path, file);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .status(OK)
                .statusCode(OK.value())
                .message("File uploaded successfully")
                .data(fileName)
                .build(), OK);
    }

    @GetMapping(value = "/file/{fileName}")
    public void downloadFile(
            @PathVariable String fileName,
            HttpServletResponse response
    ) throws IOException {
        InputStream resource = this.fileService.getResource(path, fileName);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
