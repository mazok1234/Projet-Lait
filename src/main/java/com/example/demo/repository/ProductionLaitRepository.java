package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Employe;
import com.example.demo.entity.ProductionLait;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import com.example.demo.dto.BovinProductionSumDTO;

public interface ProductionLaitRepository extends JpaRepository<ProductionLait, Integer> {
    

    @Query("SELECT new com.example.demo.dto.BovinProductionSumDTO(b.id, b.numeroBoucle, SUM(p.quantite)) " +
       "FROM ProductionLait p " +
       "JOIN p.cycleBovin c " +
       "JOIN c.bovin b " +
       "GROUP BY b.id, b.numeroBoucle")
    List<BovinProductionSumDTO> findTotalProductionParBovin();
}