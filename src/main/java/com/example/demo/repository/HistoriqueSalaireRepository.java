package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.HistoriqueSalaire;

public interface HistoriqueSalaireRepository extends JpaRepository<HistoriqueSalaire, Integer> {

    HistoriqueSalaire findFirstByEmployeIdOrderByDateDebutDesc(Integer employeId);

    HistoriqueSalaire findFirstByEmployeIdAndDateFinIsNullOrderByDateDebutDesc(Integer employeId);

}