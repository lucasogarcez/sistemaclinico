# 🏥 Sistema Clínico — Projeto de Programação Web II

![Java](https://img.shields.io/badge/Java-17-red?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)
![License](https://img.shields.io/badge/License-Acadêmico-lightgrey)
![Status](https://img.shields.io/badge/Status-Em%20Desenvolvimento-yellow)

---


## 📚 Sumário
- [👩‍💻 Integrantes](#%E2%80%8D-integrantes)
- [📖 Descrição do Projeto](#-descrição-do-projeto)
- [🎯 Objetivos da Disciplina](#-objetivos-da-disciplina)
- [⚙️ Tecnologias Utilizadas](#️-tecnologias-utilizadas)
- [🧩 Etapas do Projeto](#-etapas-do-projeto)
- [🧠 Modelagem (Diagrama de Classes)](#-modelagem-diagrama-de-classes)
- [🧭 Estrutura Esperada do Projeto](#-estrutura-esperada-do-projeto)
- [🚀 Como Executar o Projeto](#-como-executar-o-projeto)
- [🧾 Licença](#-licença)

---

## 👩‍💻 Integrantes
| Nome | Função |
|------|---------|
| **Lucas Oliveira Garcez** | Desenvolvimento Back-end / Modelagem |
| **Eduardo Rodrigo da Silva Junior** | Desenvolvimento Front-end / Banco de Dados |

---

## 📖 Descrição do Projeto
O **Sistema Clínico** tem como objetivo gerenciar informações de pacientes, médicos e consultas, oferecendo funcionalidades como:

- Cadastro de pacientes, médicos e usuários do sistema;  
- Controle de acesso por papéis (admin, funcionário e médico);  
- Registro de consultas com dados clínicos (peso, altura, pressão arterial, temperatura, etc.);  
- Emissão de relatórios em PDF contendo informações das consultas e laudo de conclusão.  

O sistema será desenvolvido de forma **orientada a objetos** e com **camadas bem definidas**, aplicando os conceitos vistos na disciplina.

---

## 🎯 Objetivos da Disciplina
O projeto visa colocar em prática os conceitos aprendidos em **Programação Web II**, incluindo:

- Mapeamento objeto-relacional com **JPA/Hibernate**;  
- Implementação de **CRUDs completos**;  
- Criação de **controllers, services, repositories e views**;  
- Uso de **migrações com Flyway**;  
- **Validação de dados** no backend;  
- Implementação de **segurança e autenticação** (Spring Security);  
- **Geração de relatórios em PDF** com sub-relatórios;  
- Utilização de **padrões de projeto** e boas práticas de arquitetura (MVC, camadas, etc.);  
- Implementação de **páginas de erro e paginação**.  

---

## ⚙️ Tecnologias Utilizadas
| Categoria | Tecnologias |
|------------|--------------|
| **Linguagem** | Java |
| **Framework Backend** | Spring Boot |
| **ORM / Persistência** | JPA / Hibernate |
| **Banco de Dados** | PostgreSQL |
| **Migrações** | Flyway |
| **Frontend** | Thymeleaf, HTMX, TailwindCSS, HTML, CSS, JavaScript |
| **Relatórios** | JasperReports (ou equivalente) |
| **Segurança** | Spring Security com HTTPS |

---

## 🧩 Etapas do Projeto

### **Etapa 1:**  
**Apresentação do tema, integrantes e diagrama de classes**  
- Criação de um diagrama simplificado de classes (sem atributos/métodos).  
- Identificação das principais entidades e seus relacionamentos.  

### **Etapa 2:**  
**Implementação das classes do sistema**  
- Criação das classes Java de acordo com o diagrama definido na Etapa 1.  

### **Etapa 3:**  
**Mapeamento Objeto-Relacional (JPA)**  
- Implementação das anotações JPA nas classes do modelo.  
- Configuração inicial do banco de dados PostgreSQL e Flyway.  

### **Etapa 4:**  
**CRUD completo**  
- Implementação de controllers, services, repositories e views.  
- Operações de criação, leitura, atualização e exclusão.  

### **Etapa 5:**  
**Lógica de Negócios**  
- Implementação das regras de negócio da aplicação.  
- Validação de dados e integração entre entidades.  

### **Etapa 6:**  
**Segurança do Sistema**  
- Implementação de login, controle de acesso e criptografia de senha.  
- Definição de papéis: *Administrador*, *Funcionário* e *Médico*.  

### **Etapa 7:**  
**Relatório em PDF**  
- Geração de relatório com sub-relatórios (ex.: consultas de um paciente).  
- Exibição via interface web.  

### **Etapa 8:**  
**Apresentação Final**  
- Demonstração do sistema completo ao professor, mostrando:  
  - Classes do modelo  
  - Repositórios e migrações  
  - Controladores e serviços  
  - Views  
  - Lógica de negócios  
  - CRUDs, segurança e relatório  

---

## 🧠 Modelagem (Diagrama de Classes)

O diagrama abaixo representa uma **visão simplificada do modelo do sistema**:

![Diagrama de Classes](src/main/resources/static/images/Modelo_do_Banco_de_Dados.png)

### Entidades principais:
- **Pacientes**
- **Médicos**
- **Consultas**
- **Usuários**
- **Papéis**

**Relacionamentos:**
- Um **usuário** pertence a um **papel** (ex.: médico, admin, funcionário);  
- Um **médico** é vinculado a um **usuário**;  
- Uma **consulta** está associada a um **paciente** e a um **médico**.  

---

## 🧭 Estrutura Esperada do Projeto

```
sistema-clinico/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── sistemaclinico/
│   │   │           └── clinico/
│   │   │               ├── controller/
│   │   │               ├── service/
│   │   │               ├── repository/
│   │   │               ├── model/
│   │   │               └── config/
│   │   └── resources/
│   │       ├── templates/        # Views Thymeleaf
│   │       ├── static/           # CSS, JS e imagens
│   │       └── db/
│   │           └── migration/    # Scripts Flyway
│   └── test/
│
├── pom.xml                       # Dependências Maven
├── README.md                     # Documentação do projeto
└── .gitignore                    # Arquivos ignorados pelo Git
```

---

## 🚀 Como Executar o Projeto
### 1️⃣ **Clonar o repositório:**
  ```bash
  git clone https://github.com/seuusuario/sistema-clinico.git
  ```

### 2️⃣ **Entrar na pasta do projeto:**
  ```bash
  cd sistema-clinico
  ```

### 3️⃣ **Configurar o banco de dados:**
Edite o arquivo `application.properties` com suas credenciais:
  ```bash
  spring.datasource.url=jdbc:postgresql://localhost:5432/sistemaclinico
  spring.datasource.username=postgres
  spring.datasource.password=suasenha
  ```

### 4️⃣ **Executar o projeto:**
  ```bash
  mvn spring-boot:run
  ```

### 5️⃣ **Acessar no navegador:**
  ```bash
  http://localhost:8080
  ```

---

## 🧾 Licença
📘Este projeto foi desenvolvido como parte da disciplina **Programação Web II** do curso de **Engenharia de Computação**.
Uso estritamente **acadêmico**.
