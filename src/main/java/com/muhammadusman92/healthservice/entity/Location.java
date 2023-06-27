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
@SQLDelete(sql = "UPDATE location SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 100)
    private String street;
    @Column(length = 80)
    private String city;
    @Column(length = 20)
    private String postal_code;
    @Column(length = 80)
    private String Country;
    private double latitude;
    private double longitude;
    private boolean deleted = Boolean.FALSE;
    @OneToOne(mappedBy = "location")
    private Patient patient;
    @OneToOne(mappedBy = "location")
    private Hospital hospital;
    @OneToOne(mappedBy = "location")
    private Laboratory laboratory;
}
