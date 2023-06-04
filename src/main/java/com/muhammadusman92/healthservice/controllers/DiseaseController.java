package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.entity.Disease;
import com.muhammadusman92.healthservice.payload.DiseaseDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.PatientDto;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("/disease")
public class DiseaseController {
    @Autowired
    private DiseaseService diseaseService;
    @PostMapping("/patientId/{patientId}")
    public ResponseEntity<Response> createDiseaseOfPatientById(
//            @RequestHeader("authorities") String authorities,
//                                                               @RequestHeader("userEmail") String email,
                                                               @RequestBody DiseaseDto diseaseDto,
                                                               @PathVariable String patientId){
//        if(
//                (diseaseDto.getPatientDto().getEmail().equalsIgnoreCase(email) && diseaseDto.getPatientDto().getCNIC().equalsIgnoreCase(patientId))
//                        || authorities.contains("RESCUE_USER")
//        ){
            DiseaseDto savedDisease = diseaseService.createDisease(patientId,diseaseDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Disease is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedDisease)
                    .build(), CREATED);
//        }
//        else  {
//            return new ResponseEntity<>(Response.builder()
//                    .timeStamp(now())
//                    .message("You are not authorized for this service")
//                    .status(FORBIDDEN)
//                    .statusCode(FORBIDDEN.value())
//                    .build(), FORBIDDEN);
//        }
    }
    @PutMapping("/{diseaseId}")
    public ResponseEntity<Response> updateDisease(@RequestHeader("authorities") String authorities,
                                                  @RequestBody DiseaseDto diseaseDto,@PathVariable Integer diseaseId){
        if (authorities.contains("RESCUE_ADMIN")) {
            DiseaseDto updateDisease = diseaseService.updateDisease(diseaseDto,diseaseId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Disease is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updateDisease)
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
    @DeleteMapping("/{diseaseId}")
    public ResponseEntity<Response> deleteDisease(@RequestHeader("authorities") String authorities,
                                                  @PathVariable Integer diseaseId){
       if (authorities.contains("RESCUE_ADMIN")) {
            diseaseService.deleteDisease(diseaseId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Disease deleted successfully")
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
    @GetMapping("/patientId/{patientId}")
    public ResponseEntity<Response> getAllDiseaseOfPatient(
//            @RequestHeader("authorities") String authorities,
//                                                           @RequestHeader("userEmail") String email,
            @PathVariable String patientId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<DiseaseDto> pageResponse = diseaseService.getAllDiseases(patientId,pageNumber,pageSize,sortBy,sortDir);
//        if(pageResponse.getContent().get(0).getPatientDto().getEmail().equalsIgnoreCase(email) || authorities.contains("RESCUE_USER")){
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Disease are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(pageResponse)
                    .build(),OK);
//        }
//        else {
//            return new ResponseEntity<>(Response.builder()
//                    .timeStamp(now())
//                    .message("You are not authorized for this service")
//                    .status(FORBIDDEN)
//                    .statusCode(FORBIDDEN.value())
//                    .build(), FORBIDDEN);
//        }
    }
    @GetMapping("/{diseaseId}")
    public ResponseEntity<Response> getDiseaseById(@RequestHeader("authorities") String authorities,
                                                   @RequestHeader("userEmail") String email,
                                                   @PathVariable Integer diseaseId){
        DiseaseDto diseaseDto=diseaseService.getById(diseaseId);
        if(diseaseDto.getPatientDto().getEmail().equalsIgnoreCase(email) || authorities.contains("RESCUE_USER")){
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Disease with id "+diseaseId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(diseaseDto)
                    .build(),OK);
        }else {
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("You are not authorized for this service")
                    .status(FORBIDDEN)
                    .statusCode(FORBIDDEN.value())
                    .build(), FORBIDDEN);
        }
    }

}
