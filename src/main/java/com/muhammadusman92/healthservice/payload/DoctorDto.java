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
public class DoctorDto {
    private String pmdc;
    private String name;
    private String specialization;
    private String qualification;
    private String gender;
    private Set<PatientDto> patientDtoSet = new HashSet<>();
    private Set<PrescriptionDto> prescriptionDtoSet =new HashSet<>();
    private Set<HospitalDto> hospitalDtoSet = new HashSet<>();
}
