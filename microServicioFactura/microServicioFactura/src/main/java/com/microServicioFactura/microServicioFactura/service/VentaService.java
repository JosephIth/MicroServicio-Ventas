package com.microServicioFactura.microServicioFactura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.microServicioFactura.microServicioFactura.model.Venta;
import com.microServicioFactura.microServicioFactura.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    public List<Venta> findAll(){
        return ventaRepository.findAll();
    }


    /*PERMITE BUSCAR VENTA POR ID Y PUT*/
    public Venta findById(Integer id){
        return ventaRepository.findById(id).get();

    }

    public Venta save(Venta venta){
        return ventaRepository.save(venta);
    }

    public void delete(Integer id){
        ventaRepository.deleteById(id);
    }

    public List<Venta> guardarVentas(List<Venta> ventas){
        return ventaRepository.saveAll(ventas);
    }

}
