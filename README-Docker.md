# Docker Setup - Pointer Back

Este documento explica como configurar e executar o projeto Pointer Back usando Docker.

## Pré-requisitos

- Docker
- Docker Compose

## Configuração

### 1. Variáveis de Ambiente

```bash
cp env.example .env
```

O arquivo `.env` já está configurado para:
- Banco PostgreSQL online (Render)
- Keycloak local (porta 8080)
- SendGrid configurado

### 2. Execução com Docker Compose (Recomendado)

Para desenvolvimento local com todos os serviços:

```bash
# Construir e iniciar todos os serviços
docker-compose up --build

# Executar em background
docker-compose up -d --build

# Parar os serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v
```

### 3. Execução apenas da aplicação

Se você já tem PostgreSQL e Keycloak rodando localmente:

```bash
# Construir a imagem
docker build -t pointer-back .

# Executar o container
docker run -p 8082:8082 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/pointer_db \
  -e SPRING_DATASOURCE_USERNAME=seu_usuario \
  -e SPRING_DATASOURCE_PASSWORD=sua_senha \
  -e KEYCLOAK_AUTH_SERVER_URL=http://host.docker.internal:8080/ \
  pointer-back
```

## Serviços Disponíveis

- **Aplicação Spring Boot**: http://localhost:8082
- **Keycloak**: http://localhost:8080
- **PostgreSQL**: localhost:5432

## Estrutura de Arquivos

- `Dockerfile`: Configuração da imagem Docker
- `.dockerignore`: Arquivos excluídos do build
- `docker-compose.yml`: Apenas a aplicação
- `env.example`: Variáveis de ambiente (banco online + Keycloak local)

## Comandos Úteis

```bash
# Ver logs da aplicação
docker-compose logs pointer-back

# Ver logs de todos os serviços
docker-compose logs

# Acessar o container da aplicação
docker-compose exec pointer-back sh

# Reconstruir apenas a aplicação
docker-compose build pointer-back

# Verificar status dos serviços
docker-compose ps
```

## Troubleshooting

### Problemas de Conexão com Banco

Se a aplicação não conseguir conectar ao PostgreSQL:

1. Verifique se o PostgreSQL está rodando: `docker-compose ps`
2. Verifique os logs: `docker-compose logs postgres`
3. Aguarde alguns segundos após iniciar o PostgreSQL antes de iniciar a aplicação

### Problemas com Keycloak

Se o Keycloak não estiver funcionando:

1. Verifique os logs: `docker-compose logs keycloak`
2. Aguarde o Keycloak inicializar completamente (pode levar alguns minutos)
3. Acesse http://localhost:8080 para verificar se está funcionando

### Problemas de Memória

Se a aplicação estiver consumindo muita memória, ajuste as variáveis JAVA_OPTS no `.env`:

```bash
JAVA_OPTS=-Xmx1024m -Xms512m
```

## Produção

Para produção, considere:

1. Usar secrets do Docker ou Kubernetes para senhas
2. Configurar volumes persistentes para o PostgreSQL
3. Usar um reverse proxy (nginx) para SSL/TLS
4. Configurar monitoramento e logs centralizados
5. Usar health checks para garantir disponibilidade 