package com.example.demo.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Aliments;

public interface AlimentsRepository extends JpaRepository<Aliments, Integer> {
    List<Aliments> findByNomContainingIgnoreCaseOrderByNomAsc(String nom);

    List<Aliments> findAllByOrderByNomAsc();

    @Query(value = """
            SELECT
                a.id AS id,
                a.nom AS nom,
                a.unite AS unite,
                COALESCE(a.seuilstockmin, 0) AS seuilStockMin,
                COALESCE(SUM(
                    CASE
                        WHEN LOWER(tm.libelle) = 'approvisionnement' THEN mf.quantite
                        WHEN LOWER(tm.libelle) = 'consommation' THEN -mf.quantite
                        ELSE 0
                    END
                ), 0) AS stock
            FROM aliments a
            LEFT JOIN mouvementstockfille mf ON a.id = mf.alimentid
            LEFT JOIN mouvementsstock m ON mf.mouvementstockid = m.id
            LEFT JOIN typemouvement tm ON m.typeid = tm.id
            WHERE (:q IS NULL OR a.nom ILIKE CONCAT('%', CAST(:q AS text), '%'))
            GROUP BY a.id, a.nom, a.unite, a.seuilstockmin
            ORDER BY a.nom
            """, nativeQuery = true)
    List<StockActuelProjection> findStockActuel(@Param("q") String q);

    @Query(value = """
            SELECT
                a.id AS id,
                a.nom AS nom,
                a.unite AS unite,
                COALESCE(a.seuilstockmin, 0) AS seuilStockMin,
                COALESCE(SUM(
                    CASE
                        WHEN LOWER(tm.libelle) = 'approvisionnement' THEN mf.quantite
                        WHEN LOWER(tm.libelle) = 'consommation' THEN -mf.quantite
                        ELSE 0
                    END
                ), 0) AS stock
            FROM aliments a
            LEFT JOIN mouvementstockfille mf ON a.id = mf.alimentid
            LEFT JOIN mouvementsstock m ON mf.mouvementstockid = m.id
                AND m.datemvt BETWEEN COALESCE(CAST(:du AS timestamp), TIMESTAMP '1900-01-01 00:00:00')
                AND COALESCE(CAST(:au AS timestamp), TIMESTAMP '2999-12-31 23:59:59')
            LEFT JOIN typemouvement tm ON m.typeid = tm.id
            WHERE (:q IS NULL OR a.nom ILIKE CONCAT('%', CAST(:q AS text), '%'))
            GROUP BY a.id, a.nom, a.unite, a.seuilstockmin
            ORDER BY a.nom
            """, nativeQuery = true)
    List<StockActuelProjection> findStockActuelByPeriod(
            @Param("q") String q,
            @Param("du") Timestamp du,
            @Param("au") Timestamp au);

    @Query(value = """
            SELECT
                a.id AS id,
                a.nom AS nom,
                a.unite AS unite,
                COALESCE(a.seuilstockmin, 0) AS seuilStockMin,
                COALESCE(SUM(
                    CASE
                        WHEN LOWER(tm.libelle) = 'approvisionnement' THEN mf.quantite
                        WHEN LOWER(tm.libelle) = 'consommation' THEN -mf.quantite
                        ELSE 0
                    END
                ), 0) AS stock
            FROM aliments a
            LEFT JOIN mouvementstockfille mf ON a.id = mf.alimentid
            LEFT JOIN mouvementsstock m ON mf.mouvementstockid = m.id
                AND m.datemvt <= COALESCE(CAST(:dateEtat AS timestamp), TIMESTAMP '2999-12-31 23:59:59')
            LEFT JOIN typemouvement tm ON m.typeid = tm.id
            WHERE (:q IS NULL OR a.nom ILIKE CONCAT('%', CAST(:q AS text), '%'))
            GROUP BY a.id, a.nom, a.unite, a.seuilstockmin
            ORDER BY a.nom
            """, nativeQuery = true)
    List<StockActuelProjection> findStockActuelByDate(@Param("q") String q, @Param("dateEtat") Timestamp dateEtat);

    @Query(value = """
            SELECT
                a.id AS id,
                a.nom AS nom,
                a.unite AS unite,
                COALESCE(a.seuilstockmin, 0) AS seuilStockMin,
                COALESCE(SUM(
                    CASE
                        WHEN LOWER(tm.libelle) = 'approvisionnement' THEN mf.quantite
                        WHEN LOWER(tm.libelle) = 'consommation' THEN -mf.quantite
                        ELSE 0
                    END
                ), 0) AS stock
            FROM aliments a
            LEFT JOIN mouvementstockfille mf ON a.id = mf.alimentid
            LEFT JOIN mouvementsstock m ON mf.mouvementstockid = m.id
            LEFT JOIN typemouvement tm ON m.typeid = tm.id
            GROUP BY a.id, a.nom, a.unite, a.seuilstockmin
            HAVING COALESCE(SUM(
                CASE
                    WHEN LOWER(tm.libelle) = 'approvisionnement' THEN mf.quantite
                    WHEN LOWER(tm.libelle) = 'consommation' THEN -mf.quantite
                    ELSE 0
                END
            ), 0) <= COALESCE(a.seuilstockmin, 0)
            ORDER BY a.nom
            """, nativeQuery = true)
    List<StockActuelProjection> findAlertesStockBas();
}
