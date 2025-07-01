package com.microServicioFactura.microServicioFactura.controller;

import com.microServicioFactura.microServicioFactura.assemblers.VentaModelAssembler;
import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/v2/ventas")
// http://localhost:8080/api/v2/productos
// http://localhost:8080/api/v2/productos/bulk (para agregar más de un producto)
public class VentaControllerV2 {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private VentaModelAssembler assembler;

    /**
     * Obtiene todas las ventas en formato HAL+JSON con enlaces HATEOAS.
     * @return Colección de ventas con enlaces.
     */
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<Venta>> getAllVentas() {
        List<EntityModel<Venta>> ventas = ventaRepository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        // Devuelve la colección con un enlace a sí misma
        return CollectionModel.of(ventas,
                linkTo(methodOn(VentaControllerV2.class).getAllVentas()).withSelfRel());
    }

    /**
     * Obtiene una venta específica por su ID en formato HAL+JSON.
     * @param id ID de la venta.
     * @return Venta encontrada con enlaces HATEOAS.
     */
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<Venta> getVentaById(@PathVariable Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
        return assembler.toModel(venta);
    }

    /**
     * Crea una nueva venta.
     * @param venta Objeto venta a crear.
     * @return Venta creada con enlaces HATEOAS y cabecera Location.
     */
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Venta>> createVenta(@RequestBody Venta venta) {
        Venta newVenta = ventaRepository.save(venta);
        return ResponseEntity
                .created(linkTo(methodOn(VentaControllerV2.class).getVentaById(newVenta.getId())).toUri())
                .body(assembler.toModel(newVenta));
    }

    /**
     * Actualiza una venta existente por su ID.
     * @param id ID de la venta a actualizar.
     * @param venta Objeto venta con los nuevos datos.
     * @return Venta actualizada con enlaces HATEOAS.
     */
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<Venta>> updateVenta(@PathVariable Integer id, @RequestBody Venta venta) {
        venta.setId(id);
        Venta updatedVenta = ventaRepository.save(venta);
        return ResponseEntity.ok(assembler.toModel(updatedVenta));
    }

    /**
     * Elimina una venta por su ID.
     * @param id ID de la venta a eliminar.
     * @return Respuesta vacía con estado 204 No Content.
     */
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<?> deleteVenta(@PathVariable Integer id) {
        ventaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
