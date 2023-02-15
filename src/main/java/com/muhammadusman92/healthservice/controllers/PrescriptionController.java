package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.entity.Prescription;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PrescriptionDto;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;
    @PostMapping("/diseaseId/{diseaseId}/doctorId/{doctorId}/hospitalId/{hospitalId}")
    public ResponseEntity<Response> createPrescription(
            @RequestBody PrescriptionDto prescriptionDto,
            @PathVariable Integer diseaseId,
            @PathVariable Integer doctorId,
            @PathVariable Integer hospitalId){
        PrescriptionDto savedPrescription = prescriptionService.createPrescription(prescriptionDto,diseaseId,doctorId,hospitalId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Prescription is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedPrescription)
                .build(), CREATED);
    }
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<Response> updatePrescription(@RequestBody PrescriptionDto prescriptionDto,
                                                       @PathVariable Integer prescriptionId){
        PrescriptionDto updatePrescription = prescriptionService.updatePrescription(prescriptionDto,prescriptionId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Prescription is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updatePrescription)
                .build(),OK);
    }
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<Response> deletePrescription(@PathVariable Integer prescriptionId){
        prescriptionService.deletePrescription(prescriptionId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Prescription deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/diseaseId/{diseaseId}")
    public ResponseEntity<Response> getAllPrescriptionOfDisease(
            @PathVariable Integer diseaseId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<PrescriptionDto> pageResponse = prescriptionService.getAllPrescriptions(diseaseId,pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Prescription are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<Response> getPrescriptionById(@PathVariable Integer prescriptionId){
        PrescriptionDto prescriptionDto=prescriptionService.getById(prescriptionId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Prescription with id "+prescriptionId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(prescriptionDto)
                .build(),OK);
    }
}
