# SkyStay – Backend

## Descripción general

SkyStay Backend es la aplicación de servidor que sustenta la plataforma **SkyStay**. Se trata de una **API RESTful** desarrollada con **Java** y **Spring Boot**, responsable de:

* Gestión de usuarios y autenticación.
* Búsqueda y reserva de vuelos y alojamientos.
* Procesamiento de reservas y generación de documentos (facturas y billetes en PDF).
* Persistencia de datos en base de datos relacional.
* Seguridad (autenticación/autorizarción con JWT).
* Envío de correos electrónicos transaccionales con PDFs adjuntos.

Este proyecto orquesta la lógica de negocio y responde a las peticiones del frontend de SkyStay.

## Funcionalidades principales

### 1. API de vuelos

* `GET /api/flights`: Buscar vuelos por origen, destino, fecha y número de pasajeros.
* `GET /api/flights/{id}`: Detalle de un vuelo (modelo de avión, cabinas, precios).
* `POST /api/flights/{id}/reserve`: Reservar un vuelo (selección de asientos, datos de pasajeros). Genera y envía PDF del billete.

### 2. API de alojamientos

* `GET /api/accommodations`: Listar alojamientos por destino y fecha.
* `GET /api/accommodations/{id}`: Detalle de alojamiento (habitaciones, amenidades, reseñas).
* `POST /api/accommodations/{id}/reserve`: Reservar estancia. Genera y envía factura en PDF.
* `GET /api/accommodations/{id}/reviews`: Consultar reseñas públicas.
* `POST /api/accommodations/{id}/reviews`: Añadir reseña (requiere reserva completada).

### 3. Gestión de usuarios y autenticación

* `POST /api/auth/register`: Crear cuenta de usuario.
* `POST /api/auth/login`: Iniciar sesión y recibir **JWT**.
* `POST /api/auth/recover-password`: Recuperación de contraseña.
* `GET /api/users/me`: Obtener perfil del usuario autenticado.
* `PUT /api/users/me`: Actualizar datos del perfil.

### 4. Favoritos y perfil de usuario

* `GET /api/users/me/favorites`: Listar alojamientos favoritos.
* `POST /api/users/me/favorites/{id}`: Añadir a favoritos.
* `DELETE /api/users/me/favorites/{id}`: Eliminar de favoritos.
* El perfil incluye también reservas y reseñas del usuario.

### 5. Administración (rol ADMIN)

* Gestión de usuarios (`/api/admin/users`).
* Creación y modificación de vuelos, aerolíneas, aeropuertos, aviones, alojamientos.
* Endpoints de analítica (`/api/admin/analytics`).

### 6. Seguridad y notificaciones

* **Spring Security** con JWT para autenticación stateless.
* Encriptación de contraseñas con **BCrypt**.
* Envío de emails con **JavaMail Sender** y plantillas Thymeleaf.
* Generación de PDFs con **OpenHTMLToPDF**.

## Tecnologías utilizadas

* **Lenguaje:** Java 17+
* **Framework:** Spring Boot, Spring MVC, Spring Data JPA, Spring Security
* **Base de datos:** MySQL (producción) / H2 (desarrollo)
* **Bibliotecas:** Hibernate, Lombok, ModelMapper/MapStruct, jjwt, Thymeleaf, OpenHTMLToPDF
* **Contenedores:** Docker & Docker Compose
* **Testing:** JUnit 5, Spring Boot Test
* **Documentación:** OpenAPI/Swagger (opcional)

## Instalación

### Opción A: Con Docker Compose (recomendada)

```bash
# Clonar repositorio
git clone https://github.com/Noogues/SkyStay-Back.git
cd SkyStay-Back

# Levantar servicios
docker compose up --build
```

La API quedará disponible en `http://localhost:8080` y la base de datos en un contenedor MySQL.

### Opción B: Local (JDK + Maven)

```bash
git clone https://github.com/Noogues/SkyStay-Back.git
cd SkyStay-Back

# Configurar datos de conexión en src/main/resources/application-*.properties
# (por ejemplo, application-dev.properties para H2 o MySQL)

# Compilar e instalar dependencias
./mvnw clean install  # Linux/macOS
mvnw.cmd clean install # Windows

# Ejecutar la aplicación
tools:
./mvnw spring-boot:run
# o bien:
java -jar target/skystay-backend.jar
```

## Uso

1. Accede a `http://localhost:8080`.
2. Utiliza **Postman**, **cURL** o la [Swagger UI](http://localhost:8080/swagger-ui/index.html) (si está habilitada) para probar los endpoints.
3. Para endpoints protegidos, incluye el header:

   ```
   Authorization: Bearer <TOKEN_JWT>
   ```

## Enlaces relevantes

* **Repositorio Frontend:** [https://github.com/Rodrigorjc/SkyStay-Front](https://github.com/Rodrigorjc/SkyStay-Front)
* **Especificación OpenAPI:** `http://localhost:8080/v3/api-docs`
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html`

## Autores y créditos

* **Antonio Nogués Gómez (Noogues)** – Desarrollador Backend principal.
* **Rodrigo (Rodrigorjc)** – Colaborador Full Stack principal.

¡Gracias por usar **SkyStay**!
