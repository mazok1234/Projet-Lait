package com.example.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Aliments;
import com.example.demo.entity.Fournisseurs;
import com.example.demo.service.StockAlimentaireService;

@Controller
@RequestMapping("/stockAlim")
public class StockAlimentaireController {
    private final StockAlimentaireService stockAlimentaireService;

    public StockAlimentaireController(StockAlimentaireService stockAlimentaireService) {
        this.stockAlimentaireService = stockAlimentaireService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("stock", stockAlimentaireService.getStockActuel(null));
        model.addAttribute("alertes", stockAlimentaireService.getAlertesStockBas());
        model.addAttribute("approvisionnements",
                stockAlimentaireService.rechercherApprovisionnements(null, null, null, null));
        model.addAttribute("consommations",
                stockAlimentaireService.rechercherConsommations(null, null, null, null));
        return "stockAlim/index";
    }

    @GetMapping("/fournisseurs")
    public String fournisseurs(@RequestParam(name = "q", required = false) String q, Model model) {
        model.addAttribute("fournisseurs", stockAlimentaireService.rechercherFournisseurs(q));
        model.addAttribute("q", q);
        return "stockAlim/fournisseurs";
    }

    @GetMapping("/fournisseurs/new")
    public String nouveauFournisseur(Model model) {
        model.addAttribute("fournisseur", new Fournisseurs());
        model.addAttribute("mode", "creation");
        return "stockAlim/fournisseur-form";
    }

    @GetMapping("/fournisseurs/{id}/edit")
    public String modifierFournisseur(@PathVariable Integer id, Model model) {
        model.addAttribute("fournisseur", stockAlimentaireService.getFournisseur(id));
        model.addAttribute("mode", "modification");
        return "stockAlim/fournisseur-form";
    }

