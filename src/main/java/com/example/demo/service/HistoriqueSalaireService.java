
package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.HistoriqueSalaire;
import com.example.demo.repository.HistoriqueSalaireRepository;


@Service
public class HistoriqueSalaireService {

    private final HistoriqueSalaireRepository HistoriqueSalaireRepository;

    public HistoriqueSalaireService(HistoriqueSalaireRepository HistoriqueSalaireRepository){
        this.HistoriqueSalaireRepository = HistoriqueSalaireRepository;
    }

    public List<HistoriqueSalaire> getAllHistoriqueSalaire(){
        return HistoriqueSalaireRepository.findAll();
    }

    public HistoriqueSalaire findFirstByEmployeIdOrderByDateDebutDesc(Integer employeId) {
        return HistoriqueSalaireRepository.findFirstByEmployeIdOrderByDateDebutDesc(employeId);
    }

}
