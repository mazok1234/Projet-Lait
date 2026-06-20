package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cycleetatvache")
public class CycleEtatVache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @OneToMany(mappedBy = "etatCycle")
    private List<CycleBovin> cycles;

    public CycleEtatVache() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public List<CycleBovin> getCycles() { return cycles; }
    public void setCycles(List<CycleBovin> cycles) { this.cycles = cycles; }
}   