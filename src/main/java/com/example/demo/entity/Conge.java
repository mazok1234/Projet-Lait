package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "conges")
public class Conge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employeid")
    private Employe employe;

    @Column(name = "datedebut")
    private LocalDate dateDebut;

    @Column(name = "datefin")
    private LocalDate dateFin;

    @ManyToOne
    @JoinColumn(name = "typeid")
    private TypeConge type;

    @ManyToOne
    @JoinColumn(name = "statutid")
    private StatutConge statut;

    @Column(name = "motif")
    private String motif;

    @ManyToOne
    @JoinColumn(name = "validepar")
    private Utilisateur validePar;

    @Column(name = "datedemande")
    private LocalDateTime dateDemande;

    @Column(name = "datevalidation")
    private LocalDateTime dateValidation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public TypeConge getType() {
        return type;
    }

    public void setType(TypeConge type) {
        this.type = type;
    }

    public StatutConge getStatut() {
        return statut;
    }

    public void setStatut(StatutConge statut) {
        this.statut = statut;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Utilisateur getValidePar() {
        return validePar;
    }

    public void setValidePar(Utilisateur validePar) {
        this.validePar = validePar;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }
}