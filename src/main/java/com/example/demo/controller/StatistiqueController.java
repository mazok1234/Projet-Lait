package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.StatistiqueService;

@Controller
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping("/finance/statistique")
    public String afficherStatistiques(
            @RequestParam(required = false) Integer jour,
            @RequestParam(required = false) Integer mois,
            @RequestParam(required = false) Integer annee,
            Model model) {

        Map<String, BigDecimal> stats = statistiqueService.getStats(jour, mois, annee);

        model.addAttribute("recettes", stats.get("recettes"));
        model.addAttribute("depenses", stats.get("depenses"));
        model.addAttribute("benefice", stats.get("benefice"));
        model.addAttribute("jour", jour);
        model.addAttribute("mois", mois);
        model.addAttribute("annee", annee);

        return "finance/statistique";
    }
}