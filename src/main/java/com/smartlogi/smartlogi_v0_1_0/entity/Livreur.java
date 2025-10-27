package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(max = 100)
    private String prenom;

    @NotBlank
    @Size(max = 20)
    private String telephone;

    @NotBlank
    @Size(max = 50)
    private String vehicule;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @OneToMany(mappedBy = "livreur")
    private List<Colis> colisAssignes = new ArrayList<>();
}