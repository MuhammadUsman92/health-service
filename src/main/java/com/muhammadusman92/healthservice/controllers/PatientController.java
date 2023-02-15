package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.PatientDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.PatientService;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @PostMapping("/")
    public ResponseEntity<Response> createPatientById(@RequestBody PatientDto patientDto){
        PatientDto savedPatient = patientService.createPatient(patientDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Patient is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedPatient)
                .build(), CREATED);
    }
    @PutMapping("/{patientId}")
    public ResponseEntity<Response> updatePatient(@RequestBody PatientDto patientDto,@PathVariable String patientId){
        PatientDto updatePatient = patientService.updatePatient(patientDto,patientId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Patient is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updatePatient)
                .build(),OK);
    }
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Response> deletePatient(@PathVariable String patientId){
        patientService.deletePatient(patientId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Patient deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/")
    public ResponseEntity<Response> getAllPatient(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.PATIENT_SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<PatientDto> pageResponse = patientService.getAllPatients(pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Patient are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{patientId}")
    public ResponseEntity<Response> getPatientById(@PathVariable String patientId){
        PatientDto patientDto=patientService.getById(patientId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Patient with id "+patientId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(patientDto)
                .build(),OK);
    }
}
