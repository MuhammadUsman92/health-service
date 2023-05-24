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
@SQLDelete(sql = "UPDATE disease SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 80)
    private String name;
    @Column(length = 50)
    private String stage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_cnic") // Specify the foreign key column name
    private Patient patient;
    @OneToMany(mappedBy = "disease",fetch = FetchType.LAZY)
    private Set<Prescription> prescriptionSet=new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
