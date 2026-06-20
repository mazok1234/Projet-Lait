package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="historiquesalaire")
public class HistoriqueSalaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="employeid")
    private Employe employe;

    @Column(name="salairebase")
    private Double salaireBase;

    @Column(name="datedebut")
    private LocalDate dateDebut;

    @Column(name="datefin")
    private LocalDate dateFin;

    @Column(name="motif")
    private String motif;

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

public Double getSalaireBase() {
    return salaireBase;
}

public void setSalaireBase( Double salaireBase) {
    this.salaireBase = salaireBase;
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

public String getMotif() {
    return motif;
}

public void setMotif(String motif) {
    this.motif = motif;
}

}