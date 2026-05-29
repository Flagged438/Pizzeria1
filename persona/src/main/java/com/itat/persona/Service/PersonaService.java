package com.itat.persona.Service;

import com.itat.persona.Model.Persona;
import com.itat.persona.Repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona la lógica de negocio de las personas.
 * Actúa como capa intermedia entre el controlador y el repositorio.
 */
@Service
public class PersonaService {

    /** Repositorio para acceder a los datos de personas en MongoDB. */
    @Autowired
    private PersonaRepository personaRepository;

    /**
     * Obtiene todas las personas registradas en la base de datos.
     *
     * @return Lista de todas las personas.
     */
    public List<Persona> obtenerTodas() {
        return personaRepository.findAll();
    }

    /**
     * Busca una persona por su ID.
     *
     * @param id Identificador único de la persona.
     * @return Optional con la persona encontrada, o vacío si no existe.
     */
    public Optional<Persona> obtenerPorId(String id) {
        return personaRepository.findById(id);
    }

    /**
     * Guarda una nueva persona en la base de datos.
     *
     * @param persona Objeto Persona a guardar.
     * @return La persona guardada con su ID asignado.
     */
    public Persona guardar(Persona persona) {
        return personaRepository.save(persona);
    }

    /**
     * Actualiza los datos de una persona existente.
     *
     * @param id                 Identificador único de la persona a actualizar.
     * @param personaActualizada Objeto Persona con los nuevos datos.
     * @return La persona actualizada.
     */
    public Persona actualizar(String id, Persona personaActualizada) {
        personaActualizada.setId(id);
        return personaRepository.save(personaActualizada);
    }

    /**
     * Elimina una persona de la base de datos por su ID.
     *
     * @param id Identificador único de la persona a eliminar.
     */
    public void eliminar(String id) {
        personaRepository.deleteById(id);
    }
}