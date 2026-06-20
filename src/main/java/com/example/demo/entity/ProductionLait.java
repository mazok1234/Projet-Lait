package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "productionlait")
public class ProductionLait {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cyclebovinid", nullable = false)
    private CycleBovin cycleBovin;

    private double quantite;

    @Column(name = "dateproduction", nullable = false)
    private LocalDateTime dateProduction;

    @Column(name = "iduser")
    private Integer idUser;

    public ProductionLait() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public CycleBovin getCycleBovin() { return cycleBovin; }
    public void setCycleBovin(CycleBovin cycleBovin) { this.cycleBovin = cycleBovin; }

    public double getQuantite() { return quantite; }
    public void setQuantite(double quantite) { this.quantite = quantite; }

    public LocalDateTime getDateProduction() { return dateProduction; }
    public void setDateProduction(LocalDateTime dateProduction) { this.dateProduction = dateProduction; }

    public Integer getIdUser() { return idUser; }
    public void setIdUser(Integer idUser) { this.idUser = idUser; }
}