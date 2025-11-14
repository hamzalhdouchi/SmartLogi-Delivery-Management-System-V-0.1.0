package com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoidsParLivreurDetailDTO {
    private String livreurId;
    private String livreurNomComplet;
    private String livreurTelephone;
    private String zoneAssignee;
    private BigDecimal poidsTotal;  // ← Changé de Double à BigDecimal
    private Integer nombreColis;
    private BigDecimal poidsMoyenParColis;  // ← Changé de Double à BigDecimal

    // Constructeur SÉCURISÉ avec BigDecimal
    public PoidsParLivreurDetailDTO(Object[] result) {
        try {
            // Index 0: String (livreurId)
            this.livreurId = safeCastToString(result[0]);

            // Index 1: String (nom complet)
            this.livreurNomComplet = safeCastToString(result[1]);

            // Index 2: String (téléphone)
            this.livreurTelephone = safeCastToString(result[2]);

            // Index 3: String (zone)
            this.zoneAssignee = safeCastToString(result[3]);

            // Index 4: BigDecimal (poids total)
            this.poidsTotal = safeCastToBigDecimal(result[4]);

            // Index 5: Long → Integer (nombre colis)
            this.nombreColis = safeCastToInteger(result[5]);

            // Index 6: BigDecimal (poids moyen)
            this.poidsMoyenParColis = safeCastToBigDecimal(result[6]);

        } catch (Exception e) {
            throw new RuntimeException("Erreur de mapping DTO: " + e.getMessage(), e);
        }
    }

    // Méthodes de casting sécurisées
    private String safeCastToString(Object obj) {
        if (obj == null) return null;
        return obj.toString();
    }

    private BigDecimal safeCastToBigDecimal(Object obj) {
        if (obj == null) return BigDecimal.ZERO;

        if (obj instanceof BigDecimal) return (BigDecimal) obj;
        if (obj instanceof Double) return BigDecimal.valueOf((Double) obj);
        if (obj instanceof Float) return BigDecimal.valueOf((Float) obj);
        if (obj instanceof Long) return BigDecimal.valueOf((Long) obj);
        if (obj instanceof Integer) return BigDecimal.valueOf((Integer) obj);
        if (obj instanceof String) {
            try {
                return new BigDecimal((String) obj);
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        throw new ClassCastException("Impossible de caster " + obj.getClass() + " en BigDecimal");
    }

    private Integer safeCastToInteger(Object obj) {
        if (obj == null) return 0;

        if (obj instanceof Integer integer) return integer;

        if (obj instanceof Long longValue) return longValue.intValue();

        if (obj instanceof String string) {
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;  // Default case for other types
    }


    // Getters formatés
    public String getPoidsTotalFormatted() {
        return poidsTotal != null ?
                String.format("%.2f kg", poidsTotal) : "0.00 kg";
    }

    public String getPoidsMoyenFormatted() {
        return poidsMoyenParColis != null ?
                String.format("%.2f kg", poidsMoyenParColis) : "0.00 kg";
    }

    // Méthodes utilitaires pour les calculs
    public BigDecimal getPoidsTotal() {
        return poidsTotal != null ? poidsTotal : BigDecimal.ZERO;
    }

    public BigDecimal getPoidsMoyenParColis() {
        return poidsMoyenParColis != null ? poidsMoyenParColis : BigDecimal.ZERO;
    }

    // Méthode pour vérifier si le poids dépasse un seuil
    public boolean depasseSeuil(BigDecimal seuil) {
        return getPoidsTotal().compareTo(seuil) > 0;
    }

    // Méthode pour obtenir le pourcentage d'utilisation
    public BigDecimal getPourcentageUtilisation(BigDecimal capaciteMax) {
        if (capaciteMax.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getPoidsTotal()
                .divide(capaciteMax, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }
}