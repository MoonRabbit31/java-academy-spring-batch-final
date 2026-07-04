package com.academia.batch.service;

import org.springframework.stereotype.Service;
import com.academia.batch.repository.EstudianteEntity;
import com.academia.batch.repository.EstudianteRepository;

/**
 * Servicio de negocio para la gestion de estudiantes.
 *
 * <p>Proporciona operaciones sobre los datos de estudiantes almacenados
 * en el repositorio, como el calculo de estadisticas de aprobacion.</p>
 */
@Service
public class EstudianteService {

    private static final int NOTA_MINIMA_APROBACION = 70;

    private final EstudianteRepository estudianteRepository;

    /**
     * Crea una nueva instancia de {@code EstudianteService}.
     *
     * @param estudianteRepository repositorio de acceso a datos de estudiantes
     */
    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * Cuenta el numero de estudiantes aprobados.
     *
     * <p>Un estudiante se considera aprobado cuando su promedio es mayor o
     * igual a 70. El calculo se realiza sobre todos los registros devueltos
     * por {@link EstudianteRepository#findAll()}.</p>
     *
     * @return cantidad de estudiantes con promedio {@code >= 70}
     */
    public long contarAprobados() {
        return estudianteRepository.findAll().stream()
                .filter(this::esAprobado)
                .count();
    }

    private boolean esAprobado(EstudianteEntity estudiante) {
        return estudiante.getPromedio() >= NOTA_MINIMA_APROBACION;
    }

    /** PROMPT MALO
     * Cuenta el numero de estudiantes reprobados.
     *
     * <p>Un estudiante se considera reprobado cuando su promedio es menor
     * a 70. El calculo se realiza sobre todos los registros devueltos
     * por {@link EstudianteRepository#findAll()}.</p>
     *
     * @return cantidad de estudiantes con promedio {@code < 70}
     */
    public long contarReprobados() {
        return estudianteRepository.findAll().stream()
                .filter(estudiante -> estudiante.getPromedio() < 70)
                .count();
    }

    //PROMPT BUENO
    public long contarReprobados2() {
        return estudianteRepository.findAll().stream()
                .filter(estudiante -> estudiante.getPromedio() < 70)
                .count();
    }
}