    @PostMapping("/fournisseurs")
    public String enregistrerFournisseur(@ModelAttribute Fournisseurs fournisseur, Model model) {
        List<String> errors = validateFournisseur(fournisseur);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("fournisseur", fournisseur);
            model.addAttribute("mode", fournisseur.getId() == null ? "creation" : "modification");
            return "stockAlim/fournisseur-form";
        }
        stockAlimentaireService.saveFournisseur(fournisseur);
        return "redirect:/stockAlim/fournisseurs";
    }

    @PostMapping("/fournisseurs/{id}/delete")
    public String supprimerFournisseur(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            stockAlimentaireService.deleteFournisseur(id);
            redirectAttributes.addFlashAttribute("success", "Suppression effectuee.");
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("error", "Suppression bloquee: ce fournisseur est deja lie a des mouvements.");
        }
        return "redirect:/stockAlim/fournisseurs";
    }

    @GetMapping("/aliments")
    public String aliments(@RequestParam(name = "q", required = false) String q, Model model) {
        model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(q));
        model.addAttribute("q", q);
        return "stockAlim/aliments";
    }

    @GetMapping("/aliments/new")
    public String nouvelAliment(Model model) {
        model.addAttribute("aliment", new Aliments());
        model.addAttribute("mode", "creation");
        return "stockAlim/aliment-form";
    }

    @GetMapping("/aliments/{id}/edit")
    public String modifierAliment(@PathVariable Integer id, Model model) {
        model.addAttribute("aliment", stockAlimentaireService.getAliment(id));
        model.addAttribute("mode", "modification");
        return "stockAlim/aliment-form";
    }

    @PostMapping("/aliments")
    public String enregistrerAliment(@ModelAttribute Aliments aliment, Model model) {
        List<String> errors = validateAliment(aliment);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("aliment", aliment);
            model.addAttribute("mode", aliment.getId() == null ? "creation" : "modification");
            return "stockAlim/aliment-form";
        }
        stockAlimentaireService.saveAliment(aliment);
        return "redirect:/stockAlim/aliments";
    }

    @PostMapping("/aliments/{id}/delete")
    public String supprimerAliment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            stockAlimentaireService.deleteAliment(id);
            redirectAttributes.addFlashAttribute("success", "Suppression effectuee.");
        } catch (DataIntegrityViolationException exception) {
            redirectAttributes.addFlashAttribute("error", "Suppression bloquee: cet aliment est deja lie a des mouvements.");
        }
        return "redirect:/stockAlim/aliments";
    }

    @GetMapping("/approvisionnements")
    public String approvisionnements(
            @RequestParam(name = "du", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate du,
            @RequestParam(name = "au", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate au,
            @RequestParam(name = "fournisseurId", required = false) Integer fournisseurId,
            @RequestParam(name = "q", required = false) String q,
            Model model) {
        model.addAttribute("lignes", stockAlimentaireService.rechercherApprovisionnements(du, au, fournisseurId, q));
        model.addAttribute("fournisseurs", stockAlimentaireService.rechercherFournisseurs(null));
        model.addAttribute("du", du);
        model.addAttribute("au", au);
        model.addAttribute("fournisseurId", fournisseurId);
        model.addAttribute("q", q);
        return "stockAlim/approvisionnements";
    }

    @GetMapping("/approvisionnements/new")
    public String nouvelApprovisionnement(
            @RequestParam(name = "alimentId", required = false) Integer alimentId,
            Model model) {
        model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(null));
        model.addAttribute("fournisseurs", stockAlimentaireService.rechercherFournisseurs(null));
        model.addAttribute("alimentId", alimentId);
        model.addAttribute("fournisseurId", null);
        model.addAttribute("quantite", null);
        model.addAttribute("prixUnitaire", null);
        model.addAttribute("dateMouvement", null);
        return "stockAlim/approvisionnement-form";
    }

    @PostMapping("/approvisionnements")
    public String enregistrerApprovisionnement(
            @RequestParam(required = false) Integer alimentId,
            @RequestParam(required = false) Integer fournisseurId,
            @RequestParam(required = false) Double quantite,
            @RequestParam(required = false) Double prixUnitaire,
            @RequestParam(name = "dateMouvement", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMouvement,
            Model model) {
        List<String> errors = validateApprovisionnement(alimentId, fournisseurId, quantite, prixUnitaire, dateMouvement);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(null));
            model.addAttribute("fournisseurs", stockAlimentaireService.rechercherFournisseurs(null));
            model.addAttribute("alimentId", alimentId);
            model.addAttribute("fournisseurId", fournisseurId);
            model.addAttribute("quantite", quantite);
            model.addAttribute("prixUnitaire", prixUnitaire);
            model.addAttribute("dateMouvement", dateMouvement);
            return "stockAlim/approvisionnement-form";
        }
        stockAlimentaireService.enregistrerApprovisionnement(alimentId, fournisseurId, quantite, prixUnitaire, dateMouvement);
        return "redirect:/stockAlim/approvisionnements";
    }

    @GetMapping("/consommations")
    public String consommations(
            @RequestParam(name = "du", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate du,
            @RequestParam(name = "au", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate au,
            @RequestParam(name = "alimentId", required = false) Integer alimentId,
            @RequestParam(name = "q", required = false) String q,
            Model model) {
        model.addAttribute("lignes", stockAlimentaireService.rechercherConsommations(du, au, alimentId, q));
        model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(null));
        model.addAttribute("du", du);
        model.addAttribute("au", au);
        model.addAttribute("alimentId", alimentId);
        model.addAttribute("q", q);
        return "stockAlim/consommations";
    }

    @GetMapping("/consommations/new")
    public String nouvelleConsommation(Model model) {
        model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(null));
        model.addAttribute("alimentId", null);
        model.addAttribute("bovinId", null);
        model.addAttribute("quantite", null);
        model.addAttribute("dateMouvement", null);
        return "stockAlim/consommation-form";
    }

    @PostMapping("/consommations")
    public String enregistrerConsommation(
            @RequestParam(required = false) Integer alimentId,
            @RequestParam(required = false) Integer bovinId,
            @RequestParam(required = false) Double quantite,
            @RequestParam(name = "dateMouvement", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateMouvement,
            Model model) {
        List<String> errors = validateConsommation(alimentId, bovinId, quantite, dateMouvement);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("aliments", stockAlimentaireService.rechercherAliments(null));
            model.addAttribute("alimentId", alimentId);
            model.addAttribute("bovinId", bovinId);
            model.addAttribute("quantite", quantite);
            model.addAttribute("dateMouvement", dateMouvement);
            return "stockAlim/consommation-form";
        }
        stockAlimentaireService.enregistrerConsommation(alimentId, bovinId, quantite, dateMouvement);
        return "redirect:/stockAlim/consommations";
    }

    @GetMapping("/mouvements")
    public String mouvements(
            @RequestParam(name = "du", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate du,
            @RequestParam(name = "au", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate au,
            @RequestParam(name = "typeId", required = false) Integer typeId,
            Model model) {
        model.addAttribute("mouvements", stockAlimentaireService.rechercherMouvements(du, au, typeId));
        model.addAttribute("types", stockAlimentaireService.getTypesMouvement());
        model.addAttribute("du", du);
        model.addAttribute("au", au);
        model.addAttribute("typeId", typeId);
        return "stockAlim/mouvements";
    }

    @GetMapping("/mouvements/{id}")
    public String detailsMouvement(@PathVariable Integer id, Model model) {
        model.addAttribute("mouvement", stockAlimentaireService.getMouvement(id));
        model.addAttribute("lignes", stockAlimentaireService.getLignesMouvement(id));
        return "stockAlim/mouvement-details";
    }

    @GetMapping("/etat-stock")
    public String etatStock(
            @RequestParam(name = "q", required = false) String q,
            @RequestParam(name = "etat", required = false, defaultValue = "TOUS") String etat,
            @RequestParam(name = "du", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate du,
            @RequestParam(name = "au", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate au,
            @RequestParam(name = "dateEtat", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEtat,
            Model model) {
        if (du == null && au == null && dateEtat != null) {
            du = dateEtat;
            au = dateEtat;
        }

        if (du != null && au != null && du.isAfter(au)) {
            model.addAttribute("errors", List.of("La date de debut doit etre inferieure ou egale a la date de fin."));
            model.addAttribute("stock", List.of());
        } else {
            model.addAttribute("stock", stockAlimentaireService.getStockActuel(q, etat, du, au));
        }

        model.addAttribute("q", q);
        model.addAttribute("etat", etat);
        model.addAttribute("du", du);
        model.addAttribute("au", au);
        return "stockAlim/etat-stock";
    }

    @GetMapping("/alertes")
    public String alertes(Model model) {
        model.addAttribute("alertes", stockAlimentaireService.getAlertesStockBas());
        return "stockAlim/alertes";
    }

    @GetMapping("/not-found")
    public String notFound() {
        return "redirect:/stockAlim";
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound() {
        return "redirect:/stockAlim/not-found";
    }

    private List<String> validateFournisseur(Fournisseurs fournisseur) {
        List<String> errors = new ArrayList<>();
        String nom = trimToNull(fournisseur.getNom());
        String telephone = trimToNull(fournisseur.getTelephone());
        String adresse = trimToNull(fournisseur.getAdresse());

        fournisseur.setNom(nom);
        fournisseur.setTelephone(telephone);
        fournisseur.setAdresse(adresse);

        if (nom == null) {
            errors.add("Le nom du fournisseur est obligatoire.");
        } else if (nom.length() > 200) {
            errors.add("Le nom du fournisseur ne doit pas depasser 200 caracteres.");
        }

        if (telephone != null && telephone.length() > 50) {
            errors.add("Le telephone ne doit pas depasser 50 caracteres.");
        }

        if (adresse != null && adresse.length() > 255) {
            errors.add("L'adresse ne doit pas depasser 255 caracteres.");
        }

        return errors;
    }

    private List<String> validateAliment(Aliments aliment) {
        List<String> errors = new ArrayList<>();
        String nom = trimToNull(aliment.getNom());
        String unite = trimToNull(aliment.getUnite());

        aliment.setNom(nom);
        aliment.setUnite(unite);

        if (nom == null) {
            errors.add("Le nom de l'aliment est obligatoire.");
        } else if (nom.length() > 200) {
            errors.add("Le nom de l'aliment ne doit pas depasser 200 caracteres.");
        }

        if (unite == null) {
            errors.add("L'unite est obligatoire.");
        } else if (unite.length() > 50) {
            errors.add("L'unite ne doit pas depasser 50 caracteres.");
        }

        if (aliment.getSeuilStockMin() != null && aliment.getSeuilStockMin() < 0) {
            errors.add("Le seuil de stock minimum doit etre superieur ou egal a 0.");
        }

        if (aliment.getPrixAchat() != null && aliment.getPrixAchat() < 0) {
            errors.add("Le prix d'achat doit etre superieur ou egal a 0.");
        }

        return errors;
    }

    private List<String> validateApprovisionnement(Integer alimentId, Integer fournisseurId, Double quantite,
            Double prixUnitaire, LocalDate dateMouvement) {
        List<String> errors = new ArrayList<>();

        if (alimentId == null) {
            errors.add("Veuillez selectionner un aliment.");
        }
        if (fournisseurId == null) {
            errors.add("Veuillez selectionner un fournisseur.");
        }
        if (quantite == null || quantite <= 0) {
            errors.add("La quantite doit etre strictement superieure a 0.");
        }
        if (prixUnitaire == null || prixUnitaire <= 0) {
            errors.add("Le prix unitaire doit etre strictement superieur a 0.");
        }
        if (dateMouvement != null && dateMouvement.isAfter(LocalDate.now())) {
            errors.add("La date du mouvement ne peut pas etre dans le futur.");
        }

        return errors;
    }

    private List<String> validateConsommation(Integer alimentId, Integer bovinId, Double quantite,
            LocalDate dateMouvement) {
        List<String> errors = new ArrayList<>();

        if (alimentId == null) {
            errors.add("Veuillez selectionner un aliment.");
        }
        if (quantite == null || quantite <= 0) {
            errors.add("La quantite doit etre strictement superieure a 0.");
        }
        if (bovinId != null && bovinId <= 0) {
            errors.add("Le bovin doit avoir un identifiant positif.");
        }
        if (dateMouvement != null && dateMouvement.isAfter(LocalDate.now())) {
            errors.add("La date du mouvement ne peut pas etre dans le futur.");
        }

        return errors;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
