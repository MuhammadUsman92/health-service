package com.muhammadusman92.healthservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE report SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date collect_date;
    private Date result_date;
    private String report_image;
    @ManyToOne(fetch = FetchType.LAZY)
    private Prescription prescription;
    @ManyToOne(fetch = FetchType.LAZY)
    private Laboratory laboratory;
    private boolean deleted = Boolean.FALSE;
}
