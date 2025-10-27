package com.smartlogi.smartlogi_v0_1_0.dto;

import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;

import java.time.LocalDateTime;

public class HistoriqueLivraisonDto {
    private Long id;
    private Long colisId;
    private StatutColis statut;
    private LocalDateTime dateChangement;
    private String commentaire;
    private LocalDateTime dateCreation;

    // Constructeurs, Getters et Setters
    public HistoriqueLivraisonDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getColisId() { return colisId; }
    public void setColisId(Long colisId) { this.colisId = colisId; }

    public StatutColis getStatut() { return statut; }
    public void setStatut(StatutColis statut) { this.statut = statut; }

    public LocalDateTime getDateChangement() { return dateChangement; }
    public void setDateChangement(LocalDateTime dateChangement) { this.dateChangement = dateChangement; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}