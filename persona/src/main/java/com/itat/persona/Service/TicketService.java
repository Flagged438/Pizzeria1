package com.itat.persona.Service;

import com.itextpdf.text.pdf.draw.LineSeparator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itat.persona.Model.Pedido;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para generar tickets de pedido en formato PDF.
 * Incluye código QR generado con ZXing e iText para el PDF.
 */
@Service
public class TicketService {

    /** Color rojo de la pizzería para el ticket. */
    private static final BaseColor ROJO_ITAT = new BaseColor(230, 57, 70);

    /**
     * Genera un ticket PDF para un pedido dado.
     *
     * @param pedido El pedido para el cual se genera el ticket.
     * @return Arreglo de bytes con el PDF generado.
     * @throws DocumentException Si ocurre un error al crear el PDF.
     * @throws IOException       Si ocurre un error de entrada/salida.
     * @throws WriterException   Si ocurre un error al generar el QR.
     */
    public byte[] generarTicket(Pedido pedido)
            throws DocumentException, IOException, WriterException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A6);
        PdfWriter.getInstance(documento, baos);
        documento.open();

        // ── Fuentes ──────────────────────────────────────
        Font fuenteTitulo  = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, ROJO_ITAT);
        Font fuenteNormal  = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
        Font fuenteNegrita = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);
        Font fuentePeque   = new Font(Font.FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.GRAY);

        // ── Encabezado ───────────────────────────────────
        Paragraph titulo = new Paragraph("🍕 Pizzería ITAT", fuenteTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(titulo);

        Paragraph subtitulo = new Paragraph("Tu pizza favorita, siempre fresca", fuentePeque);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        documento.add(subtitulo);

        documento.add(new Paragraph(" "));

        // ── Línea divisoria ──────────────────────────────
        LineSeparator linea = new LineSeparator();
        linea.setLineColor(ROJO_ITAT);
        documento.add(new Chunk(linea));
        documento.add(new Paragraph(" "));

        // ── Datos del pedido ─────────────────────────────
        documento.add(new Paragraph("TICKET DE PEDIDO", fuenteNegrita));
        documento.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(5f);

        agregarFilaTabla(tabla, "ID Pedido:",      pedido.getId(),              fuenteNegrita, fuentePeque);
        agregarFilaTabla(tabla, "Cliente:",        pedido.getNombreCliente(),   fuenteNegrita, fuenteNormal);
        agregarFilaTabla(tabla, "Producto:",       pedido.getProducto(),        fuenteNegrita, fuenteNormal);
        agregarFilaTabla(tabla, "Cantidad:",       String.valueOf(pedido.getCantidad()), fuenteNegrita, fuenteNormal);
        agregarFilaTabla(tabla, "Total:",          "$" + pedido.getTotal(),     fuenteNegrita, fuenteNormal);
        agregarFilaTabla(tabla, "Estado:",         pedido.getEstado(),          fuenteNegrita, fuenteNormal);
        agregarFilaTabla(tabla, "Fecha y Hora:",
            pedido.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            fuenteNegrita, fuenteNormal);

        documento.add(tabla);
        documento.add(new Paragraph(" "));
        documento.add(new Chunk(linea));
        documento.add(new Paragraph(" "));

        // ── Código QR ────────────────────────────────────
        String contenidoQR = "Pedido ID: " + pedido.getId()
            + "\nCliente: " + pedido.getNombreCliente()
            + "\nProducto: " + pedido.getProducto()
            + "\nTotal: $" + pedido.getTotal()
            + "\nEstado: " + pedido.getEstado();

        byte[] qrBytes = generarQR(contenidoQR, 150, 150);
        Image qrImagen = Image.getInstance(qrBytes);
        qrImagen.setAlignment(Element.ALIGN_CENTER);
        qrImagen.scaleToFit(150, 150);
        documento.add(qrImagen);

        Paragraph textoQR = new Paragraph("Escanea para verificar tu pedido", fuentePeque);
        textoQR.setAlignment(Element.ALIGN_CENTER);
        documento.add(textoQR);

        documento.add(new Paragraph(" "));

        // ── Pie de página ─────────────────────────────────
        Paragraph pie = new Paragraph("© 2026 Pizzería ITAT — ¡Gracias por tu preferencia!", fuentePeque);
        pie.setAlignment(Element.ALIGN_CENTER);
        documento.add(pie);

        documento.close();
        return baos.toByteArray();
    }

    /**
     * Agrega una fila con dos celdas a una tabla PDF.
     *
     * @param tabla      La tabla donde se agrega la fila.
     * @param etiqueta   Texto de la columna izquierda (etiqueta).
     * @param valor      Texto de la columna derecha (valor).
     * @param fEtiqueta  Fuente para la etiqueta.
     * @param fValor     Fuente para el valor.
     */
    private void agregarFilaTabla(PdfPTable tabla, String etiqueta, String valor,
                                   Font fEtiqueta, Font fValor) {
        PdfPCell celda1 = new PdfPCell(new Phrase(etiqueta, fEtiqueta));
        PdfPCell celda2 = new PdfPCell(new Phrase(valor, fValor));
        celda1.setBorder(Rectangle.NO_BORDER);
        celda2.setBorder(Rectangle.NO_BORDER);
        tabla.addCell(celda1);
        tabla.addCell(celda2);
    }

    /**
     * Genera un código QR como arreglo de bytes PNG.
     *
     * @param contenido Texto a codificar en el QR.
     * @param ancho     Ancho del QR en píxeles.
     * @param alto      Alto del QR en píxeles.
     * @return Arreglo de bytes con la imagen PNG del QR.
     * @throws WriterException Si ocurre un error al generar el QR.
     * @throws IOException     Si ocurre un error de entrada/salida.
     */
    private byte[] generarQR(String contenido, int ancho, int alto)
            throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(contenido, BarcodeFormat.QR_CODE, ancho, alto);
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOut);
        return pngOut.toByteArray();
    }
}
