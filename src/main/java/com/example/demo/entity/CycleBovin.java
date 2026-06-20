package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "cyclebovins")
public class CycleBovin {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "datedebut", nullable = false) 
    private LocalDate dateDebut;

    @Column(name = "datefin") 
    private LocalDate dateFin;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "bovinid", nullable = false)
    private Bovin bovin;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "etatcycleid", nullable = false)
    private CycleEtatVache etatCycle;

    @Column(name = "raisonfin")
    private String raisonFin;

    @OneToMany(mappedBy = "cycleBovin")
    @JsonIgnore
    private List<ProductionLait> productions;

    public CycleBovin() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Bovin getBovin() { return bovin; }
    public void setBovin(Bovin bovin) { this.bovin = bovin; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public CycleEtatVache getEtatCycle() { return etatCycle; }
    public void setEtatCycle(CycleEtatVache etatCycle) { this.etatCycle = etatCycle; }

    public String getRaisonFin() { return raisonFin; }
    public void setRaisonFin(String raisonFin) { this.raisonFin = raisonFin; }

    public List<ProductionLait> getProductions() { return productions; }
    public void setProductions(List<ProductionLait> productions) { this.productions = productions; }
}