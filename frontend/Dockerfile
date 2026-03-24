FROM httpd:alpine

# Instalar openssl para generar el certificado internamente
RUN apk add --no-cache openssl

# Copiar el archivo HTML
COPY index.html /usr/local/apache2/htdocs/

# Generar el certificado TLS auto-firmado dentro del contenedor
RUN openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout /usr/local/apache2/conf/server.key \
    -out /usr/local/apache2/conf/server.crt \
    -subj "/C=CO/ST=DC/L=Bogota/O=Escuela/OU=ID/CN=localhost"

# Habilitar SSL en la configuración de Apache
RUN sed -i \
		-e 's/^#\(Include .*httpd-ssl.conf\)/\1/' \
		-e 's/^#\(LoadModule .*mod_ssl.so\)/\1/' \
		-e 's/^#\(LoadModule .*mod_socache_shmcb.so\)/\1/' \
		conf/httpd.conf

# Ajustar la ruta de los certificados en el archivo de configuración de SSL
RUN sed -i \
		-e 's|^SSLCertificateFile .*|SSLCertificateFile "/usr/local/apache2/conf/server.crt"|' \
		-e 's|^SSLCertificateKeyFile .*|SSLCertificateKeyFile "/usr/local/apache2/conf/server.key"|' \
		conf/extra/httpd-ssl.conf

EXPOSE 80
EXPOSE 443
