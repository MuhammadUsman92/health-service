package com.muhammadusman92.healthservice.controllers;

import com.muhammadusman92.healthservice.payload.PatientDto;
import com.muhammadusman92.healthservice.payload.Response;
import com.muhammadusman92.healthservice.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationService locationService;
    @GetMapping("/latitude/{latitude}/longitude/{longitude}/radius/{radius}")
    public ResponseEntity<Response> getAllPatientsWithInLocation(@RequestHeader("authorities") String authorities,
                                                  @PathVariable double latitude,
                                                  @PathVariable double longitude,
                                                  @PathVariable double radius){
        if (authorities.contains("RESCUE_ADMIN")) {
            List<PatientDto> allDiseasesWithInLocation = locationService.getAllPatientsWithInLocation(latitude, longitude, radius);
            return new ResponseEntity<>(Response.builder()
                    .timeStamp(now())
                    .message("Get All Patients With in radius")
                    .status(OK)
                    .data(allDiseasesWithInLocation)
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

}
