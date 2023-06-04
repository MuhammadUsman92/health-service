package com.muhammadusman92.healthservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class ReportDto {
    private Integer id;
    private Date collect_date;
    private Date result_date;
    private String report_image;
    private PrescriptionDto prescription;
    private LaboratoryDto laboratoryDto;
}
