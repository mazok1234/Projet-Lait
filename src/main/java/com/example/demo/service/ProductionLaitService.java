package com.example.demo.service;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entity.ProductionLait;
import com.example.demo.repository.ProductionLaitRepository;
import com.example.demo.dto.BovinProductionSumDTO;


@Service
public class ProductionLaitService {

    private final ProductionLaitRepository productionLaitRepository;

    public ProductionLaitService(ProductionLaitRepository productionLaitRepository) {
        this.productionLaitRepository = productionLaitRepository;
    }

    public List<ProductionLait> getAllProductions() {
        return productionLaitRepository.findAll();
    }

    public ProductionLait getProductionById(int id) {
        return productionLaitRepository.findById(id).orElse(null);
    }

    public ProductionLait InsererProduction(ProductionLait production) {
        return productionLaitRepository.save(production);
    }

    public void deleteProduction(int id) {
        productionLaitRepository.deleteById(id);
    }

    public List<BovinProductionSumDTO> findTotalProductionParBovin() {
        return productionLaitRepository.findTotalProductionParBovin();
    }
}