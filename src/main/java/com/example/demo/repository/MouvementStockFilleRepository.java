package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.MouvementStockFille;

public interface MouvementStockFilleRepository extends JpaRepository<MouvementStockFille, Integer> {

    @Query("""
            SELECT f
            FROM MouvementStockFille f
            JOIN FETCH f.mouvementStock m
            JOIN FETCH f.aliment a
            LEFT JOIN FETCH m.type t
            LEFT JOIN FETCH m.fournisseur fo
            WHERE (:typeLibelle IS NULL OR LOWER(t.libelle) = LOWER(:typeLibelle))
              AND (:alimentId IS NULL OR a.id = :alimentId)
              AND (:fournisseurId IS NULL OR fo.id = :fournisseurId)
                                                        AND m.dateMvt >= :dateDebut
                                                        AND m.dateMvt <= :dateFin
                    AND (:q IS NULL OR a.nom ILIKE CONCAT('%', CAST(:q AS string), '%')
                            OR COALESCE(fo.nom, '') ILIKE CONCAT('%', CAST(:q AS string), '%')
                            OR COALESCE(m.motif, '') ILIKE CONCAT('%', CAST(:q AS string), '%'))
            ORDER BY m.dateMvt DESC, f.id DESC
            """)
    List<MouvementStockFille> rechercher(
            @Param("typeLibelle") String typeLibelle,
            @Param("dateDebut") Timestamp dateDebut,
            @Param("dateFin") Timestamp dateFin,
            @Param("alimentId") Integer alimentId,
            @Param("fournisseurId") Integer fournisseurId,
            @Param("q") String q);

    List<MouvementStockFille> findByMouvementStockId(Integer mouvementStockId);
}
