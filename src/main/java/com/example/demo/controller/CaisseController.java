package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Transaction;
import com.example.demo.service.CaisseService;

@Controller
public class CaisseController {

    @Autowired
    private CaisseService caisseService;

    @GetMapping("/finance")
    public String afficherCaisse(Model model) {
        BigDecimal solde = caisseService.getSoldeCaisse();
        List<Transaction> dernieresTransactions = caisseService.getDernieresTransactions(5);

        model.addAttribute("solde", solde);
        model.addAttribute("transactions", dernieresTransactions);

        return "finance/caisse";
    }

    @GetMapping("/finance/historique")
    public String afficherHistorique(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "dateTrans") String sort,
            @RequestParam(defaultValue = "desc") String dir,
            Model model) {

        Page<Transaction> pageTransactions = caisseService.getTransactionsPaginees(page, 10, sort, dir);

        model.addAttribute("transactions", pageTransactions.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", pageTransactions.getTotalPages());
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("reverseDir", dir.equals("asc") ? "desc" : "asc");

        return "finance/historique";
    }
}
