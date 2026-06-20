package com.example.demo.service;

import com.example.demo.entity.Clients;
import com.example.demo.repository.ClientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Clients> getClients(String nom, String dateStr) {
        if ((nom != null && !nom.trim().isEmpty()) || (dateStr != null && !dateStr.trim().isEmpty())) {
            return clientRepository.searchClients(nom, dateStr);
        }
        return clientRepository.findAll();
    }

    public Clients getClientById(Integer id) {
        return clientRepository.findById(id).orElse(new Clients());
    }

    public void saveClient(Clients client) {
        clientRepository.save(client);
    }
    
}
