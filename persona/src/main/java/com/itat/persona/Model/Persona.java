package com.itat.persona.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase que representa el modelo de una Persona.
 * Es mapeada a la colección "personas" en MongoDB.
 */
@Document(collection = "personas")
public class Persona {

    /** Identificador único generado por MongoDB. */
    @Id
    private String id;

    /** Nombre de la persona. */
    private String nombre;

    /** Apellido de la persona. */
    private String apellido;

    /** Edad de la persona. */
    private int edad;

    /** Usuario de acceso de la persona. */
    private String usuario;

    /** Contraseña de acceso de la persona. */
    private String contrasena;

    /**
     * Constructor vacío requerido por Spring Data MongoDB.
     */
    public Persona() {}

    /**
     * Constructor para crear una persona con sus datos básicos.
     *
     * @param nombre    Nombre de la persona.
     * @param apellido  Apellido de la persona.
     * @param edad      Edad de la persona.
     */
    public Persona(String nombre, String apellido, int edad) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
    }

    /**
     * Obtiene el identificador único de la persona.
     *
     * @return El id de la persona.
     */
    public String getId() { return id; }

    /**
     * Establece el identificador único de la persona.
     *
     * @param id El id a asignar.
     */
    public void setId(String id) { this.id = id; }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return El nombre de la persona.
     */
    public String getNombre() { return nombre; }

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre El nombre a asignar.
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Obtiene el apellido de la persona.
     *
     * @return El apellido de la persona.
     */
    public String getApellido() { return apellido; }

    /**
     * Establece el apellido de la persona.
     *
     * @param apellido El apellido a asignar.
     */
    public void setApellido(String apellido) { this.apellido = apellido; }

    /**
     * Obtiene la edad de la persona.
     *
     * @return La edad de la persona.
     */
    public int getEdad() { return edad; }

    /**
     * Establece la edad de la persona.
     *
     * @param edad La edad a asignar.
     */
    public void setEdad(int edad) { this.edad = edad; }

    /**
     * Obtiene el usuario de la persona.
     *
     * @return El usuario de la persona.
     */
    public String getUsuario() { return usuario; }

    /**
     * Establece el usuario de la persona.
     *
     * @param usuario El usuario a asignar.
     */
    public void setUsuario(String usuario) { this.usuario = usuario; }

    /**
     * Obtiene la contraseña de la persona.
     *
     * @return La contraseña de la persona.
     */
    public String getContrasena() { return contrasena; }

    /**
     * Establece la contraseña de la persona.
     *
     * @param contrasena La contraseña a asignar.
     */
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}