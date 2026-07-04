package com.academia.batch.controller;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import com.academia.batch.service.EstudianteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;
    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteRepository estudianteRepository,
                                EstudianteService estudianteService) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteService = estudianteService;
    }

    // GET /api/estudiantes → lista todos
    @GetMapping
    public ResponseEntity<List<EstudianteEntity>> listarTodos() {
        return ResponseEntity.ok(estudianteRepository.findAll());
    }

    // GET /api/estudiantes/{id} → 200 o 404
    @GetMapping("/{id}")
    public ResponseEntity<EstudianteEntity> obtenerPorId(@PathVariable Long id) {
        Optional<EstudianteEntity> estudiante = estudianteRepository.findById(id);
        return estudiante.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/estudiantes/aprobados/total → Map con el conteo
    @GetMapping("/aprobados/total")
    public ResponseEntity<Map<String, Long>> totalAprobados() {
        long total = estudianteService.contarAprobados();
        return ResponseEntity.ok(Map.of("aprobados", total));
    }

    // POST /api/estudiantes → crea, 201 Created
    @PostMapping
    public ResponseEntity<EstudianteEntity> crear(@RequestBody EstudianteEntity estudiante) {
        EstudianteEntity guardado = estudianteRepository.save(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT /api/estudiantes/{id} → reemplaza, 200 o 404
    @PutMapping("/{id}")
    public ResponseEntity<EstudianteEntity> reemplazar(@PathVariable Long id,
                                                       @RequestBody EstudianteEntity datos) {
        if (!estudianteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        datos.setId(id);
        return ResponseEntity.ok(estudianteRepository.save(datos));
    }

    // PATCH /api/estudiantes/{id} → cambia solo el grupo, 200 o 404
    @PatchMapping("/{id}")
    public ResponseEntity<EstudianteEntity> actualizarGrupo(@PathVariable Long id,
                                                            @RequestBody Map<String, String> campos) {
        Optional<EstudianteEntity> optional = estudianteRepository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        EstudianteEntity estudiante = optional.get();
        if (campos.containsKey("grupo")) {
            estudiante.setGrupo(campos.get("grupo"));
        }
        return ResponseEntity.ok(estudianteRepository.save(estudiante));
    }

    // DELETE /api/estudiantes/{id} → 204 o 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!estudianteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        estudianteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
