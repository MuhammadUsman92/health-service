package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.MedicineDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/medicine")
public class MedicineController {
    @Autowired
    private MedicineService medicineService;
    @PostMapping("/prescriptionId/{prescriptionId}")
    public ResponseEntity<Response> createMedicineOfPatientById(
            @RequestHeader("authorities") String authorities,
            @RequestHeader("userEmail") String userEmail,
            @RequestBody MedicineDto medicineDto,
            @PathVariable Integer prescriptionId){
        if (authorities.contains("HOSPITAL_ADMIN")) {
            MedicineDto savedMedicine = medicineService.createMedicine(userEmail,prescriptionId,medicineDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Medicine is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedMedicine)
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
    @PutMapping("/{medicineId}")
    public ResponseEntity<Response> updateMedicine(@RequestHeader("authorities") String authorities,
                                                   @RequestBody MedicineDto medicineDto,@PathVariable Integer medicineId){
        if (authorities.contains("RESCUE_ADMIN")) {
            MedicineDto updateMedicine = medicineService.updateMedicine(medicineDto,medicineId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Medicine is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updateMedicine)
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
    @DeleteMapping("/{medicineId}")
    public ResponseEntity<Response> deleteMedicine(
            @RequestHeader("authorities") String authorities,
            @PathVariable Integer medicineId){
        if (authorities.contains("RESCUE_ADMIN")) {
            medicineService.deleteMedicine(medicineId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Medicine deleted successfully")
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
    @GetMapping("/prescriptionId/{prescriptionId}")
    public ResponseEntity<Response> getAllMedicineOfPrescription(
            @RequestHeader("authorities") String authorities,
            @RequestHeader("userEmail") String userEmail,
            @PathVariable Integer prescriptionId,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
            PageResponse<MedicineDto> pageResponse = medicineService.getAllMedicines(authorities,userEmail,prescriptionId,pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Medicine are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(pageResponse)
                    .build(),OK);
    }
    @GetMapping("/{medicineId}")
    public ResponseEntity<Response> getMedicineById(@RequestHeader("authorities") String authorities,
                                                    @PathVariable Integer medicineId){
        if (authorities.contains("RESCUE_USER")) {
            MedicineDto medicineDto=medicineService.getById(medicineId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Medicine with id "+medicineId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(medicineDto)
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
