package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Employe;
import com.example.demo.entity.HistoriqueSalaire;
import com.example.demo.service.CongeService;
import com.example.demo.service.EmployeService;
import com.example.demo.service.HistoriqueSalaireService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeFicheController {

    private final EmployeService employeService;
    private final HistoriqueSalaireService historiqueSalaireService;
    private final CongeService congeService;

    public EmployeFicheController(EmployeService employeService,
                                   HistoriqueSalaireService historiqueSalaireService,
                                   CongeService congeService) {
        this.employeService = employeService;
        this.historiqueSalaireService = historiqueSalaireService;
        this.congeService = congeService;
    }

    @GetMapping("/employe/fiche")
    public String fiche(HttpSession session, Model model) {

        Integer employeId = (Integer) session.getAttribute("employeId");

        // Pas encore de controle d'acces strict (RH / pas d'employe lie) :
        // on gere juste l'absence de session pour eviter un crash.
        if (employeId == null) {
            model.addAttribute("error", "Aucun profil employe associe a ce compte.");
            return "employe/fiche";
        }

        Employe employe = employeService.getEmployeById(employeId);
        HistoriqueSalaire salaireActif = historiqueSalaireService.getSalaireActif(employeId);

        model.addAttribute("employe", employe);
        model.addAttribute("salaireActif", salaireActif);
        model.addAttribute("conges", congeService.getCongesByEmploye(employeId));

        return "employe/fiche";
    }
}