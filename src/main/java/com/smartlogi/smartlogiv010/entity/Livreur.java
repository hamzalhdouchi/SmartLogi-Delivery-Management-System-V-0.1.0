package com.smartlogi.smartlogiv010.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "livreur")
@Getter
@Setter
@AllArgsConstructor
@DiscriminatorValue(value = "livreur")
@NoArgsConstructor
public class Livreur extends  User{


    private String vehicule;

    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL)
    private List<Colis> colisAssignes = new ArrayList<>();

}