package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.FichePaie;
import com.example.demo.service.FichePaieService;

@Controller
public class FichePaieController {
    private final FichePaieService fichePaieService;

    public FichePaieController(FichePaieService fichePaieService){
        this.fichePaieService = fichePaieService;
    }
    @GetMapping("/fichePaie/list")
    public String listFichePaie(Model model){
        List<FichePaie> fichePaies = fichePaieService.getAllFichePaie();
        model.addAttribute("fichePaies", fichePaies);
        return "fichePaie/list";
    }
}