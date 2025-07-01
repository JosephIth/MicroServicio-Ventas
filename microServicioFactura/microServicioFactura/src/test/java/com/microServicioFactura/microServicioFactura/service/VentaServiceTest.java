package com.microServicioFactura.microServicioFactura.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Optional;

import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class VentaServiceTest {

    @Autowired
    private VentaService ventaService;

    @MockBean
    private VentaRepository ventaRepository;

    @Test
    public void testFindAll() {
        // Simula que el repositorio devuelve una lista con una venta
        when(ventaRepository.findAll()).thenReturn(List.of(new Venta()));
        List<Venta> listaVentas = ventaService.findAll();

        // Verifica que la lista no sea nula y tenga un elemento
        assertNotNull(listaVentas);
        assertEquals(1, listaVentas.size());
    }

    @Test
    public void testFindById() {
        // Simula la búsqueda de una venta por ID
        Integer id = 1;
        Venta venta = new Venta();
        venta.setId(id);

        when(ventaRepository.findById(id)).thenReturn(Optional.of(venta));
        Venta found = ventaService.findById(id);

        // Verifica que la venta encontrada no sea nula y tenga el ID correcto
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testSave() {
        // Simula el guardado de una venta
        Venta venta = new Venta();
        venta.setId(1);

        when(ventaRepository.save(venta)).thenReturn(venta);
        Venta saved = ventaService.save(venta);

        // Verifica que la venta guardada no sea nula y tenga el ID correcto
        assertNotNull(saved);
        assertEquals(1, saved.getId());
    }

    @Test
    public void testDeleteById() {
        // Simula la eliminación de una venta por ID
        Integer id = 1;
        doNothing().when(ventaRepository).deleteById(id);

        ventaService.delete(id);

        // Verifica que el método deleteById del repositorio se haya llamado una vez
        verify(ventaRepository, times(1)).deleteById(id);
    }

    @Test
    public void testGuardarVentas() {
        // Simula el guardado de una lista de ventas
        Venta venta1 = new Venta();
        Venta venta2 = new Venta();
        List<Venta> ventas = List.of(venta1, venta2);

        when(ventaRepository.saveAll(ventas)).thenReturn(ventas);
        List<Venta> result = ventaService.guardarVentas(ventas);

        // Verifica que la lista devuelta no sea nula y tenga dos elementos
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ventaRepository, times(1)).saveAll(ventas);
    }
}
