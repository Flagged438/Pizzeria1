package com.itat.persona.Controller;

import com.itat.persona.Model.Pedido;
import com.itat.persona.Repository.PedidoRepository;
import com.itat.persona.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

/**
 * Controlador para la generación y descarga de tickets PDF.
 * Genera un PDF con los datos del pedido y un código QR.
 */
@Controller
public class TicketController {

    /** Servicio para generar el PDF del ticket. */
    @Autowired
    private TicketService ticketService;

    /** Repositorio para buscar el pedido por ID. */
    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Genera y descarga el ticket PDF de un pedido específico.
     *
     * @param id Identificador único del pedido.
     * @return ResponseEntity con el PDF como archivo descargable,
     *         o 404 si el pedido no existe.
     */
    @GetMapping("/ticket/{id}")
    public ResponseEntity<byte[]> descargarTicket(@PathVariable String id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            byte[] pdf = ticketService.generarTicket(pedidoOpt.get());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "ticket-" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
