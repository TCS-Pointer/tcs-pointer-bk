# Implementação 2FA - Pointer

## Visão Geral

O sistema Pointer agora suporta autenticação de dois fatores (2FA) usando Google Authenticator. Esta implementação oferece uma camada adicional de segurança para os usuários.

## Funcionalidades

### 1. Configuração do 2FA
- **Endpoint**: `POST /api/2fa/setup/{keycloakId}`
- **Descrição**: Configura o 2FA para um usuário, gerando chave secreta e QR Code
- **Resposta**: Retorna QR Code URL e chave secreta

### 2. Verificação de Status
- **Endpoint**: `GET /api/2fa/status/{keycloakId}`
- **Descrição**: Verifica se o 2FA está habilitado ou configurado
- **Resposta**: Status atual do 2FA

### 3. Ativação do 2FA
- **Endpoint**: `POST /api/2fa/activate/{keycloakId}`
- **Descrição**: Ativa o 2FA após verificação do código inicial
- **Body**: `TwoFactorVerifyDTO` com código TOTP

### 4. Verificação durante Login
- **Endpoint**: `POST /api/2fa/verify`
- **Descrição**: Verifica código 2FA durante processo de login
- **Body**: `TwoFactorVerifyDTO` com email e código

### 5. Desabilitação
- **Endpoint**: `DELETE /api/2fa/disable/{keycloakId}`
- **Descrição**: Desabilita o 2FA para um usuário

## Fluxo de Configuração

1. **Setup**: Usuário chama `/setup` para gerar QR Code
2. **Escaneamento**: Usuário escaneia QR Code no Google Authenticator
3. **Verificação**: Usuário testa código no app
4. **Ativação**: Usuário chama `/activate` com código válido
5. **Confirmação**: Sistema envia email confirmando ativação

## Fluxo de Login com 2FA

1. **Login Normal**: Usuário faz login no Keycloak
2. **Verificação 2FA**: Sistema verifica se 2FA está habilitado
3. **Código TOTP**: Usuário fornece código do Google Authenticator
4. **Validação**: Sistema valida código via `/verify`
5. **Acesso**: Se válido, usuário acessa o sistema

## Segurança

- Chaves secretas são armazenadas de forma segura no banco
- Códigos TOTP expiram a cada 30 segundos
- Todas as operações são auditadas via logs

## Dependências

- `com.warrenstrange:googleauth:1.5.0` - Biblioteca Google Authenticator
- SendGrid - Para envio de emails de confirmação

## Configuração

### Banco de Dados
Execute o script de migração:
```sql
-- Adicionar colunas para 2FA
ALTER TABLE usuario 
ADD COLUMN two_factor_enabled BOOLEAN NOT NULL DEFAULT FALSE,
ADD COLUMN secret_key VARCHAR(32);
```

### SendGrid Template
Crie um template no SendGrid para emails de ativação 2FA:
- Template ID: `d-2fa-activation-template`
- Variáveis: `{{nome}}`

## Exemplos de Uso

### Configurar 2FA
```bash
curl -X POST /api/2fa/setup/user123 \
  -H "Authorization: Bearer token"
```

### Verificar Status
```bash
curl -X GET /api/2fa/status/user123 \
  -H "Authorization: Bearer token"
```

### Ativar 2FA
```bash
curl -X POST /api/2fa/activate/user123 \
  -H "Content-Type: application/json" \
  -d '{"code": 123456}'
```

### Verificar durante Login
```bash
curl -X POST /api/2fa/verify \
  -H "Content-Type: application/json" \
  -d '{"email": "user@example.com", "code": 123456}'
```

## Troubleshooting

### Problemas Comuns

1. **Código inválido**: Verificar se o app está sincronizado
2. **QR Code não funciona**: Verificar se a URL está correta
3. **Email não recebido**: Verificar configuração do SendGrid
4. **Erro de banco**: Verificar se as colunas foram criadas

### Logs

Todos os eventos 2FA são logados com nível INFO:
- Configuração iniciada/concluída
- Ativação/desativação
- Tentativas de verificação
- Erros de validação 