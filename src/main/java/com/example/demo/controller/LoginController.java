package com.example.demo.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Utilisateur;
import com.example.demo.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        // Deja connecte -> on renvoie direct vers son espace
        if (session.getAttribute("utilisateurId") != null) {
            return "redirect:" + redirectionSelonRole((String) session.getAttribute("role"));
        }
        return "login/index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                         @RequestParam String password,
                         HttpServletRequest request,
                         Model model) {

        Optional<Utilisateur> utilisateurOpt = authService.login(email, password);

        if (utilisateurOpt.isEmpty()) {
            model.addAttribute("error", "Email ou mot de passe incorrect.");
            model.addAttribute("email", email);
            return "login/index";
        }

        Utilisateur utilisateur = utilisateurOpt.get();
        String role = utilisateur.getRole() != null ? utilisateur.getRole().getNom() : "";

        // On regenere la session pour eviter la fixation de session
        HttpSession session = request.getSession(true);
        session.setAttribute("utilisateurId", utilisateur.getId());
        session.setAttribute("nom", utilisateur.getNom());
        session.setAttribute("email", utilisateur.getEmail());
        session.setAttribute("role", role);
        if (utilisateur.getEmploye() != null) {
            session.setAttribute("employeId", utilisateur.getEmploye().getId());
        }

        return "redirect:" + redirectionSelonRole(role);
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    /**
     * Determine la page d'accueil selon le role.
     * On se base sur le libelle de RoleUtilisateur (ex: "RH", "Employe").
     * A adapter si d'autres libelles sont utilises en base.
     */
    private String redirectionSelonRole(String role) {
        if (role != null && role.toLowerCase().contains("rh")) {
            return "/";
        }
        return "/employe/fiche";
    }
}