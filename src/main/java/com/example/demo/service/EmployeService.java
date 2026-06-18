package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employe;
import com.example.demo.repository.EmployeRepository;


@Service
public class EmployeService {

    private final EmployeRepository EmployeRepository;

    public EmployeService(EmployeRepository EmployeRepository){
        this.EmployeRepository = EmployeRepository;
    }

    public List<Employe> getAllEmploye(){
        return EmployeRepository.findAll();
    }

    public Employe getEmployeById(Integer id) {
        return EmployeRepository.findById(id).orElse(null);
    }

}
