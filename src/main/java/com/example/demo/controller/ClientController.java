package com.example.demo.controller;

import com.example.demo.entity.Clients;
import com.example.demo.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Page principale : Liste + Recherche + Formulaire (Nouveau ou Sélectionné)
    @GetMapping
    public String indexPage(@RequestParam(value = "searchNom", required = false) String searchNom,
                            @RequestParam(value = "searchDate", required = false) String searchDate,
                            @RequestParam(value = "editId", required = false) Integer editId,
                            Model model) {
        
        // 1. Liste des clients (filtrée ou totale)
        model.addAttribute("clients", clientService.getClients(searchNom, searchDate));
        
        // 2. Gestion du formulaire (Nouveau client ou Modification)
        if (editId != null) {
            model.addAttribute("clientForm", clientService.getClientById(editId));
        } else {
            model.addAttribute("clientForm", new Clients());
        }

        // Rétention des valeurs de recherche pour l'interface
        model.addAttribute("searchNom", searchNom);
        model.addAttribute("searchDate", searchDate);

        return "clients";
    }

    // Traitement de l'insertion / modification
    @PostMapping("/enregistrer")
    public String enregistrerClient(@ModelAttribute("clientForm") Clients client) {
        clientService.saveClient(client);
        return "redirect:/clients";
    }
}