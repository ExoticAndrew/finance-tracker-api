# FinTrack — API de Controle de Finanças Pessoais

API REST para gestão de finanças pessoais, com autenticação segura via JWT, isolamento de dados por usuário e cálculos automáticos de saldo e totais.

🔗 **[Ver projeto no ar](https://finance-tracker-front-theta.vercel.app)** · **[Repositório do front-end](https://github.com/ExoticAndrew/finance-tracker-front)**

> ⚠️ O back-end está hospedado no plano gratuito do Render, que "hiberna" após 15 min de inatividade. A primeira requisição pode levar ~50s para "acordar" o servidor.

---

## 🖥️ Preview

<!-- Adicione aqui os prints. Sugestão: dashboard, tela de login e o Swagger rodando localmente. -->
<!-- Ex: ![Dashboard](docs/dashboard.png) -->

*(screenshots em breve)*

---

## ⚙️ Tecnologias

**Back-end**
- Java 21, Spring Boot 3.4.4
- Spring Security + JWT (autenticação stateless)
- Spring Data JPA / Hibernate
- PostgreSQL (produção) · H2 (desenvolvimento)
- Bucket4j (rate limiting)
- Docker

**Testes**
- JUnit 5, Mockito, AssertJ

**Front-end**
- Angular, TypeScript, SCSS, Chart.js

**Deploy**
- Render (API + PostgreSQL) · Vercel (front-end)

---

## ✨ Funcionalidades

- Autenticação e cadastro de usuários com JWT e Spring Security
- Hash de senhas com BCrypt
- Isolamento de dados por usuário — cada um acessa apenas as próprias transações
- CRUD completo de transações (receitas e despesas)
- Categorização e filtros por tipo, categoria e período
- Paginação em todas as listagens
- Cálculo automático de saldo e totais por tipo
- Resumo mensal para o dashboard
- Rate limiting nos endpoints de autenticação (proteção contra força bruta)
- Tratamento global de exceções com status HTTP semânticos (400, 401, 409)

---

## 🔐 Decisões técnicas

Algumas escolhas conscientes de arquitetura e segurança:

- **Autenticação stateless com JWT:** a API não guarda sessão em memória; cada requisição carrega o token. Isso facilita escalabilidade horizontal.
- **Mensagem anti-enumeração no login:** email inexistente e senha errada retornam a mesma resposta ("Email ou senha inválidos"), para não revelar quais emails estão cadastrados.
- **Swagger desabilitado em produção:** a documentação interativa fica disponível apenas em ambiente de desenvolvimento. Em produção é desativada, para não expor a estrutura da API publicamente.
- **Perfis separados (`local`/`prod`):** H2 em memória no desenvolvimento, PostgreSQL em produção — configurado via Spring Profiles e variáveis de ambiente, sem credenciais no código.
- **Tratamento de erro semântico:** exceções específicas mapeadas para os status HTTP corretos (401 para credenciais, 409 para email duplicado, 400 para validação), em vez de 500 genérico.

---

## 🚀 Como rodar localmente

### Pré-requisitos
- Java 21
- Node.js + Angular CLI (para o front-end)

### Back-end

```bash
git clone https://github.com/ExoticAndrew/finance-tracker-api.git
cd finance-tracker-api/demo
./mvnw spring-boot:run
```

> A aplicação sobe no perfil `local` (banco H2 em memória) em `http://localhost:8080`.
> Alternativamente, é possível rodar pela IDE executando a classe `ControleDeDespesasPessoaisApplication`.

### Front-end

```bash
git clone https://github.com/ExoticAndrew/finance-tracker-front.git
cd finance-tracker-front
npm install
ng serve
```

> O front-end abre em `http://localhost:4200`.

---

## 📚 Endpoints

### Autenticação

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/auth/cadastro` | Cria uma conta e retorna o token |
| `POST` | `/api/auth/login` | Autentica e retorna o token |

### Transações
> Todas as rotas de transações exigem o token JWT no header `Authorization: Bearer <token>`.

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/transacoes` | Cria uma transação |
| `GET` | `/api/transacoes` | Lista todas (paginado) |
| `GET` | `/api/transacoes/{id}` | Busca por ID |
| `PUT` | `/api/transacoes/{id}` | Atualiza |
| `DELETE` | `/api/transacoes/{id}` | Remove |

### Filtros e cálculos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/transacoes/tipo/{tipo}` | Filtra por tipo (paginado) |
| `GET` | `/api/transacoes/categoria/{categoria}` | Filtra por categoria (paginado) |
| `GET` | `/api/transacoes/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` | Filtra por período (paginado) |
| `GET` | `/api/transacoes/saldo` | Saldo atual |
| `GET` | `/api/transacoes/total/tipo/{tipo}` | Total por tipo |
| `GET` | `/api/transacoes/resumo/mensal?ano=YYYY` | Resumo mensal do ano |

---


