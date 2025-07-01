package com.microServicioFactura.microServicioFactura.controller;

import com.microServicioFactura.microServicioFactura.dto.VentaDetalleTotalDTO;
import com.microServicioFactura.microServicioFactura.dto.VentaRequestDTO;
import com.microServicioFactura.microServicioFactura.model.DetalleVenta;
import com.microServicioFactura.microServicioFactura.model.Producto;
import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.ProductRepository;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Tag(name = "Ventas", description = "Operaciones relacionadas con ventas y facturación")
@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductRepository productoRepository;

    @Operation(summary = "Crear una venta", description = "Crea una nueva venta con sus detalles.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
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

    @Operation(summary = "Crear ventas en lote", description = "Crea varias ventas en una sola petición.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Ventas creadas correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
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

    @Operation(summary = "Obtener todas las ventas", description = "Devuelve una lista de todas las ventas registradas.")
    @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida correctamente")
    @GetMapping
    public List<Venta> getAllVentas() {
        return ventaRepository.findAll();
    }

    @Operation(summary = "Obtener una venta por ID", description = "Devuelve una venta específica según su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta encontrada"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> getVentaById(
            @Parameter(description = "ID de la venta a buscar", example = "1")
            @PathVariable Integer id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener detalles de una venta", description = "Devuelve los detalles de una venta específica por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Detalles encontrados"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetalleVenta>> getDetallesByVentaId(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable Integer id) {
        Optional<Venta> venta = ventaRepository.findById(id);
        return venta.map(v -> ResponseEntity.ok(v.getDetalles()))
                    .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar una venta", description = "Actualiza los datos de una venta existente por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venta actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Venta> updateVenta(
            @Parameter(description = "ID de la venta a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody VentaRequestDTO ventaDTO) {
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

    @Operation(summary = "Eliminar una venta", description = "Elimina una venta existente por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Venta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(
            @Parameter(description = "ID de la venta a eliminar", example = "1")
            @PathVariable Integer id) {
        if (!ventaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ventaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener total de una venta", description = "Devuelve el total de una venta específica por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Total calculado correctamente"),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada")
    })
    @GetMapping("/{id}/detalleTotal")
    public ResponseEntity<VentaDetalleTotalDTO> getVentaDetalleConTotal(
            @Parameter(description = "ID de la venta", example = "1")
            @PathVariable Integer id) {
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
