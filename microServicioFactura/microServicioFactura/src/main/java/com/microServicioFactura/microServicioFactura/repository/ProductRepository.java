package com.microServicioFactura.microServicioFactura.repository;

import com.microServicioFactura.microServicioFactura.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Producto, Integer> {
}
