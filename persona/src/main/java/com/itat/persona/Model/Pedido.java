package com.itat.persona.Model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Clase que representa el modelo de un Pedido.
 * Es mapeada a la colección "pedidos" en MongoDB.
 */
@Document(collection = "pedidos")
public class Pedido {

    /** Identificador único generado por MongoDB. */
    @Id
    private String id;

    /** Nombre del cliente que realiza el pedido. */
    private String nombreCliente;

    /** Producto solicitado en el pedido. */
    private String producto;

    /** Cantidad del producto solicitado. */
    private int cantidad;

    /** Precio total del pedido. */
    private double total;

    /** Fecha y hora en que se realizó el pedido. */
    private LocalDateTime fechaHora;

    /** Estado del pedido: PENDIENTE, EN_PROCESO, ENTREGADO, RECHAZADO. */
    private String estado;

    /**
     * Constructor vacío requerido por Spring Data MongoDB.
     */
    public Pedido() {}

    /**
     * Constructor para crear un pedido con sus datos básicos.
     *
     * @param nombreCliente Nombre del cliente.
     * @param producto      Producto solicitado.
     * @param cantidad      Cantidad del producto.
     * @param total         Precio total del pedido.
     */
    public Pedido(String nombreCliente, String producto, int cantidad, double total) {
        this.nombreCliente = nombreCliente;
        this.producto      = producto;
        this.cantidad      = cantidad;
        this.total         = total;
        this.fechaHora     = LocalDateTime.now();
        this.estado        = "PENDIENTE";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
