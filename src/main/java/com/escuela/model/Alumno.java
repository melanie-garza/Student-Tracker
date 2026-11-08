package com.escuela.model;

import jakarta.persistence.*;

@Entity
public class Alumno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String grupo;
    private String profesor;
    private String horario;
    private String term; 
    
    // Todas las calificaciones ahora son estrictamente Enteros (Integer)
    private Integer asistencia;
    private Integer oralExam;
    private Integer midTermExam;
    private Integer midTermProject;
    private Integer finalExam;
    private Integer finalProject;
    
    @Column(columnDefinition = "TEXT")
    private String comentarios;

    @Transient
    public Double getCalificacionFinal() {
        double att = (asistencia != null ? asistencia : 0) * 0.20;
        double oral = (oralExam != null ? oralExam : 0) * 0.20;
        double midE = (midTermExam != null ? midTermExam : 0) * 0.15;
        double midP = (midTermProject != null ? midTermProject : 0) * 0.15;
        double finE = (finalExam != null ? finalExam : 0) * 0.15;
        double finP = (finalProject != null ? finalProject : 0) * 0.15;
        return Math.round((att + oral + midE + midP + finE + finP) * 100.0) / 100.0;
    }

    @Transient
    public String getLetraAmericana() {
        double grade = getCalificacionFinal();
        if (grade >= 90) return "A";
        if (grade >= 80) return "B";
        if (grade >= 70) return "C";
        if (grade >= 60) return "D";
        return "F";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getGrupo() { return grupo; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public String getProfesor() { return profesor; }
    public void setProfesor(String profesor) { this.profesor = profesor; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    
    // Fuerza las mayúsculas desde el servidor
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = (term != null) ? term.toUpperCase() : null; }
    
    public Integer getAsistencia() { return asistencia; }
    public void setAsistencia(Integer asistencia) { this.asistencia = asistencia; }
    public Integer getOralExam() { return oralExam; }
    public void setOralExam(Integer oralExam) { this.oralExam = oralExam; }
    public Integer getMidTermExam() { return midTermExam; }
    public void setMidTermExam(Integer midTermExam) { this.midTermExam = midTermExam; }
    public Integer getMidTermProject() { return midTermProject; }
    public void setMidTermProject(Integer midTermProject) { this.midTermProject = midTermProject; }
    public Integer getFinalExam() { return finalExam; }
    public void setFinalExam(Integer finalExam) { this.finalExam = finalExam; }
    public Integer getFinalProject() { return finalProject; }
    public void setFinalProject(Integer finalProject) { this.finalProject = finalProject; }
    public String getComentarios() { return comentarios; }
    public void setComentarios(String comentarios) { this.comentarios = comentarios; }
}