package com.smartlogi.smartlogi_v0_1_0.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
class ColisProduitId implements Serializable {
    private String colisId;
    private String produitId;
}
