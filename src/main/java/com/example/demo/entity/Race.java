package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Races")
public class Race {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Nom", nullable = false)
    private String nom;

    @OneToMany(mappedBy = "race")
    private List<Bovin> bovins;

    public Race() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<Bovin> getBovins() { return bovins; }
    public void setBovins(List<Bovin> bovins) { this.bovins = bovins; }
}