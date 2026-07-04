package com.academia.batch.processor;
// Processor que implementa ItemProcessor<Estudiante, EstudianteReporte>.
// Convierte un Estudiante en un EstudianteReporte copiando nombre, grupo y promedio,
// y asigna estado "APROBADO" si el promedio es >= 70, o "REPROBADO" si es menor.
// Loguea "Step 2 - Reporte: {reporte}" y devuelve el reporte.

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;

public class ReporteEstudianteProcessor implements ItemProcessor<Estudiante, EstudianteReporte> {

    private static final Logger logger = LoggerFactory.getLogger(ReporteEstudianteProcessor.class);

    @Override
    public EstudianteReporte process(Estudiante estudiante) throws Exception {
        EstudianteReporte reporte = new EstudianteReporte();
        reporte.setNombre(estudiante.getNombre());
        reporte.setGrupo(estudiante.getGrupo());
        reporte.setPromedio(estudiante.getPromedio());
        reporte.setEstado(estudiante.getPromedio() >= 70 ? "APROBADO" : "REPROBADO");
        logger.info("Step 2 - Reporte: {}", reporte);
        return reporte;
    }
}
