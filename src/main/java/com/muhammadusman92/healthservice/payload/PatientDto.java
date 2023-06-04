package com.muhammadusman92.healthservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class PatientDto {
    private String CNIC;
    private String name;
    private String email;
    private String bloodGroup;
    private int age;
    private float height;
    private float weight;
    private String gender;
    private LocationDto location;
    private Set<DiseaseDto> diseaseSet=new HashSet<>();
    private Set<DoctorDto> doctorSet = new HashSet<>();
    private Set<HospitalDto> hospitalSet = new HashSet<>();
}
