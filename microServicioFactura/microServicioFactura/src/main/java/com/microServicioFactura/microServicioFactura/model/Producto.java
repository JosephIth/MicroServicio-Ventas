package com.microServicioFactura.microServicioFactura.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRODUCTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @Column(columnDefinition = "INT(3)")
    private int codigoProducto;           

    @Column(length = 70, nullable = false)
    private String nombreProducto;

    @Column(length = 200)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;
}