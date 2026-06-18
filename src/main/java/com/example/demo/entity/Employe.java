package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;


@Entity
@Table(name="employes")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id")
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name="poste_id")
    private Poste poste;

    @Column(name="date_embauche")
    private LocalDateTime dateEmbauche;

    @Column(name="salaire_base")
    private Double salaireBase;

    @Column(name="telephone")
    private String telephone;

    @Column(name="is_actif")
    private Boolean isActif;





}