package com.microServicioFactura.microServicioFactura.dto;

import com.microServicioFactura.microServicioFactura.model.DetalleVenta;
import lombok.Data;
import java.util.List;

@Data
public class VentaDetalleTotalDTO {
    private Integer ventaId;
    private List<DetalleVenta> detalles;
    private double total;
}