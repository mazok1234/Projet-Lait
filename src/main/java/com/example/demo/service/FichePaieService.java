package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.List;

import com.example.demo.entity.FichePaie;
import com.example.demo.entity.Employe;
import com.example.demo.repository.FichePaieRepository;


@Service
public class FichePaieService {

    private final FichePaieRepository fichePaieRepository;

    public FichePaieService(FichePaieRepository fichePaieRepository){
        this.fichePaieRepository = fichePaieRepository;
    }

    public List<FichePaie> getAllFichePaie(){
        return fichePaieRepository.findAll();
    }

    public List<FichePaie> findByMoisAndAnneeAndEmploye(Integer mois, Integer annee, Integer idEmploye){
        return fichePaieRepository.findByMoisAndAnneeAndEmploye(mois, annee, idEmploye);
    }

}
