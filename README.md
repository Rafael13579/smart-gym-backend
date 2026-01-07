# 🏋️ Gym App Backend

Backend de uma aplicação de academia desenvolvido com **Spring Boot**, focado em **boas práticas**, **segurança**, **modelagem correta de domínio** e **organização por camadas**.

O sistema permite que usuários criem treinos personalizados a partir de exercícios cadastrados por administradores, registrando séries, cargas e repetições.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- PostgreSQL
- Maven
- Lombok
- JUnit + Mockito

---

## 📐 Modelagem do Domínio

### Entidades principais:

- **User**
- **Workout**
- **Exercise**
- **WorkoutExercise** (entidade de associação)
- **TrainingSet**

### Relacionamentos importantes:

- Um **User** possui vários **Workouts**
- Um **Workout** pode conter vários **Exercises**
- A relação Workout ↔ Exercise é feita via **WorkoutExercise**
- Um **TrainingSet** pertence a um **WorkoutExercise**

👉 A entidade `WorkoutExercise` permite reutilizar exercícios em diferentes treinos sem duplicação de dados.

---

## 🔐 Autenticação e Autorização

- Autenticação via **JWT**
- Usuários comuns:
  - Criam e gerenciam seus próprios treinos
  - Adicionam exercícios existentes aos treinos
  - Registram séries (peso e repetições)
- Administradores:
  - Criam e removem exercícios do banco global
- Todas as operações validam **ownership do usuário**

---

## 📡 Principais Endpoints

### 🏋️ Workouts
- POST /workouts
- GET /workouts
- PUT /workouts/{id}
- DELETE /workouts/{id}

### 🏃 Exercises (ADMIN)
- POST /exercises
- DELETE /exercises/{id}

### 🔗 Workout ↔ Exercise
- POST /workouts/{workoutId}/exercises/{exerciseId}
- DELETE /workouts/{workoutId}/exercises/{exerciseId}

### 📊 Training Sets
- POST /workouts/{workoutId}/exercises/{exerciseId}/sets
- GET /workout-exercises/{id}/sets
- PUT /sets/{id}
- DELETE /sets/{id}

---

## ▶️ Como Rodar o Projeto

### Pré-requisitos:
- Java 21
- PostgreSQL
- Maven

### Passos:
````bash
git clone https://github.com/seu-usuario/seu-repo.git
cd seu-repo
mvn spring-boot:run
- Configure o banco de dados em application.yml ou application.properties.
`````


## 📌 Status do Projeto

- ✅ Funcional
- 🚧 Em evolução

## Próximos passos planejados:
- Testes de integração
- Autocomplete na busca de exercícios
- Documentação com Swagger
- Paginação e ordenação
- integração com IA


### 👨‍💻 Autor
### Desenvolvido por Rafael Fernandes
### Projeto para fins de estudo e portfólio backend.

  
