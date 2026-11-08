package com.escuela.repository;

import com.escuela.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    // Estos métodos le enseñan a la base de datos a filtrar
    List<Alumno> findByProfesorAndGrupo(String profesor, String grupo);
    List<Alumno> findByProfesor(String profesor);
    List<Alumno> findByGrupo(String grupo);
}