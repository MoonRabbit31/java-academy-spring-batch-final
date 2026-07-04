package com.academia.batch.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academia.batch.model.EstudianteReporte;
//import com.academia.batch.repository.ReporteEntity;
import com.academia.batch.repository.ReporteRepository;

// @RestController en /api/reportes que usa ReporteRepository:
// GET / lista todos los reportes; GET /estado/{estado} devuelve los que tengan ese estado
// (convertido a mayusculas) usando findByEstado.

@RestController
@RequestMapping("/api/reportes")

public class ReporteController {
    
    private final ReporteRepository reporteRepository;

    public ReporteController(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @GetMapping
    // public ResponseEntity<List<ReporteEntity>> listarTodos() {
    //     return ResponseEntity.ok(reporteRepository.findAll());
    // }

    //Error en los tipos dados por el autocomplete, tuve que cambiar ReporteEntity por EstudianteReporte para que el código funcione.
    public ResponseEntity<List<EstudianteReporte>> listarTodos() {
        return ResponseEntity.ok(reporteRepository.findAll());
    }

    @GetMapping("/estado/{estado}")
    // public ResponseEntity<List<ReporteEntity>> listarPorEstado(@PathVariable String estado) {
    //     List<ReporteEntity> reportes = reporteRepository.findByEstado(estado.toUpperCase());
    //     return ResponseEntity.ok(reportes);
    // }

    //Error en los tipos dados por el autocomplete, tuve que cambiar ReporteEntity por EstudianteReporte para que el código funcione.
    public ResponseEntity<List<EstudianteReporte>> listarPorEstado(@PathVariable String estado) {
        List<EstudianteReporte> reportes = reporteRepository.findByEstado(estado.toUpperCase());
        return ResponseEntity.ok(reportes);
    }
}
