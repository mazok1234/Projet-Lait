package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mouvementstockfille")
public class MouvementStockFille {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "mouvementstockid")
    private MouvementsStock mouvementStock;

    @ManyToOne
    @JoinColumn(name = "alimentid")
    private Aliments aliment;

    @Column(name = "quantite")
    private Double quantite;

    @Column(name = "prixunitaire")
    private Double prixUnitaire;

    @Column(name = "bovinid")
    private Integer bovinId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public MouvementsStock getMouvementStock() {
        return mouvementStock;
    }

    public void setMouvementStock(MouvementsStock mouvementStock) {
        this.mouvementStock = mouvementStock;
    }

    public Aliments getAliment() {
        return aliment;
    }

    public void setAliment(Aliments aliment) {
        this.aliment = aliment;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getBovinId() {
        return bovinId;
    }

    public void setBovinId(Integer bovinId) {
        this.bovinId = bovinId;
    }
}
