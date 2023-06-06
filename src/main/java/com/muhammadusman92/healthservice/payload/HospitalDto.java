package com.muhammadusman92.healthservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class HospitalDto {
    private String name;
    private String reg_no;
    private String about;
    private boolean emergency_unit;
    private LocationDto location;
    private Set<PrescriptionDto> prescriptionSet =new HashSet<>();
    private Set<DoctorDto> doctorSet = new HashSet<>();
    private Set<PatientDto> patients = new HashSet<>();
}
