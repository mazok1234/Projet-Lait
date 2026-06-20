package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "bovins")
public class Bovin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "numeroboucle", nullable = false, unique = true)
    private String numeroBoucle;

    @ManyToOne
    @JoinColumn(name = "raceid", nullable = false)
    private Race race;

    @ManyToOne
    @JoinColumn(name = "etatsanteid", nullable = false)
    private EtatSante etatSante;

    @Column(name = "datenaissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "sexe", nullable = false)
    private String sexe;

    @Column(name = "isactif", nullable = false)
    private boolean isActif;

    @Column(name = "dateajout")
    private LocalDateTime dateAjout;

    @OneToMany(mappedBy = "bovin")
    @JsonIgnore
    private List<CycleBovin> cycles;

    public Bovin() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNumeroBoucle() { return numeroBoucle; }
    public void setNumeroBoucle(String numeroBoucle) { this.numeroBoucle = numeroBoucle; }

    public Race getRace() { return race; }
    public void setRace(Race race) { this.race = race; }

    public EtatSante getEtatSante() { return etatSante; }
    public void setEtatSante(EtatSante etatSante) { this.etatSante = etatSante; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getSexe() { return sexe; }
    public void setSexe(String sexe) { this.sexe = sexe; }

    public boolean isActif() { return isActif; }
    public void setActif(boolean actif) { isActif = actif; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }

    public List<CycleBovin> getCycles() { return cycles; }
    public void setCycles(List<CycleBovin> cycles) { this.cycles = cycles; }
}