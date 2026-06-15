# Plano de Implementação — LogView MVP

## 1. Arquitetura proposta

O backend segue **Clean Architecture** com quatro camadas:

- **web** — Controllers REST, resolução de tenant, tratamento global de exceções
- **application** — Casos de uso (`UploadLogFileUseCase`, `ProcessLogFileUseCase`) e DTOs
- **domain** — Entidades, enums, ports (interfaces), parser e exceções de domínio
- **infrastructure** — Adapters MongoDB, storage local, parser, notificações e configurações

**Regra de dependência:** `web` → `application` → `domain` ← `infrastructure`

**Stack:** Java 25, Spring Boot 4.0.6, Spring Data MongoDB, Jakarta Validation, OpenAPI/Swagger, Maven, Docker.

**Decisão de versão:** A especificação original cita Java 21 / Spring Boot 3.5+. O projeto mantém Java 25 / Spring Boot 4.0.6 conforme inicializado no `pom.xml`.

### Mapeamento DER → MongoDB

| DER (PostgreSQL) | Documento MongoDB (MVP) | Uso no MVP |
|------------------|-------------------------|------------|
| `empresas` | `tenantCnpj` (string) | Tenant via header `X-Tenant-Cnpj` |
| `sistemas` | `sistemaId` (nullable) | Campo reservado |
| `execucoes_varredura` | campos em `LogFile` | status, timestamps, contadores |
| `logs` | metadados + path em `LogFile` | Arquivo bruto em disco |
| `erros` | `LogOccurrence` | Ocorrências detectadas pelo parser |
| `notificacoes` | `Notification` | Resumo pós-processamento |

| MVP (`Severity`) | DER (`criticidade_erro_enum`) |
|------------------|-------------------------------|
| `CRITICAL` | `CRITICA` |
| `WARNING` | `MEDIA` |
| `INFO` | `BAIXA` (não persistido no MVP) |

---

## 2. Estrutura de pastas

```
com.logview.logview/
├── LogviewApplication.java
├── domain/
│   ├── model/          LogFile, LogOccurrence, Notification
│   ├── enums/          Severity, LogFileStatus, StatusProcessamento, etc.
│   ├── port/           Repository ports, FileStoragePort, NotificationService
│   ├── parser/         LogParserService
│   └── exception/      Exceções de domínio
├── application/
│   ├── usecase/        UploadLogFileUseCase, ProcessLogFileUseCase
│   └── dto/            UploadLogFileResponse, ProcessLogFileResponse
├── infrastructure/
│   ├── persistence/    Spring Data repos + adapters
│   ├── storage/        LocalFileStorageAdapter
│   ├── parser/         LogParserServiceImpl
│   ├── notification/   InMemoryNotificationService
│   └── config/         CORS, Mongo, Multipart, OpenAPI, StorageProperties
└── web/
    ├── controller/     LogFileController
    ├── resolver/       TenantContextResolver
    └── advice/         GlobalExceptionHandler
```

---

## 3. Entidades MongoDB

### `log_files` — LogFile

| Campo | Tipo | Índice |
|-------|------|--------|
| id | String (UUID) | @Id |
| tenantCnpj | String | @Indexed, compound com createdAt |
| empresaId | String (nullable) | — |
| sistemaId | String (nullable) | — |
| originalFileName | String | — |
| storagePath | String | — |
| fileSizeBytes | long | — |
| status | LogFileStatus | — |
| statusProcessamento | StatusProcessamento | — |
| totalOccurrences | int | — |
| criticalCount | int | — |
| warningCount | int | — |
| createdAt | Instant | @Indexed |
| processedAt | Instant | — |
| mensagemFalha | String | — |

### `log_occurrences` — LogOccurrence

| Campo | Tipo | Índice |
|-------|------|--------|
| id | String | @Id |
| fileId | String | @Indexed, compound com lineNumber |
| tenantCnpj | String | @Indexed, compound com severity |
| lineNumber | int | compound |
| message | String | — |
| severity | Severity | @Indexed |
| detectedAt | Instant | @Indexed |

### `notifications` — Notification

| Campo | Tipo | Índice |
|-------|------|--------|
| id | String | @Id |
| tenantCnpj | String | @Indexed, compound com createdAt |
| fileId | String | — |
| sistemaId | String (nullable) | — |
| canal | CanalNotificacao | — |
| status | StatusNotificacao | — |
| titulo | String | — |
| mensagem | String | — |
| quantidadeErros | int | — |
| quantidadeAlertas | int | — |
| createdAt | Instant | @Indexed |
| enviadaEm | Instant | — |

---

## 4. Casos de uso

