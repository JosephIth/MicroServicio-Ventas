package com.microServicioFactura.microServicioFactura.repository;

import com.microServicioFactura.microServicioFactura.model.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
}
