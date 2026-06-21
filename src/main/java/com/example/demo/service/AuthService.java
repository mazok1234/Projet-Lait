package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Utilisateur;
import com.example.demo.repository.UtilisateurRepository;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;

    public AuthService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Verifie email + mot de passe.
     * Retourne l'utilisateur si les identifiants sont corrects, sinon Optional.empty().
     *
     * NB: le mot de passe est compare en clair car aucun hash n'est encore en place
     * dans la base (colonne Utilisateurs.Password). A faire evoluer plus tard
     * (BCrypt) si on passe sur Spring Security.
     */
    public Optional<Utilisateur> login(String email, String password) {
        if (email == null || password == null) {
            return Optional.empty();
        }
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email.trim());
        if (utilisateur.isPresent() && password.equals(utilisateur.get().getPassword())) {
            return utilisateur;
        }
        return Optional.empty();
    }
}