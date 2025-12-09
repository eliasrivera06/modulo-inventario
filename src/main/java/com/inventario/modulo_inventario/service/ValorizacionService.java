package com.inventario.modulo_inventario.service;

import com.inventario.modulo_inventario.entity.Producto;
import com.inventario.modulo_inventario.repository.ProductoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ValorizacionService {

    private final ProductoRepository productoRepository;

    public ValorizacionService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Genera un PDF con la valorización de stock
     * 
     * @param filtroTipo:  "TODO", "CATEGORIA", "SEDE"
     * @param filtroValor: valor del filtro (puede ser múltiples categorías
     *                     separadas por coma)
     */
    public byte[] generarPdfValorizacion(String filtroTipo, String filtroValor) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            // Obtener productos según filtro
            List<Producto> productos = obtenerProductosFiltrados(filtroTipo, filtroValor);

            // Crear documento PDF
            Document document = new Document(PageSize.A4, 36, 36, 54, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Fuentes
            Font fontTitulo = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(33, 37, 41));
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(108, 117, 125));
            Font fontHeader = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
            Font fontCelda = new Font(Font.HELVETICA, 9, Font.NORMAL, new Color(33, 37, 41));
            Font fontTotal = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(0, 123, 255));

            // Título
            Paragraph titulo = new Paragraph("📊 VALORIZACIÓN DE STOCK", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            // Fecha y filtro aplicado
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            String filtroTexto = obtenerTextoFiltro(filtroTipo, filtroValor);
            Paragraph info = new Paragraph("Fecha: " + fechaHora + "\nFiltro: " + filtroTexto, fontSubtitulo);
            info.setAlignment(Element.ALIGN_CENTER);
            info.setSpacingAfter(20);
            document.add(info);

            // Tabla de productos (7 columnas: con Precio)
            PdfPTable tabla = new PdfPTable(7);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new float[] { 1.3f, 2.2f, 1.3f, 1.3f, 0.9f, 1.2f, 1.5f });

            // Headers
            String[] headers = { "Código", "Nombre", "Categoría", "Sede", "Stock", "Precio", "Valor Total" };
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, fontHeader));
                cell.setBackgroundColor(new Color(0, 123, 255));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                tabla.addCell(cell);
            }

            // Datos de productos y cálculo de total
            BigDecimal totalGeneral = BigDecimal.ZERO;

            for (Producto p : productos) {
                BigDecimal valorProducto = p.getPrecio().multiply(new BigDecimal(p.getStock()));
                totalGeneral = totalGeneral.add(valorProducto);

                // Código
                PdfPCell celdaCodigo = new PdfPCell(new Phrase(p.getCodigo(), fontCelda));
                celdaCodigo.setPadding(5);
                tabla.addCell(celdaCodigo);

                // Nombre
                PdfPCell celdaNombre = new PdfPCell(new Phrase(p.getNombre(), fontCelda));
                celdaNombre.setPadding(5);
                tabla.addCell(celdaNombre);

                // Categoría
                PdfPCell celdaCategoria = new PdfPCell(new Phrase(p.getCategoria(), fontCelda));
                celdaCategoria.setPadding(5);
                tabla.addCell(celdaCategoria);

                // Sede
                PdfPCell celdaSede = new PdfPCell(new Phrase(p.getSede(), fontCelda));
                celdaSede.setPadding(5);
                tabla.addCell(celdaSede);

                // Stock
                PdfPCell celdaStock = new PdfPCell(new Phrase(String.valueOf(p.getStock()), fontCelda));
                celdaStock.setHorizontalAlignment(Element.ALIGN_CENTER);
                celdaStock.setPadding(5);
                tabla.addCell(celdaStock);

                // Precio unitario
                PdfPCell celdaPrecio = new PdfPCell(
                        new Phrase("S/ " + p.getPrecio().setScale(2, java.math.RoundingMode.HALF_UP), fontCelda));
                celdaPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaPrecio.setPadding(5);
                tabla.addCell(celdaPrecio);

                // Valor Total (Precio x Stock)
                PdfPCell celdaValor = new PdfPCell(
                        new Phrase("S/ " + valorProducto.setScale(2, java.math.RoundingMode.HALF_UP), fontCelda));
                celdaValor.setHorizontalAlignment(Element.ALIGN_RIGHT);
                celdaValor.setPadding(5);
                tabla.addCell(celdaValor);
            }

            document.add(tabla);

            // Línea separadora
            document.add(new Paragraph(" "));

            // Resumen
            PdfPTable tablaResumen = new PdfPTable(2);
            tablaResumen.setWidthPercentage(50);
            tablaResumen.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // Total de productos
            PdfPCell celdaLabelProductos = new PdfPCell(new Phrase("Total de Productos:", fontCelda));
            celdaLabelProductos.setBorder(Rectangle.NO_BORDER);
            celdaLabelProductos.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaLabelProductos.setPadding(5);
            tablaResumen.addCell(celdaLabelProductos);

            PdfPCell celdaValorProductos = new PdfPCell(new Phrase(String.valueOf(productos.size()), fontCelda));
            celdaValorProductos.setBorder(Rectangle.NO_BORDER);
            celdaValorProductos.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaValorProductos.setPadding(5);
            tablaResumen.addCell(celdaValorProductos);

            // Total valorización
            PdfPCell celdaLabelTotal = new PdfPCell(new Phrase("VALORIZACIÓN TOTAL:", fontTotal));
            celdaLabelTotal.setBorder(Rectangle.TOP);
            celdaLabelTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaLabelTotal.setPadding(8);
            tablaResumen.addCell(celdaLabelTotal);

            PdfPCell celdaValorTotal = new PdfPCell(
                    new Phrase("S/ " + totalGeneral.setScale(2, java.math.RoundingMode.HALF_UP), fontTotal));
            celdaValorTotal.setBorder(Rectangle.TOP);
            celdaValorTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaValorTotal.setPadding(8);
            tablaResumen.addCell(celdaValorTotal);

            document.add(tablaResumen);

            // Pie de página
            document.add(new Paragraph(" "));
            Paragraph pie = new Paragraph("Sistema de Inventario - Valorización generada automáticamente",
                    new Font(Font.HELVETICA, 8, Font.ITALIC, new Color(108, 117, 125)));
            pie.setAlignment(Element.ALIGN_CENTER);
            document.add(pie);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    private List<Producto> obtenerProductosFiltrados(String filtroTipo, String filtroValor) {
        List<Producto> todosProductos = productoRepository.findAll();

        if (filtroTipo == null || filtroTipo.equals("TODO") || filtroValor == null || filtroValor.isBlank()) {
            return todosProductos;
        }

        String valorLower = filtroValor.toLowerCase().trim();

        return todosProductos.stream().filter(p -> {
            if (filtroTipo.equals("CATEGORIA")) {
                // Puede ser múltiples categorías separadas por coma
                String[] categorias = valorLower.split(",");
                for (String cat : categorias) {
                    if (p.getCategoria().toLowerCase().contains(cat.trim())) {
                        return true;
                    }
                }
                return false;
            } else if (filtroTipo.equals("SEDE")) {
                // Puede ser múltiples sedes separadas por coma
                String[] sedes = valorLower.split(",");
                for (String sede : sedes) {
                    if (p.getSede().toLowerCase().contains(sede.trim())) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }).toList();
    }

    private String obtenerTextoFiltro(String filtroTipo, String filtroValor) {
        if (filtroTipo == null || filtroTipo.equals("TODO") || filtroValor == null || filtroValor.isBlank()) {
            return "Todos los productos";
        }
        if (filtroTipo.equals("CATEGORIA")) {
            return "Categoría(s): " + filtroValor;
        }
        if (filtroTipo.equals("SEDE")) {
            return "Sede(s): " + filtroValor;
        }
        return "Todos los productos";
    }
}
