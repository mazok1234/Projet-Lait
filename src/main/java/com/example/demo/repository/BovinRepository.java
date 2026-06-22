package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Bovin;

public interface BovinRepository extends JpaRepository<Bovin, Integer> {
    boolean existsByNumeroBoucleIgnoreCase(String numeroBoucle);
    
    List<Bovin> findAllByOrderByIdDesc();
    
    List<Bovin> findByActifTrueOrderByIdDesc();
}