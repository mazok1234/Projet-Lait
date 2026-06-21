package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Conge;

public interface CongeRepository extends JpaRepository<Conge, Integer> {

    List<Conge> findByEmployeIdOrderByDateDemandeDesc(Integer employeId);

}