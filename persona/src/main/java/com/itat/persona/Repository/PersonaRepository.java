package com.itat.persona.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.itat.persona.Model.Persona;

/**
 * Repositorio para la entidad Persona.
 * Extiende MongoRepository para proveer operaciones CRUD
 * sobre la colección "personas" en MongoDB.
 */
public interface PersonaRepository extends MongoRepository<Persona, String> {
}