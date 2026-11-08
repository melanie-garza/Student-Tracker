package com.escuela.controller;

import com.escuela.model.Alumno;
import com.escuela.service.AlumnoService;
import com.escuela.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@Controller
public class AlumnoController {
    
    private final AlumnoService alumnoService;
    private final PdfService pdfService;

    public AlumnoController(AlumnoService alumnoService, PdfService pdfService) {
        this.alumnoService = alumnoService;
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    public String dashboard(@RequestParam(required = false) String fProfesor, 
                            @RequestParam(required = false) String fGrupo,
                            Model model) {
        List<Alumno> alumnosFiltrados = alumnoService.filtrar(fProfesor, fGrupo);
        
        model.addAttribute("alumnos", alumnosFiltrados);
        model.addAttribute("promedioGrupo", alumnoService.calcularPromedioGrupo(alumnosFiltrados));
        model.addAttribute("alumno", new Alumno()); 
        model.addAttribute("fProfesor", fProfesor);
        model.addAttribute("fGrupo", fGrupo);
        return "dashboard";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Alumno alumno) {
        alumnoService.guardar(alumno);
        return "redirect:/";
    }

    @PostMapping("/promover")
    public String promover(@RequestParam String pProfesor, @RequestParam String pGrupoActual, 
                           @RequestParam String pNuevoGrupo, @RequestParam String pNuevoTerm) {
        alumnoService.promoverGrupo(pProfesor, pGrupoActual, pNuevoGrupo, pNuevoTerm);
        // Redirige al nuevo grupo filtrado automáticamente
        return "redirect:/?fProfesor=" + pProfesor.replace(" ", "%20") + "&fGrupo=" + pNuevoGrupo.replace(" ", "%20");
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("alumnos", alumnoService.listarTodos());
        model.addAttribute("alumno", alumnoService.buscarPorId(id)); 
        return "dashboard";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        alumnoService.eliminar(id);
        return "redirect:/";
    }

    @GetMapping("/boleta/{id}")
    public void descargarBoleta(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Alumno alumno = alumnoService.buscarPorId(id);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ReportCard_" + alumno.getNombre().replace(" ", "_") + ".pdf");
        pdfService.generarBoleta(response, alumno);
    }
}