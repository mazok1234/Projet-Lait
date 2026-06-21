package com.example.demo.controller;

import com.example.demo.entity.ProductionLait;
import com.example.demo.entity.CycleBovin;
import com.example.demo.repository.CycleBovinRepository; // Ou ton service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import com.example.demo.service.ProductionLaitService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.dto.BovinProductionSumDTO;

@Controller
public class ProductionLaitController {

    @Autowired
    private CycleBovinRepository cycleBovinRepository;

    @Autowired
    private ProductionLaitService productionLaitService;

    @GetMapping("/ajouter_lait")
    public String ajout_lait(Model model) {
        model.addAttribute("productionLait", new ProductionLait());
        
        List<CycleBovin> cyclesActifs = cycleBovinRepository.getVacheLactations(); 
        model.addAttribute("cycles", cyclesActifs);
        
        return "productionLait/ajouter";
    }

    @PostMapping("/sauvegarder_lait")
    public String saveProductionLait(@ModelAttribute ProductionLait productionLait, RedirectAttributes redirectAttributes) {
        try {
            productionLaitService.InsererProduction(productionLait);
            redirectAttributes.addFlashAttribute("successMessage", "La traite a été enregistrée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
        }

        return "redirect:/ajouter_lait";
    }

    @GetMapping("/productions")
    public String listProductions(Model model) {
        List<BovinProductionSumDTO> productions = productionLaitService.findTotalProductionParBovin();
        model.addAttribute("productions", productions);
        return "productionLait/liste";
    }

    
}