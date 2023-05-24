package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.*;
import com.muhammadusman92.healthservice.payload.DoctorDto;
import com.muhammadusman92.healthservice.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("/")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Response> createDoctor(
//            @RequestHeader("authorities") String authorities,
                                                 @RequestBody DoctorDto doctorDto){
//        if (authorities.contains("RESCUE_ADMIN")) {
            DoctorDto doctorDtoCreated = doctorService.createDoctor(doctorDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Doctor is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(doctorDtoCreated)
                    .build(), CREATED);
//        } else {
//            return new ResponseEntity<>(Response.builder()
//                    .timeStamp(now())
//                    .message("You are not authorized for this service")
//                    .status(FORBIDDEN)
//                    .statusCode(FORBIDDEN.value())
//                    .build(), FORBIDDEN);
//        }
    }
    @PutMapping("/{doctorId}")
    public ResponseEntity<Response> updateDoctor(@RequestHeader("authorities") String authorities,
                                                 @RequestBody DoctorDto doctorDto,@PathVariable String doctorId){
        if (authorities.contains("RESCUE_ADMIN")) {
            DoctorDto doctorDtoUpdate = doctorService.updateDoctor(doctorDto, doctorId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Doctor is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(doctorDtoUpdate)
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
    @DeleteMapping("/{doctorId}")
    public ResponseEntity<Response> deleteDoctor(@RequestHeader("authorities") String authorities,
                                                 @PathVariable String doctorId){
        if (authorities.contains("RESCUE_ADMIN")) {
            doctorService.deleteDoctor(doctorId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Doctor deleted successfully")
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
    public ResponseEntity<Response> getAllDoctors(@RequestHeader("authorities") String authorities,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_ADMIN")) {
            PageResponse<DoctorDto> pageResponse = doctorService.getAllDoctors(pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Doctor are successfully get")
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
    @GetMapping("/{doctorId}")
    public ResponseEntity<Response> getDoctorById(@RequestHeader("authorities") String authorities,
                                                  @PathVariable String doctorId){
        if (authorities.contains("RESCUE_USER")) {
            DoctorDto doctorDto=doctorService.getById(doctorId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Doctor with id "+doctorId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(doctorDto)
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
}
