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
@SQLDelete(sql = "UPDATE hospital SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Hospital {
    @Id
    @Column(name = "reg_no",unique = true,nullable = false,length = 50)
    private String reg_no;
    @Column(length = 150)
    private String name;
    @Column(length = 500)
    private String about;
    private boolean emergency_unit;
    @OneToMany(mappedBy = "hospital",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Prescription> prescriptionSet=new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "hospitalSet")
    private Set<Doctor> doctorSet = new HashSet<>();
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "patient_have_hospital", joinColumns = { @JoinColumn(name = "hospital_id") }, inverseJoinColumns = { @JoinColumn(name = "patient_id") })
    private Set<Patient> patients = new HashSet<>();
    @OneToMany(mappedBy = "hospital",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<HospitalUser> hospitalUserSet=new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
