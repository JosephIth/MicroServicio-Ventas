package com.microServicioFactura.microServicioFactura.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microServicioFactura.microServicioFactura.dto.VentaRequestDTO;
import com.microServicioFactura.microServicioFactura.model.DetalleVenta;
import com.microServicioFactura.microServicioFactura.model.Producto;
import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.ProductRepository;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class)
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VentaRepository ventaRepository;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Venta venta;
    private Producto producto;
    private DetalleVenta detalleVenta;

    @BeforeEach
    void setUp() {
        // Crea un producto de ejemplo
        producto = new Producto();
        producto.setCodigoProducto(1);
        producto.setNombreProducto("Producto Test");
        producto.setDescripcion("Desc");
        producto.setPrecio(100.0);

        // Crea un detalle de venta de ejemplo
        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(2);

        // Crea una venta de ejemplo
        venta = new Venta();
        venta.setId(1);
        venta.setClienteRut("12345678-9");
        venta.setEncargadoVenta("Juan");
        venta.setFechaVenta(new Date(System.currentTimeMillis()));
        venta.setDetalles(List.of(detalleVenta));
        detalleVenta.setVenta(venta);
    }

    @Test
    public void testGetAllVentas() throws Exception {
        // Simula que el repositorio devuelve una lista de ventas
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        mockMvc.perform(get("/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].clienteRut").value("12345678-9"))
                .andExpect(jsonPath("$[0].encargadoVenta").value("Juan"));
    }

    @Test
    public void testGetVentaById_found() throws Exception {
        // Simula que la venta existe
        when(ventaRepository.findById(1)).thenReturn(Optional.of(venta));

        mockMvc.perform(get("/ventas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clienteRut").value("12345678-9"));
    }

    @Test
    public void testGetVentaById_notFound() throws Exception {
        // Simula que la venta no existe
        when(ventaRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ventas/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearVenta() throws Exception {
        // Simula la creación de una venta
        VentaRequestDTO.DetalleDTO detalleDTO = new VentaRequestDTO.DetalleDTO();
        detalleDTO.setProductoId(1);
        detalleDTO.setCantidad(2);

        VentaRequestDTO ventaDTO = new VentaRequestDTO();
        ventaDTO.setClienteRut("12345678-9");
        ventaDTO.setEncargadoVenta("Juan");
        ventaDTO.setFechaVenta(new Date(System.currentTimeMillis()));
        ventaDTO.setDetalles(List.of(detalleDTO));

        when(productRepository.findById(1)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);

        mockMvc.perform(post("/ventas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ventaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteRut").value("12345678-9"))
                .andExpect(jsonPath("$.detalles[0].cantidad").value(2));
    }

    @Test
    public void testDeleteVenta_found() throws Exception {
        // Simula que la venta existe y se elimina correctamente
        when(ventaRepository.existsById(1)).thenReturn(true);
        doNothing().when(ventaRepository).deleteById(1);

        mockMvc.perform(delete("/ventas/1"))
                .andExpect(status().isNoContent());
        verify(ventaRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteVenta_notFound() throws Exception {
        // Simula que la venta no existe
        when(ventaRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(delete("/ventas/1"))
                .andExpect(status().isNotFound());
        verify(ventaRepository, never()).deleteById(1);
    }

    @Test
    public void testGetVentaDetalleConTotal_found() throws Exception {
        // Simula obtener el total de una venta existente
        when(ventaRepository.findById(1)).thenReturn(Optional.of(venta));

        mockMvc.perform(get("/ventas/1/detalleTotal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ventaId").value(1))
                .andExpect(jsonPath("$.total").value(200.0));
    }

    @Test
    public void testGetVentaDetalleConTotal_notFound() throws Exception {
        // Simula que la venta no existe para el total
        when(ventaRepository.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/ventas/1/detalleTotal"))
                .andExpect(status().isNotFound());
    }
}
