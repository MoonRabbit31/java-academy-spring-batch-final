package com.academia.batch.processor;

import com.academia.batch.model.Estudiante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstudianteProcessorTest {

    private EstudianteProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new EstudianteProcessor();
    }

    @Test
    void process_calculatesCorrectAverage() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(7.0);
        estudiante.setNota2(8.0);
        estudiante.setNota3(9.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(8.0, result.getPromedio(), 0.001);
    }

    @Test
    void process_returnsTheSameObject() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(5.0);
        estudiante.setNota2(6.0);
        estudiante.setNota3(7.0);

        Estudiante result = processor.process(estudiante);

        assertSame(estudiante, result);
    }

    @Test
    void process_doesNotReturnNull() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(1.0);
        estudiante.setNota2(2.0);
        estudiante.setNota3(3.0);

        Estudiante result = processor.process(estudiante);

        assertNotNull(result);
    }

    @Test
    void process_withAllZeroNotes_returnsZeroAverage() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(0.0);
        estudiante.setNota2(0.0);
        estudiante.setNota3(0.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(0.0, result.getPromedio(), 0.001);
    }

    @Test
    void process_withAllEqualNotes_returnsTheSameValue() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(5.0);
        estudiante.setNota2(5.0);
        estudiante.setNota3(5.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(5.0, result.getPromedio(), 0.001);
    }

    @Test
    void process_withMaxNotes_returnsCorrectAverage() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(10.0);
        estudiante.setNota2(10.0);
        estudiante.setNota3(10.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(10.0, result.getPromedio(), 0.001);
    }

    @Test
    void process_withDecimalNotes_returnsCorrectAverage() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(7.5);
        estudiante.setNota2(8.5);
        estudiante.setNota3(6.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(7.333, result.getPromedio(), 0.001);
    }

    @Test
    void process_withOneNotePerfect_calculatesCorrectly() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(10.0);
        estudiante.setNota2(4.0);
        estudiante.setNota3(4.0);

        Estudiante result = processor.process(estudiante);

        assertEquals(6.0, result.getPromedio(), 0.001);
    }

    @Test
    void process_setsPromedioOnOriginalObject() throws Exception {
        Estudiante estudiante = new Estudiante();
        estudiante.setNota1(3.0);
        estudiante.setNota2(6.0);
        estudiante.setNota3(9.0);

        processor.process(estudiante);

        assertEquals(6.0, estudiante.getPromedio(), 0.001);
    }
}