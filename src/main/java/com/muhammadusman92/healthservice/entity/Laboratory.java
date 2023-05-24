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
@SQLDelete(sql = "UPDATE laboratory SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Laboratory {
    @Id
    @Column(name = "reg_no",unique = true,nullable = false,length = 50)
    private String reg_no;
    @Column(length = 150)
    private String name;
    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @OneToMany(mappedBy = "prescription",fetch = FetchType.LAZY)
    private Set<Report> reportSet=new HashSet<>();
    private boolean deleted = Boolean.FALSE;
}
