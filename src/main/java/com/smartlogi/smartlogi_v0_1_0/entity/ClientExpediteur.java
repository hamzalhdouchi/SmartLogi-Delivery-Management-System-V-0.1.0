package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_expediteur")
@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class ClientExpediteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String nom;

    @NotBlank
    @Size(max = 100)
    private String prenom;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(max = 20)
    private String telephone;

    @NotBlank
    @Size(max = 255)
    private String adresse;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "clientExpediteur", cascade = CascadeType.ALL)
    private List<Colis> colis = new ArrayList<>();
}