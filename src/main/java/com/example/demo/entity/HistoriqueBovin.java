package com.example.demo.entity;

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
@Table(name = "historiquebovins")
public class HistoriqueBovin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bovinid", nullable = false)
    private Bovin bovin;

    @ManyToOne(optional = false)
    @JoinColumn(name = "etatsanteid", nullable = false)
    private EtatSante etatSante;

    @Column(name = "dateevent")
    private LocalDateTime dateEvent;

    @Column(name = "motif")
    private String motif;

    @Column(name = "iduser")
    private Integer idUser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bovin getBovin() {
        return bovin;
    }

    public void setBovin(Bovin bovin) {
        this.bovin = bovin;
    }

    public EtatSante getEtatSante() {
        return etatSante;
    }

    public void setEtatSante(EtatSante etatSante) {
        this.etatSante = etatSante;
    }

    public LocalDateTime getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
