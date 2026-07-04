package com.academia.batch.service;

import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @InjectMocks
    private EstudianteService estudianteService;

    @Test
    void contarAprobados_debeRetornarDos_cuandoHayDosAprobadosYUnReprobado() {
        EstudianteEntity aprobado1 = new EstudianteEntity("Ana", "A", 80.0, 75.0, 90.0, 81.67);
        EstudianteEntity aprobado2 = new EstudianteEntity("Luis", "B", 70.0, 72.0, 68.0, 70.0);
        EstudianteEntity reprobado = new EstudianteEntity("Pedro", "A", 50.0, 60.0, 55.0, 55.0);

        when(estudianteRepository.findAll()).thenReturn(List.of(aprobado1, aprobado2, reprobado));

        long resultado = estudianteService.contarAprobados();

        assertEquals(2, resultado);
    }
}
