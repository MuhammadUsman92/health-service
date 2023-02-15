package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/disease")
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;
    @PostMapping("/patientId/{patientId}")
    public ResponseEntity<Response> createDiseaseOfPatientById(@RequestBody DiseaseDto diseaseDto,
                                                               @PathVariable String patientId){
        DiseaseDto savedDisease = diseaseService.createDisease(patientId,diseaseDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Disease is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedDisease)
                .build(), CREATED);
    }
    @PutMapping("/{diseaseId}")
    public ResponseEntity<Response> updateDisease(@RequestBody DiseaseDto diseaseDto,@PathVariable Integer diseaseId){
        DiseaseDto updateDisease = diseaseService.updateDisease(diseaseDto,diseaseId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Disease is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updateDisease)
                .build(),OK);
    }
    @DeleteMapping("/{diseaseId}")
    public ResponseEntity<Response> deleteDisease(@PathVariable Integer diseaseId){
        diseaseService.deleteDisease(diseaseId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Disease deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/patientId/{patientId}")
    public ResponseEntity<Response> getAllDiseaseOfPatient(
            @PathVariable String patientId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<DiseaseDto> pageResponse = diseaseService.getAllDiseases(patientId,pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Disease are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{diseaseId}")
    public ResponseEntity<Response> getDiseaseById(@PathVariable Integer diseaseId){
        DiseaseDto diseaseDto=diseaseService.getById(diseaseId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Disease with id "+diseaseId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(diseaseDto)
                .build(),OK);
    }

}
