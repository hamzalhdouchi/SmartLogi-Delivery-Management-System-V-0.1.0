package com.smartlogi.smartlogi_v0_1_0.entity;

import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_livraison")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @Enumerated(EnumType.STRING)
    private StatutColis statut;

    private LocalDateTime dateChangement;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Size(max = 500)
    private String commentaire;
}
