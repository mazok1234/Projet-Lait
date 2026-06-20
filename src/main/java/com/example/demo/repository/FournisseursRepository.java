package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Fournisseurs;

public interface FournisseursRepository extends JpaRepository<Fournisseurs, Integer> {
    List<Fournisseurs> findByNomContainingIgnoreCaseOrderByNomAsc(String nom);

    List<Fournisseurs> findAllByOrderByNomAsc();
}
