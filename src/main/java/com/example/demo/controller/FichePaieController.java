package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Employe;
import com.example.demo.entity.FichePaie;
import com.example.demo.service.EmployeService;
import com.example.demo.service.FichePaieService;

@Controller
public class FichePaieController {
    private final FichePaieService fichePaieService;
    private final EmployeService employeService;

    public FichePaieController(FichePaieService fichePaieService,
                                EmployeService employeService){
        this.fichePaieService = fichePaieService;
        this.employeService = employeService;
    }


    @GetMapping("/fichePaie/list")
    public String listFichePaie(@RequestParam(name = "mois" , required = false) Integer mois, @RequestParam(name = "annee", required = false) Integer annee,@RequestParam(name = "employeId", required= false) Integer employeId, Model model) {

    List<FichePaie> fichePaies = fichePaieService.findByMoisAndAnneeAndEmploye(mois, annee, employeId);
    List<Employe> employes = employeService.getAllEmploye();
    // List<FichePaie> fichePaies = fichePaieService.getAllFichePaie();

    model.addAttribute("fichePaies", fichePaies);
    model.addAttribute("employes", employes);

    return "fichePaie/list";
}

}