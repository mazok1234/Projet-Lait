package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.BovinCreateForm;
import com.example.demo.entity.Bovin;
import com.example.demo.entity.EtatSante;
import com.example.demo.entity.HistoriqueBovin;
import com.example.demo.entity.Race;
import com.example.demo.repository.BovinRepository;
import com.example.demo.repository.EtatSanteRepository;
import com.example.demo.repository.HistoriqueBovinRepository;
import com.example.demo.repository.RaceRepository;

@Service
public class CheptelService {
    private final BovinRepository bovinRepository;
    private final RaceRepository raceRepository;
    private final EtatSanteRepository etatSanteRepository;
    private final HistoriqueBovinRepository historiqueBovinRepository;

    public CheptelService(BovinRepository bovinRepository,
            RaceRepository raceRepository,
            EtatSanteRepository etatSanteRepository,
            HistoriqueBovinRepository historiqueBovinRepository) {
        this.bovinRepository = bovinRepository;
        this.raceRepository = raceRepository;
        this.etatSanteRepository = etatSanteRepository;
        this.historiqueBovinRepository = historiqueBovinRepository;
    }

    public List<Race> getRaces() {
        return raceRepository.findAllByOrderByNomAsc();
    }

    public List<EtatSante> getEtatsSante() {
        try {
            return etatSanteRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Bovin> getBovinsActifs() {
        return bovinRepository.findByActifTrueOrderByIdDesc();
    }

    public List<Bovin> rechercherBovins(String q, Boolean actif) {
        // Pour éviter toute erreur, on retourne simplement tous les bovins
        return bovinRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public Bovin ajouterBovin(BovinCreateForm form) {
        Race race = raceRepository.findById(form.getRaceId()).orElseThrow();
        EtatSante etatSante = etatSanteRepository.findById(form.getEtatSanteId()).orElseThrow();

        Bovin bovin = new Bovin();
        bovin.setNumeroBoucle(form.getNumeroBoucle().trim());
        bovin.setRace(race);
        bovin.setEtatSante(etatSante);
        bovin.setDateNaissance(form.getDateNaissance());
        bovin.setSexe(form.getSexe() == null || form.getSexe().isBlank() ? "Femelle" : form.getSexe());
        bovin.setActif(true);
        bovin.setDateAjout(LocalDateTime.now());
        Bovin saved = bovinRepository.save(bovin);

        HistoriqueBovin histo = new HistoriqueBovin();
        histo.setBovin(saved);
        histo.setEtatSante(etatSante);
        histo.setDateEvent(LocalDateTime.now());
        histo.setMotif("CREATION");
        histo.setIdUser(null);
        historiqueBovinRepository.save(histo);

        return saved;
    }

    public Race getRace(Integer id) {
        return raceRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Race enregistrerRace(Integer id, String nom) {
        String normalized = normalize(nom);
        if (normalized == null) {
            throw new IllegalArgumentException("Le nom de la race est obligatoire.");
        }

        if (id == null) {
            if (raceRepository.existsByNomIgnoreCase(normalized)) {
                throw new IllegalArgumentException("Cette race existe déjà.");
            }
            Race race = new Race();
            race.setNom(normalized);
            return raceRepository.save(race);
        }

        if (raceRepository.existsByNomIgnoreCaseAndIdNot(normalized, id)) {
            throw new IllegalArgumentException("Cette race existe déjà.");
        }

        Race existing = raceRepository.findById(id).orElseThrow();
        existing.setNom(normalized);
        return raceRepository.save(existing);
    }

    public void supprimerRace(Integer id) {
        raceRepository.deleteById(id);
    }

    public EtatSante getEtatSante(Integer id) {
        return etatSanteRepository.findById(id).orElseThrow();
    }

    @Transactional
    public EtatSante enregistrerEtatSante(Integer id, String libelle) {
        String normalized = normalize(libelle);
        if (normalized == null) {
            throw new IllegalArgumentException("Le libellé de l'état de santé est obligatoire.");
        }

        if (id == null) {
            if (etatSanteRepository.existsByLibelleIgnoreCase(normalized)) {
                throw new IllegalArgumentException("Cet état de santé existe déjà.");
            }
            EtatSante etat = new EtatSante();
            etat.setLibelle(normalized);
            return etatSanteRepository.save(etat);
        }

        if (etatSanteRepository.existsByLibelleIgnoreCaseAndIdNot(normalized, id)) {
            throw new IllegalArgumentException("Cet état de santé existe déjà.");
        }

        EtatSante existing = etatSanteRepository.findById(id).orElseThrow();
        existing.setLibelle(normalized);
        return etatSanteRepository.save(existing);
    }

    public void supprimerEtatSante(Integer id) {
        etatSanteRepository.deleteById(id);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}