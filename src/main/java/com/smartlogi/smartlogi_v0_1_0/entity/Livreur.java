package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "livreur")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Livreur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "Le nom du livreur est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
            message = "Le nom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String nom;

    @NotBlank(message = "Le prénom du livreur est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
            message = "Le prénom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String prenom;

    @NotBlank(message = "Le téléphone du livreur est obligatoire")
    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Le numéro de téléphone doit être un numéro marocain valide (ex: +212612345678 ou 0612345678)")
    private String telephone;

    @NotBlank(message = "Le type de véhicule est obligatoire")
    @Size(max = 50, message = "Le type de véhicule ne doit pas dépasser 50 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s\\-]+$",
            message = "Le type de véhicule contient des caractères non autorisés")
    private String vehicule;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @NotNull(message = "La zone assignée au livreur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL)
    private List<Colis> colisAssignes = new ArrayList<>();

}