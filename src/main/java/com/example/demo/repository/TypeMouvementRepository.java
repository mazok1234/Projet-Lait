package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TypeMouvement;

public interface TypeMouvementRepository extends JpaRepository<TypeMouvement, Integer> {
    Optional<TypeMouvement> findByLibelleIgnoreCase(String libelle);
}
