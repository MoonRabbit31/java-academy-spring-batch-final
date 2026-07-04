package com.academia.batch.repository;

// Interfaz ReporteRepository que extiende MongoRepository<EstudianteReporte, String>
// con un metodo findByEstado(String estado) que devuelve List<EstudianteReporte>.
//Autocomplete
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

//Tuve que importar la clase EstudianteReporte desde el paquete com.academia.batch.model para que el código funcione además del List
import com.academia.batch.model.EstudianteReporte;
import java.util.List;

@Repository
public interface ReporteRepository extends MongoRepository<EstudianteReporte, String> {
    List<EstudianteReporte> findByEstado(String estado);
}