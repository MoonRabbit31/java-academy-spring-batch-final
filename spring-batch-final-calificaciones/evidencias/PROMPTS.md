# Evidencia de uso de GitHub Copilot — <Tu Nombre>
## Prompt 1 — pom.xml
- **Prompt:** Este archivo pom.xml adaptalo para un proyecto de Spring Boot 3.2.2 con Java 21 y estas dependencias:
- **Modalidad:** Chat
- **Resultado:** aceptado 
el IDE me genera el pom.xml, asi que me puse al nivel del archivo y sólo modifiqué la parte inicial del prompt para que escribiera sobre el pom, de lo contrario creo el agente hubiera tenido que hacer un trabajo extra que es buscar el archivo en el proyecto mismo que encontraría que ya existe, así que atomicé lo más que pude su trabajo además de cambiar la version de Java (yo tengo 21). 

## Punto 1.6 - ReporteEstudianteProcessor
El generador automático no tuvo errores en > 70, se dejó tal cual lo propuso (>=)

## Prompt 2 — Batch Config
- **Prompt:** Sin modificaciones al dado por mi instructor
- **Modalidad:** Chat y autocompletado en algunas secciones una vez identificados los errores
- **Resultado:** El resultado realizado por copilot fue bueno en el sentido de disminuir la escritura de código, sin embargo, cometió algunos errores los cuales tuve que intervenir (dentro del archivo comenté lo generado por copilot y dejé la versión corregida por mi y con ayuda del autocomplete). Tuvo 2 errores cruciales, uno en el tipo de generic para leer la BD y otro en el import de mongo writer que sólo pude ver comparando con el mismo batch config del proyecto pasado. Además la escritura del código puede ser reducida aún más para ser mas functional y mejorarlo a la vista con el mismo funcionamiento, solo corregí algunas y deje otras clases tal cual las dió la IA, ya que, aún no domino lo suficiente estos temas para mover algo que no sé como funciona.

## Prompt 3 - application.properties
- **Prompt:** Desarrolla un archivo 'application.properties' en el nivel de la carpeta resources del proyecto (src/main/resources/); Para SpringBoot que se conecte a MySQL en jdbc:mysql://localhost:3306/academia (usuario alumno, password alumno123), inicialice el esquema de Spring
Batch siempre, ejecute el Job al arrancar, y se conecte a MongoDB en
mongodb://root:root123@localhost:27018/academia?authSource=admin.
- Prompt por chat, modifiqué el principio de este al dado por el instructor, para ser un poco más específico en la tarea a desarrollar por el modelo, ya que estoy usando Haiku 4.5, es uno que no utiliza tantos recursos para generación de código a diferencia de Sonnet 4.6 el cual tal vez me hubiese servido en el batch config más que el haiku, pero lo que busco en este proyecto (así como aprendí en mi experiencia) es no usar modelos potentes si no alguno que en efecto me ayude a la generación de código pero yo debo pensar y revisar más los resultados que da.

## Prompt 4 — Entity estudiante
- **Prompt:** Desarrolla una Entidad JPA (@Entity, @Table name="estudiantes_procesados") que mapea la tabla existente. id Long con @Id y @GeneratedValue(IDENTITY); campos: nombre, grupo, nota1, nota2, nota3, promedio; getters y setters de cada campo.
- **Resultado:** Copilot no autogeneraba así que usé el chat, el resultado fue óptimo.

## Prompt 5 — Controlador de Estudiante
-  Prompt: Genera en este archivo un @RestController que use
EstudianteRepository y EstudianteService ...
- Modalidad: Chat, el prompt inicial fue modificado únicamente en la parte inicial, donde se le indica únicamente qué hacer, ya que me encontraba al nivel de archivo que quería modificar.
- Resultado: Satisfactorio, no veo algún error, manda el código HTTP correcto 

## Situación 5 - Reporte controller
- El autocomplete no se equivocó en la escritura del código, sin embargo el tipo de generic era invalido, asi que viendo como funcionaba el reporte en el repository, me di cuenta del error y lo corregí.

## Test 1
- Comando: /tests y prompt dado por el instructor
- El comando generó el test correctamente de Estudiante porque solo le di ese archivo, me faltó el segundo test de Reporte estudiante el cual lo generó dandole el prompt subsecuente al comando. Yo creo es mejor el promtp y darle correctamente cuales test queremos que genere, cómo y donde guardarlo, corrieron sin errores.

## Test 2
- Prompt: Genera una prueba unitaria de EstudianteService con Mockito: mockea EstudianteRepository con
@Mock, inyecta el servicio con @InjectMocks, usa @ExtendWith(MockitoExtension.class), simula findAll()
devolviendo 2 estudiantes aprobados y 1 reprobado, y verifica que contarAprobados() devuelve 2. Guárdalo en src/test/java/com/academia/batch/service/EstudianteServiceTest.java, no la ejecutes.

- Junté todo lo solicitado, así hizo todo correctamente a la primera (guardo en donde se debia), tests en verde.

## Bug en EstudianteProcessor
- Respuesta (encontró el bug): 
Bug Found
The average calculation is dividing by 2 instead of 3 (the number of grades).

Fix: Changed the divisor from 2 to 3.0.

Using 3.0 (instead of integer 3) ensures floating-point division, preventing potential integer truncation if the sum were ever computed as an integer.

## PROMPT BUENO/MALO
- Resultados: 
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
- Ambos prompts me dieron exactamente el mismo resultado, sin embargo considero mejor uno más detallado, en este caso al ser poco contexto lo entendió, algo más especifico con el 1er prompt no podría resolverlo o lo haría mal.

