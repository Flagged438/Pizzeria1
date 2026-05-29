package com.itat.persona;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/**
 * Clase principal de la aplicación Spring Boot.
 * Configura la conexión a MongoDB Atlas y habilita
 * el escaneo de repositorios MongoDB.
 */
@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.itat.persona.Repository")
public class PersonaApplication {

    /**
     * Punto de entrada de la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(PersonaApplication.class, args);
    }

    /**
     * Crea y configura el cliente de conexión a MongoDB Atlas.
     *
     * @return Instancia de MongoClient conectada a Atlas.
     */
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://241150087_db_user:JZWs0R9jGSBwZalp@itat.8zxy4hb.mongodb.net/personasdb?retryWrites=true&w=majority&appName=itat");
    }

    /**
     * Crea el MongoTemplate para operaciones sobre la base de datos.
     *
     * @param mongoClient Cliente MongoDB inyectado automáticamente.
     * @return Instancia de MongoTemplate apuntando a "personasdb".
     * @throws Exception Si ocurre un error al crear el template.
     */
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoClient(), "personasdb");
    }
}