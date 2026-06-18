package com.example.demo.controller;

import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @GetMapping("/fichePaie/generate-page")
    public String showGenerateFichePaiePage(Model model) {
        List<Employe> employes = employeService.getAllEmploye();
        model.addAttribute("employes", employes);
        return "fichePaie/generer";
    }

    @GetMapping("/fichePaie/generate")
    public String generateFichePaie(@RequestParam("employeId") Integer employeId, @RequestParam("datepaie") LocalDate datePaiement, Model model) {
        Integer mois = datePaiement.getMonthValue();
        Integer annee = datePaiement.getYear();

        Employe employe = employeService.getEmployeById(employeId);
        if (employe == null) {
            model.addAttribute("error", "Employé non trouvé");
            return "fichePaie/generate";
        }

        Integer monthly_working_days = 22;

        Integer totalAbscence = 0; // attendre configuration des conges
        Integer totalworkingdays =  monthly_working_days - totalAbscence;

        Double mb = (employe.getSalaireBase() / monthly_working_days) * totalworkingdays;
        Double mn = mb * 0.9;

        FichePaie fichePaie = new FichePaie();
        fichePaie.setEmploye(employe);
        fichePaie.setMois(mois);
        fichePaie.setAnnee(annee);
        fichePaie.setMontantBrut(mb);
        fichePaie.setMontantNet(mn);
        fichePaie.setDatePaiement(datePaiement);
        fichePaie.setJoursTravailles(totalworkingdays);
        fichePaie.setJoursConge(totalAbscence);

        fichePaieService.saveFichePaie(fichePaie);

        return "redirect:/fichePaie/list";
    }
}