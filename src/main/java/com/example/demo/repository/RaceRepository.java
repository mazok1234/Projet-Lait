package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Race;

public interface RaceRepository extends JpaRepository<Race, Integer> {
    List<Race> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);

    boolean existsByNomIgnoreCaseAndIdNot(String nom, Integer id);
}
