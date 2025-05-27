package com.microServicioFactura.microServicioFactura.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity  // Marca la clase como entidad JPA.
@Table(name= "Venta")  // Nombre de la tabla en la base de datos.
@Data  // getters - setters - equals - hashCode - toString.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor  // Genera un constructor con un argumento.
public class Venta {

    @Id  // DESIGNA PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // EL ID AUTOINCREMENTAL
    private Integer id; // ID DE LA VENTA

    @Column(nullable=false)//ESTA COLUMMNA NO PUEDE SER NULA 
    private String clienteRut;//RUT CLIENTE

    @Column(nullable=false)//ESTA COLUMMNA NO PUEDE SER NULA
    private String encargadoVenta;//ENCARGADO DE LA VENTA

    @Column(nullable=true)//ESTA COLUMNA PUEDE SER NULA
    private Date fechaVenta;//FECHA EN QUE SE REALIZO LA VENTA

    

    //upupup
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();
}

