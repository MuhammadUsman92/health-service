package com.muhammadusman92.healthservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE prescription SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date date;
    private boolean recover;
    @Column(length = 500)
    private String comments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disease_id") // Specify the foreign key column name
    private Disease disease;
    @OneToMany(mappedBy = "prescription",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Report> reportSet=new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.LAZY)
    private Hospital hospital;
    @OneToMany(mappedBy = "prescription",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<Medicine> medicineSet=new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
