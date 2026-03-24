# Arquitectura del Laboratorio

Arquitectura simple de dos servidores para el taller de seguridad.

1. **Servidor Web (Apache)**: Entrega el archivo HTML de forma estática.
2. **Servidor API (Spring Boot)**: Maneja los datos y la seguridad.

## Seguridad
- Encriptación TLS (HTTPS).
- Hashing de passwords (BCrypt).
- Comunicación asíncrona (AJAX/Fetch).

### Producción vs Local
*   **En Local (Docker)**: Usamos certificados **auto-firmados**. Por eso el navegador dice "no es seguro". Es solo para probar la arquitectura.
*   **En AWS**: Se debe usar **Certbot + Let's Encrypt** con un dominio (ej. DuckDNS). Esto genera certificados válidos que los navegadores sí reconocen sin advertencias.
