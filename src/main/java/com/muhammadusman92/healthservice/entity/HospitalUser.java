package com.muhammadusman92.healthservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE hospitalUser SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor

public class HospitalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_reg_no") // Specify the foreign key column name
    private Hospital hospital;
    private boolean deleted = Boolean.FALSE;
}
