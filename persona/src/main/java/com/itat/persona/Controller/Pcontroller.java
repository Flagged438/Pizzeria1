package com.itat.persona.Controller;

import com.itat.persona.Model.Persona;
import com.itat.persona.Service.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador principal para la gestión de personas.
 * Maneja las operaciones CRUD mediante peticiones HTTP.
 * Delega la lógica de negocio a PersonaService.
 */
@Controller
public class Pcontroller {

    /** Servicio que contiene la lógica de negocio de personas. */
    @Autowired
    private PersonaService personaService;

    /**
     * Muestra la página de inicio.
     *
     * @return El nombre de la vista "home".
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }

    /**
     * Muestra el formulario de registro y la lista de personas.
     *
     * @param model Objeto Model para pasar datos a la vista.
     * @return El nombre de la vista "crud-persona".
     */
    @GetMapping("/personas")
    public String mostrarFormularioYLista(Model model) {
        model.addAttribute("persona", new Persona());
        model.addAttribute("personas", personaService.obtenerTodas());
        return "crud-persona";
    }

    /**
     * Guarda una nueva persona en la base de datos.
     *
     * @param persona Objeto Persona con los datos del formulario.
     * @return Redirección a la lista de personas.
     */
    @PostMapping("/guardar")
    public String guardarPersona(Persona persona) {
        personaService.guardar(persona);
        return "redirect:/personas";
    }

    /**
     * Elimina una persona de la base de datos por su ID.
     *
     * @param id Identificador único de la persona a eliminar.
     * @return Redirección a la lista de personas.
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarPersona(@PathVariable String id) {
        personaService.eliminar(id);
        return "redirect:/personas";
    }

    /**
     * Muestra el formulario de edición con los datos de una persona.
     *
     * @param id    Identificador único de la persona a editar.
     * @param model Objeto Model para pasar los datos a la vista.
     * @return El nombre de la vista "edit-persona", o redirección si no existe.
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        personaService.obtenerPorId(id)
            .ifPresent(p -> model.addAttribute("persona", p));
        return "edit-persona";
    }

    /**
     * Actualiza los datos de una persona existente en la base de datos.
     *
     * @param id                 Identificador único de la persona a actualizar.
     * @param personaActualizada Objeto Persona con los nuevos datos del formulario.
     * @return Redirección a la lista de personas.
     */
    @PostMapping("/actualizar/{id}")
    public String actualizarPersona(@PathVariable String id, Persona personaActualizada) {
        personaService.actualizar(id, personaActualizada);
        return "redirect:/personas";
    }
}