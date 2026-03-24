# Laboratorio: Secure Application Design

Este es el laboratorio de Seguridad en Aplicaciones. Consiste en un backend de Spring Boot y un frontend estático.

## Cómo correrlo con Docker
Si tienes Docker Desktop:
```bash
docker-compose up --build
```
Entra a `http://localhost:8080` para ver el login.

## Cómo correrlo Manual
1. **Backend**: 
   ```bash
   cd backend
   mvn spring-boot:run
   ```
2. **Frontend**: Abre `index.html` en el navegador.

## Seguridad aplicada
* **HTTPS**: El backend usa TLS en el puerto 8443.
* **BCrypt**: Las contraseñas se hashean con BCrypt.
* **Docker**: Se usan contenedores para separar los servidores.