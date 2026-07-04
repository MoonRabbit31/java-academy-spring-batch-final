package com.academia.batch.processor;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReporteEstudianteProcessorTest {

    private ReporteEstudianteProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new ReporteEstudianteProcessor();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Estudiante estudianteConPromedio(String nombre, String grupo, double promedio) {
        Estudiante e = new Estudiante();
        e.setNombre(nombre);
        e.setGrupo(grupo);
        e.setPromedio(promedio);
        return e;
    }

    // ── Estado: APROBADO / REPROBADO ─────────────────────────────────────────

    @Test
    void process_conPromedio70_retornaEstadoAprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Ana", "A1", 70.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("APROBADO", reporte.getEstado());
    }

    @Test
    void process_conPromedio69punto9_retornaEstadoReprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Luis", "B2", 69.9);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("REPROBADO", reporte.getEstado());
    }

    @Test
    void process_conPromedioAlto_retornaEstadoAprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Maria", "A1", 95.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("APROBADO", reporte.getEstado());
    }

    @Test
    void process_conPromedioBajo_retornaEstadoReprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Pedro", "C3", 50.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("REPROBADO", reporte.getEstado());
    }

    @Test
    void process_conPromedioCero_retornaEstadoReprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Carlos", "D4", 0.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("REPROBADO", reporte.getEstado());
    }

    @Test
    void process_conPromedio100_retornaEstadoAprobado() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Sofia", "A1", 100.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("APROBADO", reporte.getEstado());
    }

    // ── Copia de campos ──────────────────────────────────────────────────────

    @Test
    void process_copiaPromedioDeEstudianteAlReporte() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Ana", "A1", 85.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals(85.0, reporte.getPromedio(), 0.001);
    }

    @Test
    void process_copiaNombreDeEstudianteAlReporte() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Juan", "B2", 75.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("Juan", reporte.getNombre());
    }

    @Test
    void process_copiaGrupoDeEstudianteAlReporte() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Laura", "G3", 80.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertEquals("G3", reporte.getGrupo());
    }

    @Test
    void process_noRetornaNull() throws Exception {
        Estudiante estudiante = estudianteConPromedio("Test", "X1", 70.0);

        EstudianteReporte reporte = processor.process(estudiante);

        assertNotNull(reporte);
    }
}
