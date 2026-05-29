package com.itat.persona.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itat.persona.Model.Pedido;
import com.itat.persona.Repository.PedidoRepository;

/**
 * Servicio que gestiona la lógica de negocio de los pedidos.
 * Implementa control de concurrencia mediante hilos virtuales
 * y limita los pedidos a un máximo de 10 por hora.
 */
@Service
public class PedidoService {

    /** Límite máximo de pedidos permitidos por hora. */
    private static final int LIMITE_POR_HORA = 10;

    /** Repositorio de pedidos para acceso a MongoDB. */
    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Contador atómico de pedidos en la hora actual.
     * Se usa AtomicInteger para evitar condiciones de carrera entre hilos.
     */
    private final AtomicInteger contadorHoraActual = new AtomicInteger(0);

    /**
     * Hora en que se reinició el contador por última vez.
     */
    private int horaActual = LocalDateTime.now().getHour();

    /**
     * Pool de hilos virtuales para procesar pedidos de forma concurrente.
     * Usa newVirtualThreadPerTaskExecutor() para hilos virtuales de Java 21.
     */
    private final ExecutorService ejecutor = Executors.newVirtualThreadPerTaskExecutor();

    /**
     * Procesa un pedido nuevo de forma asíncrona en un hilo virtual.
     * Verifica el límite de pedidos por hora antes de procesar.
     *
     * @param pedido El pedido a procesar.
     * @return Mensaje indicando si el pedido fue aceptado o rechazado.
     */
    public String procesarPedido(Pedido pedido) {
        // Verificar si cambió la hora y reiniciar el contador
        int horaAhora = LocalDateTime.now().getHour();
        if (horaAhora != horaActual) {
            horaActual = horaAhora;
            contadorHoraActual.set(0);
        }

        // Verificar límite de pedidos por hora
        if (contadorHoraActual.get() >= LIMITE_POR_HORA) {
            pedido.setEstado("RECHAZADO");
            pedido.setFechaHora(LocalDateTime.now());
            pedidoRepository.save(pedido);
            return "RECHAZADO: Se alcanzó el límite de " + LIMITE_POR_HORA + " pedidos por hora.";
        }

        // Incrementar contador y procesar en hilo virtual
        contadorHoraActual.incrementAndGet();
        pedido.setFechaHora(LocalDateTime.now());
        pedido.setEstado("PENDIENTE");

        // Lanzar hilo virtual para procesar el pedido
        ejecutor.submit(() -> procesarEnHilo(pedido));

        return "ACEPTADO: Tu pedido está siendo procesado. Pedidos esta hora: " + contadorHoraActual.get() + "/" + LIMITE_POR_HORA;
    }

    /**
     * Procesa el pedido dentro de un hilo virtual.
     * Simula el tiempo de preparación y actualiza el estado en MongoDB.
     *
     * @param pedido El pedido a procesar en el hilo.
     */
    private void procesarEnHilo(Pedido pedido) {
        try {
            System.out.println("[Hilo] Procesando pedido de: " + pedido.getNombreCliente());
            pedido.setEstado("EN_PROCESO");
            pedidoRepository.save(pedido);

            // Simula tiempo de preparación (3 segundos)
            Thread.sleep(3000);

            pedido.setEstado("ENTREGADO");
            pedidoRepository.save(pedido);
            System.out.println("[Hilo] Pedido entregado a: " + pedido.getNombreCliente());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            pedido.setEstado("RECHAZADO");
            pedidoRepository.save(pedido);
        }
    }

    /**
     * Obtiene todos los pedidos almacenados en MongoDB.
     *
     * @return Lista de todos los pedidos.
     */
    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * Obtiene los pedidos realizados en el día actual.
     *
     * @return Lista de pedidos del día.
     */
    public List<Pedido> obtenerPedidosDeHoy() {
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finDia    = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        return pedidoRepository.findByFechaHoraBetween(inicioDia, finDia);
    }

    /**
     * Obtiene cuántos pedidos se han hecho en la hora actual.
     *
     * @return Número de pedidos en la hora actual.
     */
    public int getPedidosHoraActual() {
        return contadorHoraActual.get();
    }

    /**
     * Obtiene el límite máximo de pedidos por hora.
     *
     * @return Límite de pedidos por hora.
     */
    public int getLimitePorHora() {
        return LIMITE_POR_HORA;
    }
}