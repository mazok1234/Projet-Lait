package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.FichePaie;

public interface FichePaieRepository extends JpaRepository<FichePaie, Integer> {

}