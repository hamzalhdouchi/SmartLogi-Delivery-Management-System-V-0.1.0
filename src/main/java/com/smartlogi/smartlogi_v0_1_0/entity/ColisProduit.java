package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "colis_produit")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColisProduit {
    @EmbeddedId
    private ColisProduitId id;

    @ManyToOne
    @MapsId("colisId")
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private Integer quantite;

    private BigDecimal prix;

    private LocalDateTime dateAjout;


}

