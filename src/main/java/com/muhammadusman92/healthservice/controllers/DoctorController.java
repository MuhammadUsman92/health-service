package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.*;
import com.muhammadusman92.healthservice.payload.DoctorDto;
import com.muhammadusman92.healthservice.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("/")
    public ResponseEntity<Response> createDoctor(@RequestBody DoctorDto doctorDto){
        DoctorDto doctorDtoCreated = doctorService.createDoctor(doctorDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Doctor is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(doctorDtoCreated)
                .build(), CREATED);
    }
    @PutMapping("/{doctorId}")
    public ResponseEntity<Response> updateDoctor(@RequestBody DoctorDto doctorDto,@PathVariable Integer doctorId){
        DoctorDto doctorDtoUpdate = doctorService.updateDoctor(doctorDto, doctorId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Doctor is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(doctorDtoUpdate)
                .build(),OK);
    }
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Response> deleteDoctor(@PathVariable Integer doctorId){
        doctorService.deleteDoctor(doctorId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Doctor deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/")
    public ResponseEntity<Response> getAllDoctors(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<DoctorDto> pageResponse = doctorService.getAllDoctors(pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Doctor are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{doctorId}")
    public ResponseEntity<Response> getDoctorById(@PathVariable Integer doctorId){
        DoctorDto doctorDto=doctorService.getById(doctorId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Doctor with id "+doctorId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(doctorDto)
                .build(),OK);
    }
}
