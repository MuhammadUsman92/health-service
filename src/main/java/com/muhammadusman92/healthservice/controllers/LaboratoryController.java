package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.config.AppConstants;
import com.muhammadusman92.healthservice.payload.LaboratoryDto;
import com.muhammadusman92.healthservice.payload.PageResponse;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.LaboratoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {
    @Autowired
    private LaboratoryService laboratoryService;
    @PostMapping("/")
    public ResponseEntity<Response> createLaboratory(
//            @RequestHeader("authorities") String authorities,
                                                     @RequestBody LaboratoryDto laboratoryDto){
//        if (authorities.contains("RESCUE_ADMIN")) {
            LaboratoryDto savedLaboratory = laboratoryService.createLaboratory(laboratoryDto);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Laboratory is successfully created")
                    .status(CREATED)
                    .statusCode(CREATED.value())
                    .data(savedLaboratory)
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
    @PutMapping("/{laboratoryId}")
    public ResponseEntity<Response> updateLaboratory(@RequestHeader("authorities") String authorities,
                                                     @RequestBody LaboratoryDto laboratoryDto,@PathVariable String laboratoryId){
        if (authorities.contains("RESCUE_ADMIN")) {
            LaboratoryDto updateLaboratory = laboratoryService.updateLaboratory(laboratoryDto,laboratoryId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Laboratory is successfully updated")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(updateLaboratory)
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
    @DeleteMapping("/{laboratoryId}")
    public ResponseEntity<Response> deleteLaboratory(@RequestHeader("authorities") String authorities,
                                                     @PathVariable String laboratoryId){
        if (authorities.contains("RESCUE_ADMIN")) {
            laboratoryService.deleteLaboratory(laboratoryId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Laboratory deleted successfully")
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
    public ResponseEntity<Response> getAllLaboratories(@RequestHeader("authorities") String authorities,
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        if (authorities.contains("RESCUE_USER")) {
            PageResponse<LaboratoryDto> pageResponse = laboratoryService.getAllLaboratories(pageNumber,pageSize,sortBy,sortDir);
            return new  ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("All Laboratory are successfully get")
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
    @GetMapping("/{laboratoryId}")
    public ResponseEntity<Response> getLaboratoryById(@RequestHeader("authorities") String authorities,
                                                      @PathVariable String laboratoryId){
        if (authorities.contains("RESCUE_USER")) {
            LaboratoryDto laboratoryDto=laboratoryService.getById(laboratoryId);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Laboratory with id "+laboratoryId+" are successfully get")
                    .status(OK)
                    .statusCode(OK.value())
                    .data(laboratoryDto)
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
