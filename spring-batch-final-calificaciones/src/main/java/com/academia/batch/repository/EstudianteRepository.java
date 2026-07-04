package com.academia.batch.repository;

import java.util.List;

// Interfaz EstudianteRepository que extiende JpaRepository<EstudianteEntity, Long>
// con un metodo findByGrupo(String grupo) que devuelve List<EstudianteEntity>.

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Long> {
    List<EstudianteEntity> findByGrupo(String grupo);
}