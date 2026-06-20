package com.example.demo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.CycleBovin;
import com.example.demo.repository.CycleBovinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import java.util.Map;

@RestController
public class CycleBovinController {
    @Autowired
    private CycleBovinRepository cycleBovinRepository;

@GetMapping("/api/cycles/lactation")
public List<Map<String, Object>> getLactationsParDate(@RequestParam("date") String dateStr) {
    LocalDate date = LocalDate.parse(dateStr);
    return cycleBovinRepository.findLactationsByDate(date);
}
}