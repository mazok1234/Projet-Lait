package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MouvementsStock;

public interface MouvementsStockRepository extends JpaRepository<MouvementsStock, Integer> {

    @Query("""
            SELECT m
            FROM MouvementsStock m
            LEFT JOIN FETCH m.type
            LEFT JOIN FETCH m.fournisseur
            WHERE (:typeId IS NULL OR m.type.id = :typeId)
                                                        AND m.dateMvt >= :dateDebut
                                                        AND m.dateMvt <= :dateFin
            ORDER BY m.dateMvt DESC
            """)
    List<MouvementsStock> rechercher(
            @Param("dateDebut") Timestamp dateDebut,
            @Param("dateFin") Timestamp dateFin,
            @Param("typeId") Integer typeId);
}
