# 💰 Finance Tracker API

API REST para controle de despesas pessoais desenvolvida com **Spring Boot**.  
O projeto tem como objetivo praticar fundamentos de **back-end em Java**, como arquitetura em camadas, DTOs, validações e boas práticas REST.

---

## 📋 Sobre

Sistema simples de gerenciamento financeiro pessoal que permite cadastrar, consultar e organizar **receitas e despesas**, além de calcular automaticamente o **saldo atual** e totais por tipo.

---

## 🚀 Funcionalidades

- CRUD de transações financeiras  
- Categorização de receitas e despesas  
- Filtros por tipo, categoria e período  
- Cálculo de saldo atual  
- Cálculo de total por tipo de transação  
- Validações de dados com Bean Validation  

---

## 🛠️ Tecnologias

- Java 17+  
- Spring Boot 3.x  
- Spring Data JPA  
- H2 Database (em memória)  
- Lombok  
- Maven  

---

## 📡 Endpoints Principais

### Transações

| Método | Endpoint               |
|-------|------------------------|
| POST  | `/api/transacoes`      |
| GET   | `/api/transacoes`      |
| GET   | `/api/transacoes/{id}` |
| PUT   | `/api/transacoes/{id}` |
| DELETE| `/api/transacoes/{id}` |

### Filtros e Cálculos

| Método | Endpoint |
|------|----------|
| GET | `/api/transacoes/tipo/{tipo}` |
| GET | `/api/transacoes/categoria/{categoria}` |
| GET | `/api/transacoes/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` |
| GET | `/api/transacoes/saldo` |
| GET | `/api/transacoes/total/tipo/{tipo}` |

---

## 🔧 Como Executar

**Pré-requisitos:** Java 17+ e Maven

```bash
git clone https://github.com/seu-usuario/finance-tracker-api.git
cd finance-tracker-api
mvn spring-boot:run

