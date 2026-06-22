package com.example.demo.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BovinCreateForm;
import com.example.demo.dto.EtatSanteForm;
import com.example.demo.dto.RaceForm;
import com.example.demo.entity.EtatSante;
import com.example.demo.entity.Race;
import com.example.demo.service.CheptelService;

@Controller
@RequestMapping("/cheptel")
public class CheptelController {
    private final CheptelService cheptelService;

    public CheptelController(CheptelService cheptelService) {
        this.cheptelService = cheptelService;
    }

    @GetMapping({ "", "/" })
    public String dashboard() {
        return "cheptel/index";
    }

    @GetMapping("/bovins")
    public String listeBovins(@RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "actif", required = false) String actif,
            Model model) {
        Boolean actifValue = null;
        String actifKey = "tous";
        if (actif != null) {
            String normalized = actif.trim().toLowerCase();
            if ("actifs".equals(normalized) || "true".equals(normalized)) {
                actifValue = true;
                actifKey = "actifs";
            } else if ("inactifs".equals(normalized) || "false".equals(normalized)) {
                actifValue = false;
                actifKey = "inactifs";
            }
        }

        model.addAttribute("bovins", cheptelService.rechercherBovins(q, actifValue));
        model.addAttribute("q", q);
        model.addAttribute("actif", actifKey);
        return "cheptel/bovins-liste";
    }

    @GetMapping("/bovins/ajout")
    public String pageAjout(Model model) {
        model.addAttribute("bovinForm", new BovinCreateForm());
        model.addAttribute("races", cheptelService.getRaces());
        model.addAttribute("etatsSante", cheptelService.getEtatsSante());
        return "cheptel/bovins-ajouter";
    }

    @PostMapping("/bovins")
    public String ajouter(@ModelAttribute("bovinForm") BovinCreateForm form,
            RedirectAttributes redirectAttributes) {
        if (form.getNumeroBoucle() == null || form.getNumeroBoucle().isBlank()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Le numéro de boucle est obligatoire.");
            return "redirect:/cheptel/bovins/ajout";
        }
        if (form.getRaceId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La race est obligatoire.");
            return "redirect:/cheptel/bovins/ajout";
        }
        if (form.getEtatSanteId() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "L'état de santé est obligatoire.");
            return "redirect:/cheptel/bovins/ajout";
        }
        if (form.getDateNaissance() == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date de naissance est obligatoire.");
            return "redirect:/cheptel/bovins/ajout";
        }

        try {
            cheptelService.ajouterBovin(form);
            redirectAttributes.addFlashAttribute("successMessage", "Vache ajoutée avec succès.");
            return "redirect:/cheptel/bovins";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Numéro de boucle déjà utilisé.");
            return "redirect:/cheptel/bovins/ajout";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erreur lors de l'ajout : " + e.getMessage());
            return "redirect:/cheptel/bovins/ajout";
        }
    }

   
    @GetMapping("/races")
    public String races(Model model) {
        model.addAttribute("raceForm", new RaceForm());
        model.addAttribute("races", cheptelService.getRaces());
        return "cheptel/races";
    }

    @GetMapping("/races/{id}/edit")
    public String editRace(@PathVariable Integer id, Model model) {
        Race race = cheptelService.getRace(id);
        RaceForm form = new RaceForm();
        form.setId(race.getId());
        form.setNom(race.getNom());
        model.addAttribute("raceForm", form);
        model.addAttribute("races", cheptelService.getRaces());
        return "cheptel/races";
    }

    @PostMapping("/races")
    public String saveRace(@ModelAttribute("raceForm") RaceForm form, RedirectAttributes redirectAttributes) {
        try {
            cheptelService.enregistrerRace(form.getId(), form.getNom());
            redirectAttributes.addFlashAttribute("successMessage", "Race enregistrée avec succès.");
            return "redirect:/cheptel/races";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return form.getId() == null ? "redirect:/cheptel/races" : "redirect:/cheptel/races/" + form.getId() + "/edit";
        }
    }

    @PostMapping("/races/{id}/delete")
    public String deleteRace(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cheptelService.supprimerRace(id);
            redirectAttributes.addFlashAttribute("successMessage", "Race supprimée.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Suppression impossible : cette race est déjà utilisée.");
        }
        return "redirect:/cheptel/races";
    }
  

    @GetMapping("/etats-sante")
    public String etatsSante(Model model) {
        model.addAttribute("etatForm", new EtatSanteForm());
        model.addAttribute("etatsSante", cheptelService.getEtatsSante());
        return "cheptel/etats-sante";
    }

    @GetMapping("/etats-sante/{id}/edit")
    public String editEtatSante(@PathVariable Integer id, Model model) {
        EtatSante etat = cheptelService.getEtatSante(id);
        EtatSanteForm form = new EtatSanteForm();
        form.setId(etat.getId());
        form.setLibelle(etat.getLibelle());
        model.addAttribute("etatForm", form);
        model.addAttribute("etatsSante", cheptelService.getEtatsSante());
        return "cheptel/etats-sante";
    }

    @PostMapping("/etats-sante")
    public String saveEtatSante(@ModelAttribute("etatForm") EtatSanteForm form, RedirectAttributes redirectAttributes) {
        try {
            cheptelService.enregistrerEtatSante(form.getId(), form.getLibelle());
            redirectAttributes.addFlashAttribute("successMessage", "État de santé enregistré avec succès.");
            return "redirect:/cheptel/etats-sante";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return form.getId() == null ? "redirect:/cheptel/etats-sante"
                    : "redirect:/cheptel/etats-sante/" + form.getId() + "/edit";
        }
    }

    @PostMapping("/etats-sante/{id}/delete")
    public String deleteEtatSante(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            cheptelService.supprimerEtatSante(id);
            redirectAttributes.addFlashAttribute("successMessage", "État de santé supprimé.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Suppression impossible : cet état de santé est déjà utilisé.");
        }
        return "redirect:/cheptel/etats-sante";
    }
}