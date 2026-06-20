package com.example.demo.repository;

public interface StockActuelProjection {
    Integer getId();

    String getNom();

    String getUnite();

    Double getSeuilStockMin();

    Double getStock();
}
