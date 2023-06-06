package com.muhammadusman92.healthservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE doctor SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Doctor {
    @Id
    @Column(name = "pmdc",unique = true,nullable = false,length = 50)
    private String pmdc;
    @Column(length = 100)
    private String name;
    @Column(length = 50)
    private String specialization;
    @Column(length = 50)
    private String qualification;
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Gender gender;
    @Column(length = 500)
    private String about;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "patient_have_doctor", joinColumns = { @JoinColumn(name = "doctor_id") }, inverseJoinColumns = { @JoinColumn(name = "patient_id") })
    private Set<Patient> patientSet = new HashSet<>();
    @OneToMany(mappedBy = "doctor",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Prescription> prescriptionSet=new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "doctor_have_hospital", joinColumns = { @JoinColumn(name = "doctor_id") }, inverseJoinColumns = { @JoinColumn(name = "hospital_id") })
    private Set<Hospital> hospitalSet = new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
