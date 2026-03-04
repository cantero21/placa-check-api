# 🚗 PlacaCheck API

API REST para registro y búsqueda de vehículos por placa o propietario. Diseñado para uso en parqueaderos y entornos corporativos donde se necesita identificar rápidamente a quién pertenece un vehículo.

## 🛠️ Tecnologías

- **Java 17** + **Spring Boot 3**
- **Spring Security** + **JWT** (autenticación stateless)
- **Spring Data JPA** + **MySQL**
- **Lombok** (reducción de boilerplate)
- **Jakarta Validation** (validación de DTOs)

## 📐 Arquitectura

El proyecto sigue la arquitectura de capas estándar de Spring Boot:

```
Controller → Service → Repository → Entity
     ↕            ↕
    DTOs      Base de datos
```

```
📦 com.placacheck.api
├── config/          → Seguridad y CORS
├── controller/      → Endpoints REST
├── dto/             → Records de Java (Request/Response)
├── entity/          → Entidades JPA
├── exception/       → Manejo global de errores
├── repository/      → Interfaces Spring Data JPA
├── security/        → JWT Provider y filtro de autenticación
└── service/         → Lógica de negocio
```

## 🔐 Seguridad

La API implementa dos niveles de acceso:

| Acción | Acceso | Auth requerida |
|--------|--------|----------------|
| Buscar por placa | Público | No |
| Buscar por propietario | Público | No |
| Listar todos | Público | No |
| Registrar vehículo | Admin | JWT Bearer Token |
| Editar vehículo | Admin | JWT Bearer Token |
| Eliminar vehículo | Admin | JWT Bearer Token |

## 📡 Endpoints

### Autenticación
```
POST /api/auth/login    → Obtener JWT token
```

### Vehículos
```
GET  /api/vehicles                    → Listar todos
GET  /api/vehicles/search?plate=ABC   → Buscar por placa (parcial)
GET  /api/vehicles/search?owner=Juan  → Buscar por propietario (parcial)
POST /api/vehicles                    → Registrar vehículo (auth)
PUT  /api/vehicles/{id}               → Actualizar vehículo (auth)
DELETE /api/vehicles/{id}             → Eliminar vehículo (auth)
```

### Ejemplo de request - Registrar vehículo
```json
{
  "licensePlate": "ABC123",
  "ownerName": "Carlos Pérez",
  "workArea": "Mantenimiento"
}
```

## 🗄️ Modelo de datos

```sql
CREATE TABLE vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    license_plate VARCHAR(10) NOT NULL UNIQUE,
    owner_name VARCHAR(100) NOT NULL,
    work_area VARCHAR(100) NOT NULL
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);
```

## ⚙️ Configuración

### Variables de entorno requeridas

| Variable | Descripción |
|----------|-------------|
| `DB_USERNAME` | Usuario de MySQL |
| `DB_PASSWORD` | Contraseña de MySQL |
| `JWT_SECRET` | Clave secreta para firmar tokens (mín. 32 caracteres) |

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/placa_check_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

## 🚀 Cómo ejecutar

1. Clona el repositorio
```bash
git clone https://github.com/TU_USUARIO/placa-check-api.git
cd placa-check-api
```

2. Crea la base de datos
```sql
CREATE DATABASE placa_check_db;
```

3. Configura las variables de entorno

4. Ejecuta el proyecto
```bash
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`

## 🔗 Frontend

El frontend de esta aplicación está en un repositorio separado: [placa-check-web](https://github.com/TU_USUARIO/placa-check-web)

## 📝 Decisiones técnicas

- **Records de Java** para DTOs: inmutables y sin boilerplate
- **Búsqueda parcial** con `LIKE %term%` e `IgnoreCase` para facilidad de uso
- **Placas en mayúsculas**: se normalizan al guardar con `toUpperCase()`
- **GlobalExceptionHandler**: respuestas de error consistentes en formato JSON
- **DataInitializer**: crea el usuario admin automáticamente al primer arranque
- **JWT stateless**: sin sesiones del lado del servidor
