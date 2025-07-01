package com.microServicioFactura.microServicioFactura.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "detalle_venta")
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
// Relación con la entidad Venta 
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;
// Relación con la entidad Producto
    @ManyToOne
    @JoinColumn(name = "producto_id", referencedColumnName = "codigoProducto", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private int cantidad;

}
