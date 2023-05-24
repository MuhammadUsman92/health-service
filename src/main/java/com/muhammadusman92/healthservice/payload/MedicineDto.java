package com.muhammadusman92.healthservice.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MedicineDto {
    private Integer id;
    private String name;
    private String type;
    private String quantity;
    private String timing;
    private String duration;
    private PrescriptionDto prescriptionDto;
}
