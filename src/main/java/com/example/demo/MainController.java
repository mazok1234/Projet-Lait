package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.example.demo.service.ProductionLaitService;
import com.example.demo.dto.BovinProductionSumDTO;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class MainController {

    @Autowired
    private ProductionLaitService productionLaitService;

    @GetMapping("/")
    public String index(Model model) {
        List<BovinProductionSumDTO> productions = productionLaitService.findTotalProductionParBovin();

        model.addAttribute("productions", productions);
        return "index";
    }
}