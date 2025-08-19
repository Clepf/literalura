# 📚 LiterAlura - Catálogo Digital de Livros Clássicos 📚




## 📋 Índice

- [Funcionalidades](#-funcionalidades)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [API Utilizada](#-api-utilizada)
- [Screenshots](#-screenshots)
- [Pessoas Desenvolvedoras](#-pessoas-desenvolvedoras)

***

## 🎯 Sobre o Projeto

**LiterAlura** é uma aplicação de console desenvolvida como parte do **Challenge Alura + Oracle ONE**. O projeto consiste em um catálogo digital de livros clássicos que consome a API **Gutendx** (Project Gutenberg) para buscar informações de mais de 70.000 livros gratuitos.

A aplicação permite aos usuários buscar livros por título, explorar autores, filtrar por idiomas e visualizar estatísticas do catálogo, tudo através de uma interface de console intuitiva e visualmente atrativa.

### 🎨 Características do Projeto:
- ✅ Interface console com ASCII art profissional
- ✅ Integração com API externa (Gutendx)
- ✅ Persistência de dados com PostgreSQL
- ✅ Arquitetura robusta com Spring Boot
- ✅ Tratamento de erros e validações
- ✅ Queries JPA otimizadas

***

## ⚡ Funcionalidades

### 🔍 **1. Buscar Livro por Título**
- Consulta em tempo real na API Gutendx
- Verificação de duplicatas no banco
- Opção de salvar livros encontrados
- Tratamento para livros não encontrados

### 📚 **2. Listar Todos os Livros**
- Exibição formatada com título, autor, idioma e downloads
- Ordenação alfabética por título
- Contagem total de livros cadastrados

### 👤 **3. Listar Todos os Autores**
- Informações completas: nome, nascimento, morte
- Contagem de livros por autor
- Ordenação alfabética por nome

### 📅 **4. Autores Vivos em Determinado Ano**
- Busca inteligente por período histórico
- Query otimizada considerando datas de nascimento e morte
- Filtro dinâmico por ano específico

### 🌍 **5. Livros por Idioma**
- Filtro por códigos de idioma (pt, en, es, fr, etc.)
- Lista idiomas disponíveis no catálogo
- Busca case-insensitive

### 📊 **6. Estatísticas do Catálogo**
- Totais de livros, autores e idiomas
- Top 3 livros mais baixados
- Métricas consolidadas do acervo

***

## 🛠 Tecnologias Utilizadas

### **Backend & Framework:**
- **Java 24** - Linguagem principal
- **Spring Boot 3.2.3** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Boot Starter Validation** - Validações

### **Banco de Dados:**
- **PostgreSQL 16+** - Banco de dados principal
- **HikariCP** - Pool de conexões
- **Hibernate** - ORM

### **Integração & APIs:**
- **HttpClient** - Cliente HTTP nativo Java 11+
- **Jackson** - Processamento JSON
- **Gutendx API** - API de livros do Project Gutenberg

### **Ferramentas & Build:**
- **Maven** - Gerenciamento de dependências
- **SLF4J + Logback** - Sistema de logs
- **IntelliJ IDEA** - IDE de desenvolvimento

***

## 📋 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- ☕ **Java JDK 17+**
- 🗃️ **PostgreSQL 16+**
- 📦 **Maven 3.8+**
- 💻 **IDE** (IntelliJ IDEA recomendado)

***

## 🚀 Como Executar

### **1. Clone o repositório:**
```bash
git clone https://github.com/Clepf/literalura.git
cd literalura
```

### **2. Configure o banco de dados:**
```sql
-- Criar banco de dados PostgreSQL
CREATE DATABASE literalura;
```

### **3. Configure application.properties:**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
```

### **4. Execute o projeto:**

**Via IDE:**
- Abra o projeto no IntelliJ IDEA
- Execute a classe `LiteraluraApplication`

**Via Maven:**
```bash
mvn spring-boot:run
```

### **5. Interaja com o sistema:**
```
╔══════════════════════════════════════════════════════════════╗
║                         📚 LITERALURA 📚                    ║
╠══════════════════════════════════════════════════════════════╣
║  🔍 1 - Buscar livro por título                              ║
║  📚 2 - Listar todos os livros cadastrados                   ║
║  👤 3 - Listar todos os autores cadastrados                  ║
║  📅 4 - Listar autores vivos em determinado ano              ║
║  🌍 5 - Listar livros por idioma                             ║
║  📊 6 - Exibir estatísticas do catálogo                      ║
║  ❌ 0 - Sair                                                 ║
╚══════════════════════════════════════════════════════════════╝
```

***

## 🏗 Estrutura do Projeto

```
src/main/java/com/literalura/literalura/
├── 📁 dto/                    # Data Transfer Objects
│   ├── AutorDTO.java
│   ├── GutendxResponseDTO.java
│   └── LivroDTO.java
├── 📁 exception/              # Exceções customizadas
│   ├── ApiException.java
│   ├── ValidationException.java
│   └── ...
├── 📁 menu/                   # Interface de usuário
│   └── MenuPrincipal.java
├── 📁 model/                  # Entidades JPA
│   ├── Autor.java
│   └── Livro.java
├── 📁 repository/             # Repositories Spring Data
│   ├── AutorRepository.java
│   └── LivroRepository.java
├── 📁 service/                # Camada de negócio
│   ├── ApiService.java
│   ├── ConversaoService.java
│   └── LiteraluraService.java
└── LiteraluraApplication.java
```

***

## 🌐 API Utilizada

### **Gutendx API**
- **URL Base:** `https://gutendx.com/books/`
- **Descrição:** API gratuita com metadados de livros do Project Gutenberg
- **Recursos:** Mais de 70.000 livros clássicos
- **Funcionalidades:**
    - Busca por título
    - Busca por autor
    - Filtros por idioma
    - Informações de downloads

**Exemplo de requisição:**
```
GET https://gutendx.com/books/?search=dom+casmurro
```

***

## 📸 Screenshots

### **Menu Principal:**
```
╔══════════════════════════════════════════════════════════════╗
║                         📚 LITERALURA 📚                    ║
║              Catálogo Digital de Livros Clássicos            ║
║                     Challenge Alura + ONE                    ║
╚══════════════════════════════════════════════════════════════╝
```

### **Busca de Livros:**
```
🔍 BUSCAR LIVRO POR TÍTULO
============================================================
Digite o título do livro: dom casmurro
🔍 Buscando na API Gutendx...

📚 Livro encontrado:
================================================================================
ID: 55752 | Dom Casmurro | Machado de Assis | pt | 1.615 downloads
================================================================================

💾 Deseja salvar este livro no banco de dados? (s/N): s
✅ Livro salvo com sucesso!
```

### **Estatísticas:**
```
📊 ESTATÍSTICAS DO CATÁLOGO:
==================================================
📚 Total de livros: 5
👤 Total de autores: 5
🌍 Idiomas disponíveis: 2
📋 Idiomas: en, pt

🏆 TOP 3 MAIS BAIXADOS:
   • Moby Dick; Or, The Whale - 115.502 downloads
   • A Room with a View - 108.984 downloads
   • Frankenstein; Or, The Modern Prometheus - 95.343 downloads
==================================================
```

***

## 🎓 Aprendizados

Este projeto foi fundamental para consolidar conhecimentos em:

- **🔧 Spring Boot:** Configuração, injeção de dependências, profiles
- **🗃️ JPA/Hibernate:** Entidades, relacionamentos, queries derivadas
- **🌐 Consumo de APIs:** HttpClient, tratamento de JSON, retry logic
- **🏗️ Arquitetura:** Separação em camadas, DTOs, services
- **🛡️ Tratamento de Erros:** Exceções customizadas, validações
- **📊 PostgreSQL:** Modelagem, índices, queries otimizadas

***

## 👥 Pessoas Desenvolvedoras

| [<img loading="lazy" src="https://avatars.githubusercontent.com/u/88713149?s=400&u=4104bd7a1fb2143ecf5d1470b0c829bc5898c250&v=4" width=115><br><sub>Clepf</sub>](https://github.com/Clepf) |
| :---: |

---

> Este projeto foi desenvolvido como parte do Challenge Oracle Next Education (ONE) e demonstra aplicação prática de Java backend, consumo de APIs REST e programação orientada a objetos.