package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisProduitId implements Serializable {
    private String colisId;
    private String produitId;

    // ✅ Important: Override equals() and hashCode()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColisProduitId that = (ColisProduitId) o;
        return Objects.equals(colisId, that.colisId) &&
                Objects.equals(produitId, that.produitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colisId, produitId);
    }
}