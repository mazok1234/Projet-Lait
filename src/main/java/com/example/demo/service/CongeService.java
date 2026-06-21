package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Conge;
import com.example.demo.repository.CongeRepository;

@Service
public class CongeService {

    private final CongeRepository congeRepository;

    public CongeService(CongeRepository congeRepository) {
        this.congeRepository = congeRepository;
    }

    public List<Conge> getCongesByEmploye(Integer employeId) {
        return congeRepository.findByEmployeIdOrderByDateDemandeDesc(employeId);
    }
}