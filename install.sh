#!/bin/bash
echo "Iniciando Instalación..."
echo "Creando Directorios..."
mkdir /opt/combustible
echo "Copiando archivos..."
cp -R lib /opt/combustible
cp ReseteoCombustible.jar /opt/combustible
cp reseteo.sh /opt/combustible
echo "Asignando permisos..."
chown -R 776 /opt/combustible
echo "Creando log..."
mkdir /var/log/combustible
touch /var/log/combustible/ReseteoCombustible.log
echo "Asignando permisos..."
chmod 777 /var/log/combustible/ReseteoCombustible.log
echo "Fin de la instalación!!!
Por favor creé la entrada en CRON de la siguiente manera:
1.- Ejecuta: crontab -e
2.- Agregar la sig linea al archivo: 10 12 * * 0 /opt/combustible/reseteo.sh
3.- Guarda el archivo."