### UC-01 — Upload de Arquivo de Log

**Endpoint:** `POST /api/log-files/upload`

1. Extrair `tenantCnpj` do header `X-Tenant-Cnpj`
2. Validar extensão `.log` e tamanho máximo configurável
3. Gerar UUID, salvar arquivo em diretório temporário
4. Persistir metadados no MongoDB
5. Retornar `{ "fileId": "...", "status": "UPLOADED" }`

### UC-02 — Processamento de Arquivo

**Endpoint:** `POST /api/log-files/{id}/process`

1. Buscar `LogFile` por id + tenant
2. Ler arquivo, parsear linha a linha
3. Detectar CRITICAL e WARNING, persistir `LogOccurrence`
4. Atualizar contadores em `LogFile`
5. Gerar notificação com resumo
6. Retornar resumo do processamento

---

## 5. Repositórios

| Port (domain) | Adapter | Spring Data |
|---------------|---------|-------------|
| LogFileRepository | MongoLogFileRepositoryAdapter | SpringDataLogFileRepository |
| LogOccurrenceRepository | MongoLogOccurrenceRepositoryAdapter | SpringDataLogOccurrenceRepository |
| NotificationRepository | MongoNotificationRepositoryAdapter | SpringDataNotificationRepository |
| FileStoragePort | LocalFileStorageAdapter | — |

---

## 6. Controllers

### LogFileController

| Método | Path | Headers | Body |
|--------|------|---------|------|
| POST | `/api/log-files/upload` | X-Tenant-Cnpj | MultipartFile |
| POST | `/api/log-files/{id}/process` | X-Tenant-Cnpj | — |

---

## 7. DTOs

- **UploadLogFileResponse:** `fileId`, `status`
- **ProcessLogFileResponse:** `fileId`, `status`, `criticalCount`, `warningCount`, `totalLines`, `summary`

---

## 8. Estratégia de testes

- Fixture `sample-log.log`: 50 linhas (5 CRITICAL, 10 WARNING, 35 informativas)
- Testes unitários: `LogParserServiceTest`, `UploadLogFileUseCaseTest`, `ProcessLogFileUseCaseTest`, `InMemoryNotificationServiceTest`
- JaCoCo com cobertura mínima de 80% (linhas)
- Exclusões: `LogviewApplication`, `*Config`, DTOs

---

## 9. Estratégia de notificações

Interface `NotificationService` no domain com implementação `InMemoryNotificationService` que:
- Persiste `Notification` no MongoDB
- Registra resumo via log (`log.info`)
- Preparada para futuras implementações: WebSocket, Push Mobile, Firebase, SSE

---

## 10. Estratégia de CORS

Configuração global via `CorsConfig`:
- Origins: `http://localhost:3000`, `http://localhost:5173`, `http://localhost:8080`
- Methods: GET, POST, PUT, DELETE, OPTIONS
- Credentials: true
- Headers expostos: Content-Type, Authorization, X-Tenant-Cnpj

---

## 11. Fluxo completo (upload → notificação)

```
Cliente → POST /upload (file + X-Tenant-Cnpj)
  → TenantContextResolver extrai CNPJ
  → UploadLogFileUseCase valida, salva arquivo, persiste LogFile
  → Retorna fileId + UPLOADED

Cliente → POST /{id}/process
  → ProcessLogFileUseCase busca LogFile
  → Lê arquivo, LogParserService parseia linha a linha
  → Persiste LogOccurrences em batch
  → Atualiza LogFile (PROCESSED)
  → NotificationService gera resumo e persiste Notification
  → Retorna summary JSON
```

---

## 12. Riscos técnicos

| Risco | Mitigação |
|-------|-----------|
| DER relacional vs MongoDB | Mapeamento documentado; campos DER reservados |
| Arquivos grandes em memória | MVP conforme spec; futuro: streaming + @Async |
| Multi-tenant sem auth real | Header X-Tenant-Cnpj; abstração para Clerk |
| Keywords ambíguas | MVP aceita falsos positivos |
| Java 25 / SB 4 vs spec | Manter versões atuais do pom.xml |

---

## 13. Melhorias futuras

1. Auth Clerk/JWT via `clerk_identities`
2. Entidades DER completas (empresas, sistemas, grupos_erro, erros)
3. Agrupamento de erros por fingerprint
4. Pipeline assíncrono para arquivos grandes
5. IA (sugestoes_ia, interacoes_llm)
6. Notificações reais (WebSocket, FCM, e-mail)
7. Varredura periódica (configuracoes_varredura)
8. SDK de integração (tokens_integracao)
9. Relatórios e exportações
10. Testcontainers para testes de integração
