package com.microServicioFactura.microServicioFactura.dto;

import lombok.Data;
import java.sql.Date;
import java.util.List;

@Data
public class VentaRequestDTO {
    private String clienteRut;
    private String encargadoVenta;
    private Date fechaVenta;
    private List<DetalleDTO> detalles;

    @Data
    public static class DetalleDTO {
        private Integer productoId;
        private int cantidad;
    }
}
