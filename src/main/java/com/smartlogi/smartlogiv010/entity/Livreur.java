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
@NoArgsConstructor
public class Livreur extends  User{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String vehicule;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "livreur", cascade = CascadeType.ALL)
    private List<Colis> colisAssignes = new ArrayList<>();

}