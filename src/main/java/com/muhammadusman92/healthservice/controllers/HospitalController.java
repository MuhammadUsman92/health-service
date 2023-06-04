package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.HospitalDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.HospitalService;
import com.muhammadusman92.healthservice.services.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalUserService hospitalUserService;
    @PostMapping("/")
    public ResponseEntity<Response> createHospital(
//            @RequestHeader("authorities") String authorities,
                                                   @RequestBody HospitalDto hospitalDto){
//        if (authorities.contains("RESCUE_ADMIN")) {
            HospitalDto savedHospital = hospitalService.createHospital(hospitalDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Hospital is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedHospital)
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
    @PostMapping("/hospitalId/{hospitalId}/user-email/{userEmail}")
    public ResponseEntity<String> addHospitalUser(
            @PathVariable String hospitalId,
            @PathVariable String userEmail){
        return new ResponseEntity<>(hospitalUserService.addHospitalUser(hospitalId,userEmail),OK);
    }
    @PutMapping("/{hospitalId}")
    public ResponseEntity<Response> updateHospital(@RequestHeader("authorities") String authorities,
                                                   @RequestBody HospitalDto hospitalDto,@PathVariable String hospitalId){
        if (authorities.contains("RESCUE_ADMIN")) {
            HospitalDto updateHospital = hospitalService.updateHospital(hospitalDto,hospitalId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Hospital is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updateHospital)
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
    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Response> deleteHospital(@RequestHeader("authorities") String authorities,
                                                   @PathVariable String hospitalId){
        if (authorities.contains("RESCUE_ADMIN")) {
            hospitalService.deleteHospital(hospitalId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Hospital deleted successfully")
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
    public ResponseEntity<Response> getAllHospitals(
            @RequestHeader("authorities") String authorities,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_USER")) {
            PageResponse<HospitalDto> pageResponse = hospitalService.getAllHospitals(pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Hospital are successfully get")
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
    @GetMapping("/{hospitalId}")
    public ResponseEntity<Response> getHospitalById(@RequestHeader("authorities") String authorities,
                                                    @PathVariable String hospitalId){
        if (authorities.contains("RESCUE_USER")) {
            HospitalDto hospitalDto=hospitalService.getById(hospitalId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Hospital with id "+hospitalId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(hospitalDto)
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
