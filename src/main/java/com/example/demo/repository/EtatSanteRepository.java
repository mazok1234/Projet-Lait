package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.EtatSante;

public interface EtatSanteRepository extends JpaRepository<EtatSante, Integer> {
    List<EtatSante> findAllByOrderByLibelleAsc();

    boolean existsByLibelleIgnoreCase(String libelle);

    boolean existsByLibelleIgnoreCaseAndIdNot(String libelle, Integer id);
}
