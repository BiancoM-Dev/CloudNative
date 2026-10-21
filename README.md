# Cloud Native: Microservicios con RabbitMQ y MySQL

Sistema de comercio electrónico desacoplado basado en una **Arquitectura Orientada a Eventos (EDA)** y el principio **Database-per-Service**, implementado con **Spring Boot**, **RabbitMQ** y **MySQL**.

---

## 🏛️ Arquitectura del Sistema

```text
               POST /api/v1/ventas (Postman / Cliente)
                                │
                                ▼
 ┌─────────────────────────────────────────────────────────────┐
 │  Microservicio Publicador (Spring Boot - Puerto 8080)       │
 └──────────────┬───────────────────────────────┬──────────────┘
                │ Guarda venta                  │ Publica mensaje con ACK
                ▼                               ▼
 ┌─────────────────────────────┐  ┌─────────────────────────────┐
 │ Base de Datos 1 (MySQL):    │  │ RabbitMQ Broker             │
 │ BD: ventas_publicador       │  │ Exchange: ventas.exchange   │
 └─────────────────────────────┘  │ Routing Key: ventas.creada  │
                                  │ Cola: ventas.queue          │
                                  └─────────────┬───────────────┘
                                                │ Consume mensaje asíncrono
                                                ▼
                                  ┌─────────────────────────────┐
                                  │ Microservicio Consumidor    │
                                  │ (Spring Boot - Puerto 8081) │
                                  └─────────────┬───────────────┘
                                                │ Guarda pedido
                                                ▼
                                  ┌─────────────────────────────┐
                                  │ Base de Datos 2 (MySQL):    │
                                  │ BD: ventas_consumidor       │
                                  └─────────────────────────────┘
```

---

## 📦 Estructura del Repositorio

- `back-rabbitmq-publicador-seccion-1/`: Microservicio Spring Boot que expone el endpoint REST para registrar ventas, persiste en su propia base de datos y publica eventos a RabbitMQ con confirmación de entrega (Publisher Confirms).
- `ventas-consumidor/`: Microservicio Spring Boot que se suscribe a la cola de RabbitMQ y persiste los pedidos en su propia base de datos.
- `mysql-init/`: Script `init.sql` para crear automáticamente las bases de datos de ambos microservicios.
- `rabbitmq-server-aws/`: Configuración y scripts para desplegar RabbitMQ en servidor Ubuntu/AWS EC2.
- `docker-compose.yml`: Orquestación local de contenedores para RabbitMQ (con consola web de gestión) y MySQL.
- `INSTRUCCIONES.txt`: Guía paso a paso en formato texto plano para levantar y probar el sistema.

---

## 🚀 Paso a Paso de Ejecución

### 1. Iniciar Infraestructura Base (RabbitMQ y MySQL)

En la raíz del proyecto, ejecuta:

```bash
docker compose up -d
```

- **RabbitMQ**: AMQP en `localhost:5672`, Consola web en `http://localhost:15672` (usuario: `guest`, clave: `guest`).
- **MySQL**: Puerto `3306` (usuario: `root`, clave: `root`), creando automáticamente `ventas_publicador` y `ventas_consumidor`.

### 2. Iniciar el Microservicio Publicador

```bash
cd back-rabbitmq-publicador-seccion-1
./mvnw spring-boot:run
```
*Disponible en `http://localhost:8080`.*

### 3. Iniciar el Microservicio Consumidor

```bash
cd ../ventas-consumidor
./mvnw spring-boot:run
```
*Inicia en puerto 8081 y queda escuchando la cola `ventas.queue`.*

---

## 🧪 Prueba con Postman / cURL

- **Método**: `POST`
- **URL**: `http://localhost:8080/api/v1/ventas`
- **Headers**: `Content-Type: application/json`
- **Cuerpo (Body JSON)**:

```json
[
  {
    "nombreCliente": "Ana Perez",
    "idCliente": 1001,
    "idProducto": 200,
    "nombreProducto": "Notebook Lenovo",
    "precio": 650000,
    "cantidad": 1
  },
  {
    "nombreCliente": "Ana Perez",
    "idCliente": 1001,
    "idProducto": 201,
    "nombreProducto": "Mouse Logitech",
    "precio": 25000,
    "cantidad": 2
  }
]
```

- **Respuesta esperada**: `201 Created` con el arreglo de productos registrados y sus identificadores asignados.
- **Resultado en segundo plano**: El consumidor procesa el evento desde RabbitMQ y persiste la venta en `ventas_consumidor`.
