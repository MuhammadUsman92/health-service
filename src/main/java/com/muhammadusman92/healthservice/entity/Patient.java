package com.muhammadusman92.healthservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE patient SET deleted = true WHERE CNIC=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Patient {
    @Id
    @Column(name = "CNIC",unique = true,nullable = false,length = 50)
    private String CNIC;
    @Column(length = 100)
    private String Name;
    private String email;
    @Column(length = 10)
    private String bloodGroup;
    private int age;
    private float height;
    private float weight;
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private Gender gender;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "patient",fetch = FetchType.LAZY)
    private Set<Disease> diseaseSet=new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "patientSet")
    private Set<Doctor> doctorSet = new HashSet<>();
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "patients")
    private Set<Hospital> hospitalSet = new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
