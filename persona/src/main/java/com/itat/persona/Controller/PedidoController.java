package com.itat.persona.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.itat.persona.Model.Pedido;
import com.itat.persona.Service.PedidoService;

/**
 * Controlador para la gestión de pedidos de la pizzería.
 * Maneja las rutas HTTP relacionadas con pedidos.
 */
@Controller
public class PedidoController {

    /** Servicio que contiene la lógica de negocio de pedidos. */
    @Autowired
    private PedidoService pedidoService;

    /**
     * Muestra el formulario de pedidos con la lista de pedidos del día.
     *
     * @param model Objeto Model para pasar datos a la vista.
     * @return El nombre de la vista "pedidos".
     */
    @GetMapping("/pedidos")
    public String mostrarPedidos(Model model) {
        model.addAttribute("pedido", new Pedido());
        model.addAttribute("pedidos", pedidoService.obtenerPedidosDeHoy());
        model.addAttribute("pedidosHora", pedidoService.getPedidosHoraActual());
        model.addAttribute("limitePorHora", pedidoService.getLimitePorHora());
        return "pedidos";
    }

    /**
     * Recibe y procesa un nuevo pedido mediante hilo virtual.
     * Si se supera el límite por hora, el pedido es rechazado.
     *
     * @param pedido             Objeto Pedido con los datos del formulario.
     * @param redirectAttributes Atributos para pasar mensajes al redirect.
     * @return Redirección a la vista de pedidos con mensaje de resultado.
     */
    @PostMapping("/pedidos/nuevo")
    public String nuevoPedido(Pedido pedido, RedirectAttributes redirectAttributes) {
        String resultado = pedidoService.procesarPedido(pedido);
        redirectAttributes.addFlashAttribute("mensaje", resultado);
        redirectAttributes.addFlashAttribute(
            "tipoMensaje",
            resultado.startsWith("ACEPTADO") ? "success" : "danger"
        );
        return "redirect:/pedidos";
    }
}
