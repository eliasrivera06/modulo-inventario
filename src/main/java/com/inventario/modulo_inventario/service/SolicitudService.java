package com.inventario.modulo_inventario.service;

import com.inventario.modulo_inventario.entity.Solicitud;
import com.inventario.modulo_inventario.repository.SolicitudRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudRepository;

    public SolicitudService(SolicitudRepository solicitudRepository) {
        this.solicitudRepository = solicitudRepository;
    }

    @Transactional
    public Solicitud guardar(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    // Listar todas las solicitudes
    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAllByOrderByFechaDesc();
    }

    // Buscar por nombre o código
    public List<Solicitud> buscar(String keyword) {
        return solicitudRepository.buscarPorNombreOCodigo(keyword);
    }

    public ByteArrayInputStream generarReporteTxt(Solicitud solicitud) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       REPORTE DE SOLICITUD DE ALMACÉN       \n");
        sb.append("========================================\n\n");

        sb.append("FECHA: ").append(solicitud.getFecha().format(formatter)).append("\n\n");

        sb.append("--- DATOS DEL SOLICITANTE ---\n");
        sb.append("Nombre: ").append(solicitud.getNombreSolicitante()).append("\n");
        sb.append("Código: ").append(solicitud.getCodigoSolicitante()).append("\n");
        sb.append("Sede Origen: ").append(solicitud.getSedeOrigen()).append("\n\n");

        sb.append("--- DATOS DEL PRODUCTO ---\n");
        sb.append("Código: ").append(solicitud.getCodigoProducto()).append("\n");
        sb.append("Nombre: ").append(solicitud.getNombreProducto()).append("\n");
        sb.append("Categoría: ").append(solicitud.getCategoriaProducto()).append("\n");
        sb.append("Cantidad Solicitada: ").append(solicitud.getCantidadSolicitada()).append("\n\n");

        sb.append("--- DETALLES ---\n");
        sb.append("Motivo/Comentario: ").append(solicitud.getMotivo()).append("\n");
        sb.append("Destino de Consulta: ").append(solicitud.getDestino()).append("\n");
        sb.append("Tipo de Acción: ").append(solicitud.getTipoAccion()).append("\n");
        sb.append("Estado: ").append(solicitud.getEstado()).append("\n");

        sb.append("\n========================================\n");

        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    // Generar PDF con todas las consultas
    public byte[] generarPdfConsultas(List<Solicitud> solicitudes) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fuentes
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(33, 37, 41));
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(108, 117, 125));
            Font fontHeader = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
            Font fontCelda = new Font(Font.HELVETICA, 8, Font.NORMAL, new Color(33, 37, 41));

            // Título
            Paragraph titulo = new Paragraph("📋 LISTADO DE CONSULTAS DE ALMACÉN", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // Fecha
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph info = new Paragraph("Generado: " + fechaHora + " | Total: " + solicitudes.size() + " consultas",
                    fontSubtitulo);
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(20);
            document.add(info);

            // Tabla
            PdfPTable tabla = new PdfPTable(9);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[] { 1.2f, 1.5f, 1.2f, 1.2f, 1.5f, 1.2f, 0.8f, 1f, 0.8f });

            // Headers
            String[] headers = { "Fecha", "Solicitante", "Cód. Encargado", "Sede Origen", "Producto", "Destino",
                    "Cantidad", "Tipo", "Estado" };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
                cell.setBackgroundColor(new Color(0, 123, 255));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(6);
                tabla.addCell(cell);
            }

            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm");

            for (Solicitud s : solicitudes) {
                // Fecha
                PdfPCell c1 = new PdfPCell(new Phrase(s.getFecha() != null ? s.getFecha().format(df) : "-", fontCelda));
                c1.setPadding(4);
                tabla.addCell(c1);

                // Solicitante
                PdfPCell c2 = new PdfPCell(new Phrase(s.getNombreSolicitante(), fontCelda));
                c2.setPadding(4);
                tabla.addCell(c2);

                // Código Encargado
                PdfPCell c3 = new PdfPCell(new Phrase(s.getCodigoSolicitante(), fontCelda));
                c3.setPadding(4);
                tabla.addCell(c3);

                // Sede Origen
                PdfPCell c4 = new PdfPCell(new Phrase(s.getSedeOrigen(), fontCelda));
                c4.setPadding(4);
                tabla.addCell(c4);

                // Producto
                PdfPCell c5 = new PdfPCell(new Phrase(s.getNombreProducto(), fontCelda));
                c5.setPadding(4);
                tabla.addCell(c5);

                // Destino
                PdfPCell c6 = new PdfPCell(new Phrase(s.getDestino(), fontCelda));
                c6.setPadding(4);
                tabla.addCell(c6);

                // Cantidad
                PdfPCell c7 = new PdfPCell(new Phrase(String.valueOf(s.getCantidadSolicitada()), fontCelda));
                c7.setHorizontalAlignment(Element.ALIGN_CENTER);
                c7.setPadding(4);
                tabla.addCell(c7);

                // Tipo
                PdfPCell c8 = new PdfPCell(new Phrase(s.getTipoAccion(), fontCelda));
                c8.setPadding(4);
                tabla.addCell(c8);

                // Estado con color
                PdfPCell c9 = new PdfPCell(new Phrase(s.getEstado(), fontCelda));
                if ("Enviado".equals(s.getEstado())) {
                    c9.setBackgroundColor(new Color(255, 243, 205)); // Amarillo
                } else if ("Recibido".equals(s.getEstado())) {
                    c9.setBackgroundColor(new Color(212, 237, 218)); // Verde claro
                } else if ("Realizado".equals(s.getEstado())) {
                    c9.setBackgroundColor(new Color(209, 231, 221)); // Verde
                }
                c9.setHorizontalAlignment(Element.ALIGN_CENTER);
                c9.setPadding(4);
                tabla.addCell(c9);
            }

            document.add(tabla);

            // Pie
            document.add(new Paragraph(" "));
            Paragraph pie = new Paragraph("Sistema de Inventario - Reporte de Consultas",
                    new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(108, 117, 125)));
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    // Generar PDF de una consulta individual
    public byte[] generarPdfConsultaIndividual(Long id) {
        var solicitudOpt = solicitudRepository.findById(id);
        if (solicitudOpt.isEmpty()) {
            return null;
        }

        Solicitud s = solicitudOpt.get();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fuentes
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(33, 37, 41));
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(108, 117, 125));
            Font fontLabel = new Font(Font.HELVETICA, 10, Font.BOLD, new Color(33, 37, 41));
            Font fontValor = new Font(Font.HELVETICA, 10, Font.NORMAL, new Color(33, 37, 41));
            Font fontEstado = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 123, 255));

            // Título
            Paragraph titulo = new Paragraph("📋 CONSULTA DE ALMACÉN #" + s.getId(), fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // Fecha de generación
            String fechaGen = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph info = new Paragraph("Generado: " + fechaGen, fontSubtitulo);
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(20);
            document.add(info);

            // Tabla de datos
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            tabla.setWidths(new float[] { 1f, 2f });

            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            // Datos del solicitante
            agregarFilaTabla(tabla, "📅 Fecha de Solicitud", s.getFecha() != null ? s.getFecha().format(df) : "-",
                    fontLabel, fontValor);
            agregarFilaTabla(tabla, "👤 Nombre Solicitante", s.getNombreSolicitante(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "🔑 Código Encargado", s.getCodigoSolicitante(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "🏢 Sede Origen", s.getSedeOrigen(), fontLabel, fontValor);

            // Datos del producto
            agregarFilaTabla(tabla, "📦 Código Producto", s.getCodigoProducto(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "📝 Nombre Producto", s.getNombreProducto(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "📂 Categoría", s.getCategoriaProducto(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "🔢 Cantidad Solicitada", String.valueOf(s.getCantidadSolicitada()), fontLabel,
                    fontValor);

            // Detalles
            agregarFilaTabla(tabla, "💬 Motivo", s.getMotivo() != null ? s.getMotivo() : "-", fontLabel, fontValor);
            agregarFilaTabla(tabla, "🏬 Destino", s.getDestino(), fontLabel, fontValor);
            agregarFilaTabla(tabla, "📬 Tipo de Acción", s.getTipoAccion(), fontLabel, fontValor);

            document.add(tabla);

            // Estado destacado
            document.add(new Paragraph(" "));
            Paragraph estadoParagraph = new Paragraph("Estado: " + s.getEstado(), fontEstado);
            estadoParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(estadoParagraph);

            // Pie
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            Paragraph pie = new Paragraph("Sistema de Inventario - Consulta de Almacén",
                    new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(108, 117, 125)));
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private void agregarFilaTabla(PdfPTable tabla, String label, String valor, Font fontLabel, Font fontValor) {
        PdfPCell celdaLabel = new PdfPCell(new Phrase(label, fontLabel));
        celdaLabel.setBorder(Rectangle.NO_BORDER);
        celdaLabel.setPadding(8);
        celdaLabel.setBackgroundColor(new Color(248, 249, 250));
        tabla.addCell(celdaLabel);

        PdfPCell celdaValor = new PdfPCell(new Phrase(valor, fontValor));
        celdaValor.setBorder(Rectangle.NO_BORDER);
        celdaValor.setPadding(8);
        tabla.addCell(celdaValor);
    }
}
