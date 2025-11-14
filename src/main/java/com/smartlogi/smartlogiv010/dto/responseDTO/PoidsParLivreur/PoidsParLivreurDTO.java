package com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur;

import com.smartlogi.smartlogiv010.entity.Livreur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoidsParLivreurDTO {
    private Livreur livreur;
    private BigDecimal poidsTotal;

    public PoidsParLivreurDTO(Object[] result) {
        try {
            this.livreur = (Livreur) result[0];

            if (result[1] instanceof BigDecimal) {
                this.poidsTotal = ((BigDecimal) result[1]).setScale(2, RoundingMode.HALF_UP);
            } else if (result[1] instanceof Double) {
                this.poidsTotal = BigDecimal.valueOf((Double) result[1]).setScale(2, RoundingMode.HALF_UP);
            } else if (result[1] instanceof Integer) {
                this.poidsTotal = new BigDecimal((Integer) result[1]).setScale(2, RoundingMode.HALF_UP);
            } else {
                this.poidsTotal = BigDecimal.ZERO;
            }

        } catch (Exception e) {
            this.livreur = null;
            this.poidsTotal = BigDecimal.ZERO;
        }
    }

    public String getPoidsTotalFormatted() {
        return String.format("%s kg", getPoidsTotal().toString());
    }

    public BigDecimal getPoidsTotal() {
        return poidsTotal != null ? poidsTotal : BigDecimal.ZERO;
    }

    public boolean depasseSeuil(BigDecimal seuil) {
        return getPoidsTotal().compareTo(seuil != null ? seuil : BigDecimal.ZERO) > 0;
    }

    public boolean depasseSeuil(Double seuil) {
        return depasseSeuil(seuil != null ? BigDecimal.valueOf(seuil) : BigDecimal.ZERO);
    }

    public String getPourcentageUtilisationFormatted(BigDecimal capaciteMax) {
        BigDecimal pourcentage = getPourcentageUtilisation(capaciteMax);
        return String.format("%.1f%%", pourcentage);
    }

    public BigDecimal getPourcentageUtilisation(BigDecimal capaciteMax) {
        if (capaciteMax == null || capaciteMax.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return getPoidsTotal()
                .divide(capaciteMax, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(1, RoundingMode.HALF_UP);
    }
}