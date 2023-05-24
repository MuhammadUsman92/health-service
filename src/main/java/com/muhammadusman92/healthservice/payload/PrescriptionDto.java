package com.muhammadusman92.healthservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PrescriptionDto {
    private Integer id;
    private Date date;
    private boolean recover;
    private String comments;
    private DiseaseDto disease;
    private Set<ReportDto> reportSet;
    private DoctorDto doctor;
    private HospitalDto hospital;
    private Set<MedicineDto> medicineSet = new HashSet<>();
}
