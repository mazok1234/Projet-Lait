package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Employe;
import com.example.demo.entity.FichePaie;

public interface FichePaieRepository extends JpaRepository<FichePaie, Integer> {

@Query("SELECT fp FROM FichePaie fp WHERE " +
       "(:mois IS NULL OR fp.mois = :mois) AND " +
       "(:annee IS NULL OR fp.annee = :annee) AND " +
       "(:employeId IS NULL OR fp.employe.id = :employeId)")
List<FichePaie> findByMoisAndAnneeAndEmploye(
    @Param("mois") Integer mois,
    @Param("annee") Integer annee,
    @Param("employeId") Integer employeId
);

FichePaie findTopByEmployeOrderByDatePaiementDesc(Employe employe);


}