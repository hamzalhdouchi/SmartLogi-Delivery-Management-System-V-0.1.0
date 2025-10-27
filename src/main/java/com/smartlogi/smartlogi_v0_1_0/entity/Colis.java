package com.smartlogi.smartlogi_v0_1_0.entity;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String description;

    @DecimalMin("0.1")
    @Column(precision = 10, scale = 2)
    private BigDecimal poids;

    @Enumerated(EnumType.STRING)
    private StatutColis statut = StatutColis.CREE;

    @Enumerated(EnumType.STRING)
    private Priorite priorite = Priorite.NORMALE;


    @NotBlank
    @Size(max = 100)
    private String villeDestination;

    @ManyToOne
    @JoinColumn(name = "client_expediteur_id")
    private ClientExpediteur clientExpediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private Destinataire destinataire;

    @ManyToOne
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL)
    private List<HistoriqueLivraison> historique = new ArrayList<>();

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL)
    private List<ColisProduit> produits = new ArrayList<>();
}

