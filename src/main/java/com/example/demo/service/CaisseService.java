package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class CaisseService {

    @Autowired
    private TransactionRepository transactionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public BigDecimal getSoldeCaisse() {
        Query query = entityManager.createNativeQuery("SELECT BeneficeNet FROM BilanFinancier");
        Object result = query.getSingleResult();
        return result != null ? (BigDecimal) result : BigDecimal.ZERO;
    }

    public List<Transaction> getDernieresTransactions(int limit) {
        return transactionRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Transaction::getDateTrans).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public Page<Transaction> getTransactionsPaginees(int page, int size, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return transactionRepository.findAll(pageable);
    }
}
