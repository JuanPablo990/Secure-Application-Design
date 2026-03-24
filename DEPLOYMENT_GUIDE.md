# Guía de Despliegue en AWS (Paso a Paso)

Esta guía te ayudará a pasar de tu entorno Local (Docker) al despliegue real en la nube con un certificado válido (sin advertencias).

## 1. Preparar el Dominio en DuckDNS
1.  Entra a [DuckDNS.org](https://www.duckdns.org/) e inicia sesión.
2.  Crea un subdominio (ej: `misitearep`). Tu dominio será `misitearep.duckdns.org`.
3.  Copia la **IP pública** de tu instancia EC2 de AWS y ponla en el campo "IP" de DuckDNS. Haz clic en "Update IP".

## 2. Configurar el Servidor en AWS (EC2)
1.  Lanza una instancia EC2 (Amazon Linux 2023 o Ubuntu).
2.  **Importante**: En el **Security Group** (Reglas de entrada), abre los puertos:
    *   `22` (SSH) para conectarte.
    *   `80` y `443` (HTTP/HTTPS) para el servidor Apache.
    *   `8443` (TCP Custom) para la API de Spring Boot.

## 3. Instalar y Configurar Apache (Frontend)
Conéctate por SSH a tu instancia y ejecuta:
```bash
# Instalar Apache y SSL
sudo dnf install httpd mod_ssl -y
sudo systemctl start httpd
sudo systemctl enable httpd

# Copiar tu archivo index.html (puedes usar scp o git)
# Supongamos que lo pones en la carpeta por defecto:
sudo cp index.html /var/www/html/
```

## 4. Obtener Certificados con Certbot (Let's Encrypt)
Usa Certbot para obtener el certificado oficial que quitará el mensaje de "No es seguro":
```bash
# Instalar Certbot
sudo dnf install certbot python3-certbot-apache -y

# Obtener certificado para Apache automáticamente
# Reemplaza 'tudominio' por el que creaste en DuckDNS
sudo certbot --apache -d tudominio.duckdns.org
```
*Sigue las instrucciones en pantalla. Esto hará que `https://tudominio.duckdns.org` ya tenga el candado verde.*

## 5. Configurar Certificados en Spring Boot (Backend)
Spring Boot necesita un archivo `.p12`. Como Certbot genera archivos `.pem`, hay que convertirlos:
```bash
# Convertir certificados de PEM a PKCS12
sudo openssl pkcs12 -export \
    -in /etc/letsencrypt/live/tudominio.duckdns.org/fullchain.pem \
    -inkey /etc/letsencrypt/live/tudominio.duckdns.org/privkey.pem \
    -out secure-app.p12 -name secureapp -password pass:password
```
Luego, en el servidor, actualiza tu `application.properties` (o el JAR) para usar ese archivo:
```properties
server.port=8443
server.ssl.key-store=secure-app.p12
server.ssl.key-store-password=password
server.ssl.key-store-type=PKCS12
server.ssl.key-alias=secureapp
```

## 6. Actualizar el Frontend (Último paso)
En tu archivo `index.html` del servidor, asegúrate de que la URL del `fetch` apunte a tu dominio:
`const response = await fetch('https://tudominio.duckdns.org:8443/api/login', ...);`

## 7. Ejecutar y Verificar
1. Inicia tu JAR de Spring Boot: `java -jar secure-app.jar`.
2. Entra a `https://tudominio.duckdns.org`.
3. ¡Ya tendrás el candado verde y todo funcionará de forma segura!
