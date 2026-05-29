package com.itat.persona.Controller;

import com.itat.persona.Model.Pedido;
import com.itat.persona.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para el informe diario de ventas y pedidos.
 * Genera los datos necesarios para las gráficas con Chart.js.
 */
@Controller
public class InformeController {

    /** Servicio de pedidos para obtener datos del día. */
    @Autowired
    private PedidoService pedidoService;

    /**
     * Muestra el informe diario con gráficas de ventas y pedidos.
     *
     * @param model Objeto Model para pasar datos a la vista.
     * @return El nombre de la vista "informe".
     */
    @GetMapping("/informe")
    public String mostrarInforme(Model model) {

        List<Pedido> pedidosHoy = pedidoService.obtenerPedidosDeHoy();

        // ── Ventas por producto ──────────────────────────
        Map<String, Integer> ventasPorProducto = new LinkedHashMap<>();
        Map<String, Double>  ingresosPorProducto = new LinkedHashMap<>();

        for (Pedido p : pedidosHoy) {
            if (p.getEstado().equals("RECHAZADO")) continue;
            String prod = p.getProducto();
            ventasPorProducto.put(prod, ventasPorProducto.getOrDefault(prod, 0) + p.getCantidad());
            ingresosPorProducto.put(prod, ingresosPorProducto.getOrDefault(prod, 0.0) + p.getTotal());
        }

        // ── Pedidos por hora ─────────────────────────────
        Map<String, Integer> pedidosPorHora = new LinkedHashMap<>();
        for (int h = 0; h < 24; h++) {
            pedidosPorHora.put(String.format("%02d:00", h), 0);
        }
        for (Pedido p : pedidosHoy) {
            if (p.getEstado().equals("RECHAZADO")) continue;
            String hora = String.format("%02d:00", p.getFechaHora().getHour());
            pedidosPorHora.put(hora, pedidosPorHora.getOrDefault(hora, 0) + 1);
        }

        // ── Total de ingresos del día ────────────────────
        double totalIngresos = pedidosHoy.stream()
            .filter(p -> !p.getEstado().equals("RECHAZADO"))
            .mapToDouble(Pedido::getTotal)
            .sum();

        // ── Pasar datos al modelo ────────────────────────
        model.addAttribute("pedidosHoy", pedidosHoy);
        model.addAttribute("totalPedidos", pedidosHoy.size());
        model.addAttribute("totalIngresos", totalIngresos);
        model.addAttribute("productosLabels", ventasPorProducto.keySet());
        model.addAttribute("productosData", ventasPorProducto.values());
        model.addAttribute("horasLabels", pedidosPorHora.keySet());
        model.addAttribute("horasData", pedidosPorHora.values());

        return "informe";
    }
}