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
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;
    @PostMapping("/diseaseId/{diseaseId}/doctorId/{doctorId}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Response> createPrescription(
//            @RequestHeader("authorities") String authorities,
                                                       @RequestHeader("userEmail") String email,
                                                        @RequestBody PrescriptionDto prescriptionDto,
                                                        @PathVariable Integer diseaseId,
                                                        @PathVariable String doctorId){
//        if (authorities.contains("RESCUE_USER")) {
            PrescriptionDto savedPrescription = prescriptionService.createPrescription(prescriptionDto,diseaseId,doctorId,email);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Prescription is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedPrescription)
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
    @PutMapping("/{prescriptionId}")
    public ResponseEntity<Response> updatePrescription(@RequestBody PrescriptionDto prescriptionDto,
                                                       @RequestHeader("authorities") String authorities,
                                                       @PathVariable Integer prescriptionId){
        if (authorities.contains("RESCUE_USER")) {
            PrescriptionDto updatePrescription = prescriptionService.updatePrescription(prescriptionDto,prescriptionId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Prescription is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updatePrescription)
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
    @DeleteMapping("/{prescriptionId}")
    public ResponseEntity<Response> deletePrescription(@RequestHeader("authorities") String authorities,
                                                       @PathVariable Integer prescriptionId){
        if (authorities.contains("RESCUE_USER")) {
            prescriptionService.deletePrescription(prescriptionId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Prescription deleted successfully")
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
    @GetMapping("/diseaseId/{diseaseId}")
    public ResponseEntity<Response> getAllPrescriptionOfDisease(@RequestHeader("authorities") String authorities,
            @PathVariable Integer diseaseId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_USER")) {
            PageResponse<PrescriptionDto> pageResponse = prescriptionService.getAllPrescriptions(diseaseId,pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Prescription are successfully get")
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
    @GetMapping("/{prescriptionId}")
    public ResponseEntity<Response> getPrescriptionById(@RequestHeader("authorities") String authorities,
                                                        @PathVariable Integer prescriptionId){
        if (authorities.contains("RESCUE_USER")) {
            PrescriptionDto prescriptionDto=prescriptionService.getById(prescriptionId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Prescription with id "+prescriptionId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(prescriptionDto)
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
