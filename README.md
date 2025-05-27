# MicroServicioFactura

Microservicio REST para la gestión de ventas, productos y detalles de venta. Permite crear, consultar, actualizar y eliminar ventas, así como calcular el total de cada venta. Desarrollado con Spring Boot, JPA y Oracle.

---

## Tabla de Contenidos
- [IMPORTANTE](#Importante)
- [Características](#características)
- [Requisitos](#requisitos)
- [Configuración](#configuración)
- [Ejecución](#ejecución)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Endpoints](#endpoints)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Autores](#autores)

---

## Importante
- SE DEBEN INGRESAR LOS PRODUCTOS A MANO EN LA BASE DE DATOS, ANTES DE HACER CUALQUIER POST.

## Características

- CRUD de ventas y productos.
- Registro de detalles de venta (producto y cantidad).
- Cálculo automático del total de la venta.
- Endpoints para operaciones en lote.
- Documentación clara y código limpio con Lombok.

---

## Requisitos

- Java 17+
- Maven 3.8+
- Oracle Database (o compatible)
- Wallet de conexión configurado

---

## Configuración

Edita el archivo [`src/main/resources/application.properties`](src/main/resources/application.properties):

```properties
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@<tu-url>?TNS_ADMIN=<ruta-wallet>
spring.datasource.username=ADMIN
spring.datasource.password=TuPassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
```

---

## Ejecución

```bash
mvn clean install
mvn spring-boot:run
```

---

## Estructura del Proyecto

- `controller/` - Controladores REST
- `model/` - Entidades JPA
- `repository/` - Repositorios Spring Data JPA
- `dto/` - Objetos de transferencia de datos (DTO)
- `service/` - Lógica de negocio (opcional)

---

## Endpoints

### Ventas

- **POST** `/ventas`  
  Crea una venta.

- **POST** `/ventas/lote`  
  Crea varias ventas en lote.

- **GET** `/ventas`  
  Lista todas las ventas.

- **GET** `/ventas/{id}`  
  Obtiene una venta por ID.

- **GET** `/ventas/{id}/detalles`  
  Obtiene los detalles de una venta.

- **GET** `/ventas/{id}/detalleTotal`  
  Obtiene los detalles y el total de una venta.

- **PUT** `/ventas/{id}`  
  Actualiza una venta.

- **DELETE** `/ventas/{id}`  
  Elimina una venta.

---

## Ejemplos de Uso

### Crear una venta

```json
POST /ventas
{
  "clienteRut": "12345678-9",
  "encargadoVenta": "Juan Perez",
  "fechaVenta": "2024-05-26",
  "detalles": [
    { "productoId": 1, "cantidad": 2 }
  ]
}
```

### Crear ventas en lote

```json
POST /ventas/lote
[
  {
    "clienteRut": "11111111-1",
    "encargadoVenta": "Ana Torres",
    "fechaVenta": "2024-05-29",
    "detalles": [
      { "productoId": 2, "cantidad": 3 }
    ]
  },
  {
    "clienteRut": "22222222-2",
    "encargadoVenta": "Luis Rojas",
    "fechaVenta": "2024-05-30",
    "detalles": [
      { "productoId": 1, "cantidad": 1 }
    ]
  }
]
```

### Respuesta de `/ventas/{id}/detalleTotal`

```json
{
  "ventaId": 1,
  "detalles": [
    {
      "id": 1,
      "producto": {
        "codigoProducto": 1,
        "nombreProducto": "Producto A",
        "descripcion": "Ejemplo",
        "precio": 2500.0
      },
      "cantidad": 2
    }
  ],
  "total": 5000.0
}
```

---

## Códigos de Estado

- **200 OK**: Consulta o actualización exitosa.
- **201 Created**: Recurso creado (por convención en POST).
- **204 No Content**: Eliminación exitosa.
- **400 Bad Request**: Datos inválidos.
- **404 Not Found**: Recurso no encontrado.
- **500 Internal Server Error**: Error inesperado.

---

## Autores

- [Tu Nombre]
- [Colaboradores]

---
