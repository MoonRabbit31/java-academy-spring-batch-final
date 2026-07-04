package com.academia.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
//import arreglado
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
/* Aqui se equivocó en el import, me marcaba error y no sabia por qué
hasta que comparé el import con el archivo de la semana pasada, 
hay incluso imports que en la versión final no se utilizaron, no los borraré para dejar como evidencia.
*/
//import org.springframework.batch.item.mongo.MongoItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import com.academia.batch.model.Estudiante;
import com.academia.batch.model.EstudianteReporte;
import com.academia.batch.processor.EstudianteProcessor;
import com.academia.batch.processor.ReporteEstudianteProcessor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class BatchConfig {

    //Autowired dados por la IA
    // @Autowired
    // private JobRepository jobRepository;

    // @Autowired
    // private PlatformTransactionManager platformTransactionManager;

    // @Autowired
    // private DataSource dataSource;

    // @Autowired
    // private MongoTemplate mongoTemplate;

    // ==================== READERS ====================

    /**
     * Lector de archivo plano (CSV) para estudiantes
     */
    @Bean
    public FlatFileItemReader<Estudiante> estudianteReader() {
        FlatFileItemReader<Estudiante> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("estudiantes.csv"));
        reader.setLinesToSkip(1); // Salta la línea de encabezado
        
        DefaultLineMapper<Estudiante> lineMapper = new DefaultLineMapper<>();
        
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("nombre", "grupo", "nota1", "nota2", "nota3");
        tokenizer.setDelimiter(",");
        
        BeanWrapperFieldSetMapper<Estudiante> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Estudiante.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        
        reader.setLineMapper(lineMapper);
        return reader;
    }

    /**
     * Lector JDBC para consulta de estudiantes procesados desde MySQL
     */
    // @Bean
    // public JdbcCursorItemReader<EstudianteReporte> estudianteProcessadoReader(DataSource dataSource) {
    //     JdbcCursorItemReader<EstudianteReporte> reader = new JdbcCursorItemReader<>();
    //     reader.setDataSource(dataSource);
    //     reader.setSql("SELECT nombre, grupo, promedio FROM estudiantes_procesados");
    //     reader.setRowMapper(new RowMapper<EstudianteReporte>() {
    //         @Override
    //         public EstudianteReporte mapRow(ResultSet rs, int rowNum) throws SQLException {
    //             EstudianteReporte reporte = new EstudianteReporte();
    //             reporte.setNombre(rs.getString("nombre"));
    //             reporte.setGrupo(rs.getString("grupo"));
    //             reporte.setPromedio(rs.getDouble("promedio"));
    //             return reporte;
    //         }
    //     });
    //     return reader;
    // }
    // Aqui hubo un error de copilot en el tipo de generico, comparando con el archivo de la semana pasada,
    // se debe mandar un tipo Estudiante y no EstudianteReporte, ya que el reader lee de la tabla estudiantes_procesados
    // tambien considero puede haber una mejora en la escritura pero no me quiero meter en ello
    @Bean
    public JdbcCursorItemReader<Estudiante> estudianteProcessadoReader(DataSource dataSource) {
        JdbcCursorItemReader<Estudiante> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT nombre, grupo, promedio FROM estudiantes_procesados");
        reader.setRowMapper(new RowMapper<Estudiante>() {
            @Override
            public Estudiante mapRow(ResultSet rs, int rowNum) throws SQLException {
                Estudiante estudiante = new Estudiante();
                estudiante.setNombre(rs.getString("nombre"));
                estudiante.setGrupo(rs.getString("grupo"));
                estudiante.setPromedio(rs.getDouble("promedio"));
                return estudiante;
            }
        });
        return reader;
    }

    // ==================== WRITERS ====================

    /**
     * Escritor JDBC para insertar estudiantes procesados en MySQL
     */
    @Bean
    public JdbcBatchItemWriter<Estudiante> estudianteWriter(DataSource dataSource) {
        JdbcBatchItemWriter<Estudiante> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO estudiantes_procesados (nombre, grupo, nota1, nota2, nota3, promedio) " +
                      "VALUES (:nombre, :grupo, :nota1, :nota2, :nota3, :promedio)");
        writer.setItemSqlParameterSourceProvider(new org.springframework.batch.item.database.ItemSqlParameterSourceProvider<Estudiante>() {
            @Override
            public org.springframework.jdbc.core.namedparam.SqlParameterSource createSqlParameterSource(Estudiante item) {
                org.springframework.jdbc.core.namedparam.MapSqlParameterSource parameterSource = 
                    new org.springframework.jdbc.core.namedparam.MapSqlParameterSource();
                parameterSource.addValue("nombre", item.getNombre());
                parameterSource.addValue("grupo", item.getGrupo());
                parameterSource.addValue("nota1", item.getNota1());
                parameterSource.addValue("nota2", item.getNota2());
                parameterSource.addValue("nota3", item.getNota3());
                parameterSource.addValue("promedio", item.getPromedio());
                return parameterSource;
            }
        });
        return writer;
    }

    /**
     * Escritor MongoDB para los reportes de estudiantes
     */
    @Bean
    public MongoItemWriter<EstudianteReporte> reporteEstudianteWriter(MongoTemplate mongoTemplate) {
        /* MongoItemWriter<EstudianteReporte> writer = new MongoItemWriter<>();
        writer.setTemplate(mongoTemplate);
        writer.setCollection("reportes_estudiantes"); 
        
        Copilot hizo lo anterior, no encontré errores de codigo pero considero
        se puede mejorar la escritura */
        return new MongoItemWriterBuilder<EstudianteReporte>()
                .template(mongoTemplate)
                .collection("reportes_estudiantes")
                .build();
    }

    // ==================== PROCESSORS ====================

    @Bean
    public EstudianteProcessor estudianteProcessor() {
        return new EstudianteProcessor();
    }

    @Bean
    public ReporteEstudianteProcessor reporteEstudianteProcessor() {
        return new ReporteEstudianteProcessor();
    }

    // ==================== STEPS ====================

    /**
     * Paso 1: Lee CSV, procesa y escribe en MySQL
     */
    //Aqui copilot tuvo un error, no se puede inyectar jobRepository y platformTransactionManager en el metodo paso1, ya que no son beans, por lo que se debe inyectar en la clase BatchConfig y luego usarlo en el metodo paso1
    // @Bean
    // public Step paso1() {
    //     return new StepBuilder("paso1", jobRepository)
    //             .<Estudiante, Estudiante>chunk(3, platformTransactionManager)
    //             .reader(estudianteReader())
    //             .processor(estudianteProcessor())
    //             .writer(estudianteWriter())
    //             .build();
    // }
    @Bean
    public Step paso1(JobRepository jobRepository, 
        PlatformTransactionManager platformTransactionManager,
        FlatFileItemReader<Estudiante> estudianteReader,
        EstudianteProcessor estudianteProcessor, 
        JdbcBatchItemWriter<Estudiante> estudianteWriter) {
         return new StepBuilder("paso1", jobRepository)
                 .<Estudiante, Estudiante>chunk(3, platformTransactionManager)
                 .reader(estudianteReader)
                 .processor(estudianteProcessor)
                 .writer(estudianteWriter)
                 .build();
     }

    /**
     * Paso 2: Lee de MySQL, procesa y escribe en MongoDB
     */
    // @Bean
    // public Step paso2() {
    //     return new StepBuilder("paso2", jobRepository)
    //             .<EstudianteReporte, EstudianteReporte>chunk(3, platformTransactionManager)
    //             .reader(estudianteProcessadoReader())
    //             .processor(reporteEstudianteProcessor())
    //             .writer(reporteEstudianteWriter())
    //             .build();
    // }
     @Bean
     public Step paso2(JobRepository jobRepository, 
            PlatformTransactionManager platformTransactionManager,
            JdbcCursorItemReader<Estudiante> estudianteProcessadoReader,
            ReporteEstudianteProcessor reporteEstudianteProcessor,
            MongoItemWriter<EstudianteReporte> reporteEstudianteWriter) {
         return new StepBuilder("paso2", jobRepository)
                 .<Estudiante, EstudianteReporte>chunk(3, platformTransactionManager)
                 .reader(estudianteProcessadoReader)
                 .processor(reporteEstudianteProcessor)
                 .writer(reporteEstudianteWriter)
                 .build();
     }



    // ==================== JOB ====================

    /**
     * Job principal: ejecuta paso1 y paso2 secuencialmente
     */
    @Bean
    public Job procesarCalificacionesJob(JobRepository jobRepository, 
                                         PlatformTransactionManager platformTransactionManager,
                                         Step paso1,
                                         Step paso2) {
        return new JobBuilder("procesarCalificacionesJob", jobRepository)
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .start(paso1)
                .next(paso2)
                .build();
    }
}
