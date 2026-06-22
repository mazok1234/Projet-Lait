package com.example.demo.dto;

import java.time.LocalDate;

public class BovinCreateForm {
    private String numeroBoucle;
    private Integer raceId;
    private Integer etatSanteId;
    private LocalDate dateNaissance;
    private String sexe;

    public String getNumeroBoucle() {
        return numeroBoucle;
    }

    public void setNumeroBoucle(String numeroBoucle) {
        this.numeroBoucle = numeroBoucle;
    }

    public Integer getRaceId() {
        return raceId;
    }

    public void setRaceId(Integer raceId) {
        this.raceId = raceId;
    }

    public Integer getEtatSanteId() {
        return etatSanteId;
    }

    public void setEtatSanteId(Integer etatSanteId) {
        this.etatSanteId = etatSanteId;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }
}
