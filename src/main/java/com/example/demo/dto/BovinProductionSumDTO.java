package com.example.demo.dto;

import java.time.LocalDate;

public class BovinProductionSumDTO {
    private int bovinId;
    private String numeroBoucle;
    private double totalQuantite;

    // Constructeur complet indispensable pour le "SELECT new" dans la requête JPQL
    public BovinProductionSumDTO(int bovinId, String numeroBoucle, double totalQuantite) {
        this.bovinId = bovinId;
        this.numeroBoucle = numeroBoucle;
        this.totalQuantite = totalQuantite;
    }

    // Getters et Setters
    public int getBovinId() { return bovinId; }
    public void setBovinId(int bovinId) { this.bovinId = bovinId; }

    public String getNumeroBoucle() { return numeroBoucle; }
    public void setNumeroBoucle(String numeroBoucle) { this.numeroBoucle = numeroBoucle; }

    public double getTotalQuantite() { return totalQuantite; }
    public void setTotalQuantite(double totalQuantite) { this.totalQuantite = totalQuantite; }
}