package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "mouvementsstock")
public class MouvementsStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "typeid")
    private TypeMouvement type;

    @Column(name = "datemvt")
    private Timestamp dateMvt;

    @ManyToOne
    @JoinColumn(name = "fournisseurid")
    private Fournisseurs fournisseur;

    private String motif;

    @ManyToOne
    @JoinColumn(name = "iduser")
    private Utilisateur user;

    //Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TypeMouvement getType() {
        return type;
    }

    public void setType(TypeMouvement type) {
        this.type = type;
    }

    public Timestamp getDateMvt() {
        return dateMvt;
    }

    public void setDateMvt(Timestamp dateMvt) {
        this.dateMvt = dateMvt;
    }

    public Fournisseurs getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseurs fournisseur) {
        this.fournisseur = fournisseur;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }
}
