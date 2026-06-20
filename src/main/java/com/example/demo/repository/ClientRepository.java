package com.example.demo.repository;

import com.example.demo.entity.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ClientRepository extends JpaRepository<Clients, Integer> {

    // Recherche combinée : par nom (contient) ET/OU par date de création (convertie en texte pour plus de flexibilité)
    @Query("SELECT c FROM Clients c WHERE " +
           "(:nom IS NULL OR LOWER(c.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) AND " +
           "(:dateStr IS NULL OR CAST(c.dateCreation AS string) LIKE CONCAT('%', :dateStr, '%'))")
    List<Clients> searchClients(@Param("nom") String nom, @Param("dateStr") String dateStr);
}