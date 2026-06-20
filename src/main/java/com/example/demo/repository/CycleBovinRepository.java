package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Employe;
import com.example.demo.entity.CycleBovin;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.Map;

public interface CycleBovinRepository extends JpaRepository<CycleBovin, Integer> {

    @Query("SELECT c FROM CycleBovin c WHERE c.dateFin IS NULL AND c.etatCycle.id = 4")
    List<CycleBovin> getVacheLactations();


   @Query("SELECT new map(c.id as id, b.numeroBoucle as numeroBoucle, e.libelle as etatLibelle) " +
           "FROM CycleBovin c " +
           "JOIN c.bovin b " +
           "JOIN c.etatCycle e " +
           "WHERE e.id = 4 " +
           "AND c.dateDebut <= :date " +
           "AND (c.dateFin IS NULL OR c.dateFin >= :date)")
    List<Map<String, Object>> findLactationsByDate(@Param("date") LocalDate date);

}