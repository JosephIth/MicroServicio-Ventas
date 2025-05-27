package com.microServicioFactura.microServicioFactura.controller;


import com.microServicioFactura.microServicioFactura.dto.VentaDetalleTotalDTO;
import com.microServicioFactura.microServicioFactura.dto.VentaRequestDTO;
import com.microServicioFactura.microServicioFactura.model.DetalleVenta;
import com.microServicioFactura.microServicioFactura.model.Producto;
import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.ProductRepository;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//// Controlador REST para manejar las operaciones de venta
@RestController
//
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductRepository productoRepository;

    //PERMITE CREAR UNA VENTA
    @PostMapping
    public Venta crearVenta(@RequestBody VentaRequestDTO ventaDTO) {
        Venta venta = new Venta();
        venta.setClienteRut(ventaDTO.getClienteRut());
        venta.setEncargadoVenta(ventaDTO.getEncargadoVenta());
        venta.setFechaVenta(ventaDTO.getFechaVenta());

        List<DetalleVenta> detalles = new ArrayList<>();
        for (VentaRequestDTO.DetalleDTO d : ventaDTO.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            Producto producto = productoRepository.findById(d.getProductoId()).orElseThrow();
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            detalles.add(detalle);
        }
        venta.setDetalles(detalles);

        return ventaRepository.save(venta);
    }
    //PERMITE CREAR VENTAS EN LOTE
    @PostMapping("/lote")
    public List<Venta> crearVentasLote(@RequestBody List<VentaRequestDTO> ventasDTO) {
        List<Venta> ventas = new ArrayList<>();
        for (VentaRequestDTO ventaDTO : ventasDTO) {
            Venta venta = new Venta();
            venta.setClienteRut(ventaDTO.getClienteRut());
            venta.setEncargadoVenta(ventaDTO.getEncargadoVenta());
            venta.setFechaVenta(ventaDTO.getFechaVenta());

            List<DetalleVenta> detalles = new ArrayList<>();
            for (VentaRequestDTO.DetalleDTO d : ventaDTO.getDetalles()) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setVenta(venta);
                Producto producto = productoRepository.findById(d.getProductoId()).orElseThrow();
                detalle.setProducto(producto);
                detalle.setCantidad(d.getCantidad());
                detalles.add(detalle);
            }
            venta.setDetalles(detalles);
            ventas.add(venta);
        }
        return ventaRepository.saveAll(ventas);
    }

    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }
    //PERMITE OBTENER UNA VENTA MEDIANTE EL ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(@PathVariable Integer id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }
    //PERMITE OBTENER LOS DETALLES DE UNA VENTA MEDIANTE EL ID
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> getDetallesByVentaId(@PathVariable Integer id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(v -> ResponseEntity.ok(v.getDetalles()))
                    .orElse(ResponseEntity.notFound().build());
    }

    //PERMITE ACTUALIZAR UNA VENTA MEDIANTE EL ID
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(@PathVariable Integer id, @RequestBody VentaRequestDTO ventaDTO) {
        Optional<Venta> optionalVenta = ventaRepository.findById(id);
        if (optionalVenta.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venta venta = optionalVenta.get();
        venta.setClienteRut(ventaDTO.getClienteRut());
        venta.setEncargadoVenta(ventaDTO.getEncargadoVenta());
        venta.setFechaVenta(ventaDTO.getFechaVenta());

        // Limpiar detalles anteriores
        venta.getDetalles().clear();

        
        List<DetalleVenta> nuevosDetalles = new ArrayList<>();
        for (VentaRequestDTO.DetalleDTO d : ventaDTO.getDetalles()) {
            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            Producto producto = productoRepository.findById(d.getProductoId()).orElseThrow();
            detalle.setProducto(producto);
            detalle.setCantidad(d.getCantidad());
            nuevosDetalles.add(detalle);
        }
        venta.getDetalles().addAll(nuevosDetalles);

        return ResponseEntity.ok(ventaRepository.save(venta));
    }

    //PERMITE ELIMINAR UNA VENTA MEDIANTE EL ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Integer id) {
        if (!ventaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ventaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    //PERMITE CALCULAR EL PRECIO TOTAL DE UNA VENTA MEDIANTE EL ID
    @GetMapping("/{id}/detalleTotal")
    public ResponseEntity<VentaDetalleTotalDTO> getVentaDetalleConTotal(@PathVariable Integer id) {
        Optional<Venta> ventaOpt = ventaRepository.findById(id);
        if (ventaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venta venta = ventaOpt.get();
        double total = venta.getDetalles().stream()
            .mapToDouble(det -> det.getProducto().getPrecio() * det.getCantidad())
            .sum();

        VentaDetalleTotalDTO dto = new VentaDetalleTotalDTO();
        dto.setVentaId(venta.getId());
        dto.setDetalles(venta.getDetalles());
        dto.setTotal(total);

        return ResponseEntity.ok(dto);
    }
}
