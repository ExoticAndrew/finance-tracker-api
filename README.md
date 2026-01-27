# Finance Tracker API

API REST para controle de despesas pessoais desenvolvida com Spring Boot.

Este projeto tem como objetivo praticar fundamentos de desenvolvimento back-end em Java, incluindo arquitetura em camadas, uso de DTOs, validações, organização de código e aplicação de boas práticas REST.

---

## Sobre o Projeto

Sistema de gerenciamento financeiro pessoal que permite cadastrar, consultar e organizar receitas e despesas, além de realizar cálculos automáticos como saldo atual e totais por tipo de transação.

---

## Funcionalidades

- CRUD de transações financeiras  
- Categorização de receitas e despesas  
- Filtros por tipo, categoria e período  
- Cálculo automático do saldo atual  
- Cálculo de totais por tipo de transação  
- Validações de dados com Bean Validation  

---

## Tecnologias Utilizadas

- Java 17+  
- Spring Boot 3.x  
- Spring Data JPA  
- H2 Database (em memória)  
- Lombok  
- Maven  

---

## Endpoints Principais

### Transações

| Método | Endpoint |
|------|---------|
| POST | `/api/transacoes` |
| GET | `/api/transacoes` |
| GET | `/api/transacoes/{id}` |
| PUT | `/api/transacoes/{id}` |
| DELETE | `/api/transacoes/{id}` |

---

### Filtros e Cálculos

| Método | Endpoint |
|------|---------|
| GET | `/api/transacoes/tipo/{tipo}` |
| GET | `/api/transacoes/categoria/{categoria}` |
| GET | `/api/transacoes/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` |
| GET | `/api/transacoes/saldo` |
| GET | `/api/transacoes/total/tipo/{tipo}` |

---

## Como Executar o Projeto

### Pré-requisitos

- Java 17 ou superior  
- Maven  

### Passos para execução

```bash
git clone https://github.com/ExoticAndrew/finance-tracker-api.git
cd finance-tracker-api
mvn spring-boot:run


