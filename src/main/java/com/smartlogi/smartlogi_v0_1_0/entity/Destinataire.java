package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "destinataire")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Destinataire {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    private String nom;

    private String prenom;

    @Column(unique = true)
    private String email;

    private String telephone;

    private String adresse;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Colis> colis = new ArrayList<>();
}