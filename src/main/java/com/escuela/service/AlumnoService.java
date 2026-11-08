package com.escuela.service;

import com.escuela.model.Alumno;
import com.escuela.repository.AlumnoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AlumnoService {
    
    private final AlumnoRepository repository;

    public AlumnoService(AlumnoRepository repository) {
        this.repository = repository;
    }

    public List<Alumno> listarTodos() { return repository.findAll(); }
    public Alumno guardar(Alumno alumno) { return repository.save(alumno); }
    public Alumno buscarPorId(Long id) { return repository.findById(id).orElse(null); }
    public void eliminar(Long id) { repository.deleteById(id); }

    public List<Alumno> filtrar(String profesor, String grupo) {
        boolean conProfesor = (profesor != null && !profesor.isEmpty());
        boolean conGrupo = (grupo != null && !grupo.isEmpty());
        if (conProfesor && conGrupo) return repository.findByProfesorAndGrupo(profesor, grupo);
        else if (conProfesor) return repository.findByProfesor(profesor);
        else if (conGrupo) return repository.findByGrupo(grupo);
        return listarTodos();
    }

    public Double calcularPromedioGrupo(List<Alumno> alumnos) {
        if (alumnos.isEmpty()) return 0.0;
        double sum = alumnos.stream().mapToDouble(Alumno::getCalificacionFinal).sum();
        return Math.round((sum / alumnos.size()) * 100.0) / 100.0;
    }

    // CLONAR GRUPO PARA EL SIGUIENTE NIVEL
    public void promoverGrupo(String profesor, String grupoActual, String nuevoGrupo, String nuevoTerm) {
        List<Alumno> estudiantesViejos = repository.findByProfesorAndGrupo(profesor, grupoActual);
        for (Alumno a : estudiantesViejos) {
            Alumno nuevo = new Alumno();
            nuevo.setNombre(a.getNombre());
            nuevo.setProfesor(a.getProfesor());
            nuevo.setHorario(a.getHorario());
            nuevo.setGrupo(nuevoGrupo);
            nuevo.setTerm(nuevoTerm);
            // Las calificaciones nacen nulas por defecto al crear un objeto nuevo
            repository.save(nuevo);
        }
    }
}