package com.smartlogi.smartlogi_v0_1_0.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    // Exceptions métier spécifiques
    public static BusinessException colisDejaLivre(String colisId) {
        return new BusinessException(String.format("Le colis avec ID %d est déjà livré", colisId));
    }

    public static BusinessException statutInvalide(String statutActuel, String statutDemande) {
        return new BusinessException(String.format(
                "Changement de statut invalide : %s → %s", statutActuel, statutDemande));
    }

    public static BusinessException livreurNonDisponible(String livreurId) {
        return new BusinessException(String.format(
                "Le livreur avec ID %d n'est pas disponible", livreurId));
    }

    public static BusinessException poidsDepasse(Double poidsActuel, Double poidsMax) {
        return new BusinessException(String.format(
                "Poids dépassé : %s kg (maximum : %s kg)", poidsActuel, poidsMax));
    }
}