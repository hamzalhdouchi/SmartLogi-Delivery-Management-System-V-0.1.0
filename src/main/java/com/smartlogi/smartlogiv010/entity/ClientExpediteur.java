package com.smartlogi.smartlogiv010.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client_expediteur")
@Getter
@Setter
@AllArgsConstructor

@NoArgsConstructor
public class ClientExpediteur extends User{

    @OneToMany(mappedBy = "clientExpediteur", cascade = CascadeType.ALL)
    private List<Colis> colis = new ArrayList<>();
}