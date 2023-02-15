package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.HospitalDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    @PostMapping("/")
    public ResponseEntity<Response> createHospital(@RequestBody HospitalDto hospitalDto){
        HospitalDto savedHospital = hospitalService.createHospital(hospitalDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Hospital is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedHospital)
                .build(), CREATED);
    }
    @PutMapping("/{hospitalId}")
    public ResponseEntity<Response> updateHospital(@RequestBody HospitalDto hospitalDto,@PathVariable Integer hospitalId){
        HospitalDto updateHospital = hospitalService.updateHospital(hospitalDto,hospitalId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Hospital is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updateHospital)
                .build(),OK);
    }
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Response> deleteHospital(@PathVariable Integer hospitalId){
        hospitalService.deleteHospital(hospitalId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Hospital deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/")
    public ResponseEntity<Response> getAllHospitals(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<HospitalDto> pageResponse = hospitalService.getAllHospitals(pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Hospital are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{hospitalId}")
    public ResponseEntity<Response> getHospitalById(@PathVariable Integer hospitalId){
        HospitalDto hospitalDto=hospitalService.getById(hospitalId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Hospital with id "+hospitalId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(hospitalDto)
                .build(),OK);
    }
}
