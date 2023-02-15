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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {
    @Autowired
    private LaboratoryService laboratoryService;
    @PostMapping("/")
    public ResponseEntity<Response> createLaboratory(@RequestBody LaboratoryDto laboratoryDto){
        LaboratoryDto savedLaboratory = laboratoryService.createLaboratory(laboratoryDto);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Laboratory is successfully created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .data(savedLaboratory)
                .build(), CREATED);
    }
    @PutMapping("/{laboratoryId}")
    public ResponseEntity<Response> updateLaboratory(@RequestBody LaboratoryDto laboratoryDto,@PathVariable Integer laboratoryId){
        LaboratoryDto updateLaboratory = laboratoryService.updateLaboratory(laboratoryDto,laboratoryId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Laboratory is successfully updated")
                .status(OK)
                .statusCode(OK.value())
                .data(updateLaboratory)
                .build(),OK);
    }
    @DeleteMapping("/{laboratoryId}")
    public ResponseEntity<Response> deleteLaboratory(@PathVariable Integer laboratoryId){
        laboratoryService.deleteLaboratory(laboratoryId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Laboratory deleted successfully")
                .status(OK)
                .statusCode(OK.value())
                .build(),OK);
    }
    @GetMapping("/")
    public ResponseEntity<Response> getAllLaboratories(
            @RequestParam(name = "pageNumber",defaultValue = AppConstants.PAGE_NUMBER,required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = AppConstants.PAGE_SIZE,required = false) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = AppConstants.SORT_BY,required = false)String sortBy,
            @RequestParam(name = "sortDir",defaultValue = AppConstants.SORT_DIR,required = false)String sortDir){
        PageResponse<LaboratoryDto> pageResponse = laboratoryService.getAllLaboratories(pageNumber,pageSize,sortBy,sortDir);
        return new  ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("All Laboratory are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(pageResponse)
                .build(),OK);
    }
    @GetMapping("/{laboratoryId}")
    public ResponseEntity<Response> getLaboratoryById(@PathVariable Integer laboratoryId){
        LaboratoryDto laboratoryDto=laboratoryService.getById(laboratoryId);
        return new ResponseEntity<>(Response.builder()
                .timeStamp(now())
                .message("Laboratory with id "+laboratoryId+" are successfully get")
                .status(OK)
                .statusCode(OK.value())
                .data(laboratoryDto)
                .build(),OK);
    }
}
