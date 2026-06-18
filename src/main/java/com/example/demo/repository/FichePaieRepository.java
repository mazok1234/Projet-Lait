package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.demo.entity.Employe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.example.demo.entity.FichePaie;

public interface FichePaieRepository extends JpaRepository<FichePaie, Integer> {

    @Query("SELECT fp from FichePaie fp where (:mois IS NULL or fp.mois = :mois) and (:annee IS NULL or fp.annee = :annee) and (:employe IS NULL or fp.employe = :employe) ")
    List<FichePaie> findByMoisAndAnneeAndEmploye(@Param("mois") Integer mois, @Param("annee") Integer annee, @Param("employe") Employe employe);
}