package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "zone")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String nom;


    @Column(name = "code_postal", unique = true, length = 5)
    private String codePostal;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Livreur> livreurs = new ArrayList<>();

    @OneToMany(mappedBy = "zone", cascade = CascadeType.ALL)
    private List<Colis> colis = new ArrayList<>();

}