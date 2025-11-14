    package com.smartlogi.smartlogiv010.entity;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.PastOrPresent;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.hibernate.annotations.CreationTimestamp;

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


        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("colisId")
        @JoinColumn(name = "colis_id")
        private Colis colis;

        @ManyToOne(fetch = FetchType.LAZY)
        @MapsId("produitId")
        @JoinColumn(name = "produit_id")
        private Produit produit;


        @Column(nullable = false)
        private Integer quantite;

        @Column(precision = 10, scale = 2, nullable = false)
        private BigDecimal prix;

        @CreationTimestamp
        @Column(name = "date_ajout", updatable = false)
        @PastOrPresent(message = "La date d'ajout ne peut pas être dans le futur")
        private LocalDateTime dateAjout;
    }