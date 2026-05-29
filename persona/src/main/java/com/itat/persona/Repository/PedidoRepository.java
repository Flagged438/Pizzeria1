package com.itat.persona.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.itat.persona.Model.Pedido;

/**
 * Repositorio para la entidad Pedido.
 * Extiende MongoRepository para proveer operaciones CRUD
 * sobre la colección "pedidos" en MongoDB.
 */
public interface PedidoRepository extends MongoRepository<Pedido, String> {

    /**
     * Busca pedidos dentro de un rango de fechas.
     *
     * @param inicio Fecha y hora de inicio del rango.
     * @param fin    Fecha y hora de fin del rango.
     * @return Lista de pedidos en ese rango.
     */
    List<Pedido> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
}
