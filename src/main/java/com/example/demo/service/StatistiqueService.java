package com.example.demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class StatistiqueService {

    @PersistenceContext
    private EntityManager entityManager;

    public Map<String, BigDecimal> getStats(Integer jour, Integer mois, Integer annee) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ")
           .append("    COALESCE(SUM(CASE WHEN tt.libelle = 'recette' THEN t.montant ELSE 0 END), 0) AS recettes, ")
           .append("    COALESCE(SUM(CASE WHEN tt.libelle = 'depense' THEN t.montant ELSE 0 END), 0) AS depenses ")
           .append("FROM Transactions t ")
           .append("JOIN TypesTransaction tt ON t.typeid = tt.id ")
           .append("WHERE 1=1 ");

        if (jour != null) {
            sql.append(" AND EXTRACT(DAY FROM t.datetrans) = :jour");
        }
        if (mois != null) {
            sql.append(" AND EXTRACT(MONTH FROM t.datetrans) = :mois");
        }
        if (annee != null) {
            sql.append(" AND EXTRACT(YEAR FROM t.datetrans) = :annee");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (jour != null) query.setParameter("jour", jour);
        if (mois != null) query.setParameter("mois", mois);
        if (annee != null) query.setParameter("annee", annee);

        Object[] result = (Object[]) query.getSingleResult();

        Map<String, BigDecimal> stats = new HashMap<>();
        stats.put("recettes", (BigDecimal) result[0]);
        stats.put("depenses", (BigDecimal) result[1]);
        stats.put("benefice", ((BigDecimal) result[0]).subtract((BigDecimal) result[1]));

        return stats;
    }
}