# 🏋️ Smart Gym Backend - AI-Powered Personal Trainer

![Java](https://img.shields.io/badge/Java-22-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4-green)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![AI](https://img.shields.io/badge/AI-Gemini_Flash-purple)

## 📖 Sobre o Projeto

O **Smart Gym Backend** é uma API robusta desenvolvida para gerenciamento de treinos de academia, com um diferencial inovador: **Integração com Inteligência Artificial**.

Diferente de apps comuns de "CRUD", este sistema atua como um **Personal Trainer Virtual**. Ele utiliza a **Gemini API (Google)** para gerar, adaptar e personalizar treinos com base no perfil biomecânico, objetivos e limitações do usuário.

O projeto preza pela qualidade de código, contando com uma suíte completa de **testes unitários e de integração**.

## 🚀 Funcionalidades Principais

### 🧠 Inteligência Artificial (AI Core)
* **Gerador de Treinos:** Cria rotinas completas (exercícios, séries, cargas) baseadas em objetivo (Hipertrofia, Força, etc.), nível de experiência e dias disponíveis.
* **Adaptador de Rotina:** Recalcula o treino caso o usuário sofra uma lesão ou mude de objetivo (ex: "Torci o tornozelo", a IA remove exercícios de perna e foca em superiores).
* **Chatbot Fitness:** Chat contextual que lembra do histórico do usuário para tirar dúvidas sobre execução e saúde.

### 🛡️ Segurança & Gestão
* **Autenticação JWT:** Login seguro e stateless.
* **Controle de Acesso (RBAC):** Níveis de permissão `USER` e `ADMIN`.
* **Proteção de Dados:** Usuários acessam apenas seus próprios dados (prevenção contra IDOR).

### 🧪 Qualidade & Testes
* **Testes Unitários:** Validação isolada das regras de negócio e serviços usando **Mockito**.
* **Testes de Integração:** Validação dos fluxos completos (Controller -> Service -> Repository) utilizando banco de dados em memória (**H2 Database**).

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 22
* **Framework:** Spring Boot
* **Banco de Dados:** PostgreSQL (Produção/Docker) & H2 (Testes)
* **AI Integration:** Google Gemini 2.5 Flash via OpenFeign
* **Testes:** JUnit 5, Mockito, AssertJ
* **Segurança:** Spring Security + JJWT
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Infraestrutura:** Docker & Docker Compose

## 🔧 Como Rodar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.
* Uma chave de API do Google Gemini (AI Studio).

### Passo a Passo

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/seu-usuario/smart-gym-backend.git](https://github.com/seu-usuario/smart-gym-backend.git)
   cd smart-gym-backend

2. **Configure as Variáveis de Ambiente:**
- Crie um arquivo .env na raiz (baseado no application.properties.example):
  ```
  POSTGRES_PASSWORD=sua_senha_db
  JWT_SECRET=sua_chave_secreta_jwt
  GEMINI_API_KEY=sua_chave_api_google

3. **Suba os containers com Docker Compose:**
   ```bash
   docker-compose up -d --build

4. **Acesse a Documentação da API:**
- Abra no navegador:
  http://localhost:8080/swagger-ui.html

## 🐳 Estrutura do Docker Compose
  ``` yaml
  version: '3.9'
  services:
    app:
      build: .
      ports:
        - "8080:8080"
      environment:
        - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/gym_app
        - SPRING_DATASOURCE_USERNAME=postgres
        - SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}
        - GEMINI_API_KEY=${GEMINI_API_KEY}
        - APP_UPLOAD_DIR=/app/uploads
      volumes:
        - ./uploads:/app/uploads
      depends_on:
        - db

    db:
      image: postgres:15
      environment:
        POSTGRES_DB: gym_app
        POSTGRES_USER: postgres
        POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      ports:
        - "5432:5432"
      volumes:
        - postgres_data:/var/lib/postgresql/data
```
## 📐 Arquitetura
**O projeto segue os princípios de Clean Architecture e SOLID:**
- Controllers: Apenas recebem requisições e validam DTOs.
- Services: Contêm toda a regra de negócio e são cobertos por testes unitários.
- Repositories: Abstração do acesso a dados.
- AI Provider Pattern: A integração com a IA é isolada por interfaces.

## 📝 Licença
Este projeto está sob a licença MIT.
