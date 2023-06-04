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
public class DiseaseDto {
    private Integer id;
    private String name;
    private String stage;
    private PatientDto patientDto;
    private Set<PrescriptionDto> prescriptionDtoSet =new HashSet<>();
}
