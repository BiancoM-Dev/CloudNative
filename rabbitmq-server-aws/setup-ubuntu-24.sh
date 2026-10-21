#!/bin/bash
# Script de instalacion automatica de Docker y RabbitMQ para Ubuntu 24.04 LTS (AWS EC2 / Azure VM)

set -e

echo "=== 1. Actualizando paquetes ==="
sudo apt update && sudo apt upgrade -y

echo "=== 2. Instalando certificados y utilidades ==="
sudo apt install -y ca-certificates curl gnupg

echo "=== 3. Configurando repositorio oficial de Docker ==="
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

echo "=== 4. Instalando Docker Engine y Docker Compose ==="
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

echo "=== 5. Habilitando e iniciando Docker ==="
sudo systemctl enable --now docker
sudo usermod -aG docker $USER

echo "=== 6. Creando carpeta ~/rabbitmq-lab y compose.yaml ==="
mkdir -p ~/rabbitmq-lab
cat << 'EOF' > ~/rabbitmq-lab/compose.yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq
    restart: always
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
    volumes:
      - rabbitmq_data:/var/lib/rabbitmq

volumes:
  rabbitmq_data:
EOF

echo "=== 7. Levantando RabbitMQ ==="
cd ~/rabbitmq-lab
sudo docker compose pull
sudo docker compose up -d

echo "=== 8. Verificando estado de RabbitMQ ==="
sudo docker compose ps
sudo docker exec rabbitmq rabbitmq-diagnostics ping

IP_PUB=$(curl -s ifconfig.me || hostname -I | awk '{print $1}')
echo "=========================================================="
echo " RabbitMQ instalado correctamente!"
echo " Accede al panel web: http://$IP_PUB:15672"
echo " Usuario: guest  |  Password: guest"
echo " Recuerda abrir los puertos 5672 y 15672 en el Security Group / Firewall."
echo "=========================================================="
