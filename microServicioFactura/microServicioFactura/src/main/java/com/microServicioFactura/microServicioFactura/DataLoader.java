package com.microServicioFactura.microServicioFactura;

import net.datafaker.Faker; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.CommandLineRunner; 
import org.springframework.context.annotation.Profile; 
import org.springframework.stereotype.Component;

import com.microServicioFactura.microServicioFactura.repository.ProductRepository;
import com.microServicioFactura.microServicioFactura.model.Producto;


@Profile("dev") 
@Component

public class DataLoader implements CommandLineRunner {


    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();


            // Cargar productos de ejemplo
            for (int i = 0; i < 10; i++) {
                Producto producto = new Producto();
                producto.setCodigoProducto(i + 1);
                producto.setNombreProducto(faker.commerce().productName());
                producto.setDescripcion(faker.lorem().sentence());
                producto.setPrecio(Double.parseDouble(faker.commerce().price()));
                productRepository.save(producto);
            }
        }

}
