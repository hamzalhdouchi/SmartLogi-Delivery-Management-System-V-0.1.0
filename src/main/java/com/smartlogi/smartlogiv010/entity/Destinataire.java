package com.smartlogi.smartlogiv010.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinataire")
@Getter
@Setter
@AllArgsConstructor
@DiscriminatorValue(value = "destinataire")
@NoArgsConstructor
public class Destinataire extends User{


    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    private List<Colis> colis = new ArrayList<>();
}