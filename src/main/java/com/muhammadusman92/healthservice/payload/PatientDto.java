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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientDto {
    private String CNIC;
    private String Name;
    private int Age;
    private float height;
    private float weight;
    private String gender;
    private Set<DiseaseDto> diseaseSet=new HashSet<>();
    private Set<DoctorDto> doctorSet = new HashSet<>();
    private Set<HospitalDto> hospitalSet = new HashSet<>();
}
