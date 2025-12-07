package com.inventario.modulo_inventario.service;

import com.inventario.modulo_inventario.entity.Solicitud;
import com.inventario.modulo_inventario.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

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

        sb.append("\n========================================\n");

        return new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
    }
}
