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
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @PostMapping("/")
    public ResponseEntity<Response> createPatientById(
            @RequestHeader("authorities") String authorities,
            @RequestHeader("userEmail") String userEmail,
            @RequestBody PatientDto patientDto){
        if (authorities.contains("HOSPITAL_ADMIN")) {
            PatientDto savedPatient = patientService.createPatient(userEmail,patientDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Patient is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedPatient)
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
    @PutMapping("/{patientId}")
    public ResponseEntity<Response> updatePatient(@RequestHeader("authorities") String authorities,
                                                  @RequestBody PatientDto patientDto,@PathVariable String patientId){
        if (authorities.contains("HOSPITAL_ADMIN")) {
            PatientDto updatePatient = patientService.updatePatient(patientDto,patientId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Patient is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updatePatient)
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
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Response> deletePatient(@RequestHeader("authorities") String authorities,
                                                  @PathVariable String patientId){
        if (authorities.contains("RESCUE_ADMIN")) {
            patientService.deletePatient(patientId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Patient deleted successfully")
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
    @GetMapping("/")
    public ResponseEntity<Response> getAllPatient(@RequestHeader("authorities") String authorities,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.PATIENT_SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_ADMIN")) {
            PageResponse<PatientDto> pageResponse = patientService.getAllPatients(pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Patient are successfully get")
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
    @GetMapping("/{patientId}")
    public ResponseEntity<Response> getPatientById(@RequestHeader("authorities") String authorities,
                                                   @RequestHeader("userEmail") String userEmail,
                                                   @PathVariable String patientId){
            PatientDto patientDto=patientService.getById(authorities,userEmail,patientId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Patient with id "+patientId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(patientDto)
                    .build(),OK);
    }

    @GetMapping("/own-record")
    public ResponseEntity<Response> getPatientByEmail(@RequestHeader("authorities") String authorities,
                                                   @RequestHeader("userEmail") String userEmail){
        PatientDto patientDto=patientService.getByEmail(authorities,userEmail);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Patient with email "+userEmail+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(patientDto)
                .build(),OK);
    }
}
