# 🍜 Roots Açaí - Sistema de Gerenciamento

Sistema web completo para gerenciamento de vendas, estoque e finanças de uma franquia de açaí. Desenvolvido com Java, Spring Boot e MySQL.

---

## 📋 Sumário

- [Características](#características)
- [Tech Stack](#tech-stack)
- [Instalação](#instalação)
- [Como Usar](#como-usar)
- [Arquitetura](#arquitetura)
- [API Endpoints](#api-endpoints)
- [Modelos de Dados](#modelos-de-dados)
- [Autor](#autor)

---

## ✨ Características

### 📦 Gestão de Estoque
- ✅ Cadastro de produtos
- ✅ Controle de quantidade em tempo real
- ✅ Alertas de estoque baixo
- ✅ Histórico de movimentações
- ✅ Validade de produtos

### 💰 Gestão Financeira
- ✅ Dashboard de faturamento
- ✅ Relatório de vendas
- ✅ Análise de lucro
- ✅ Gráficos de desempenho
- ✅ Previsão de receita

### 👥 Gestão de Usuários
- ✅ Cadastro de clientes
- ✅ Cadastro de vendedores
- ✅ Gerenciamento de permissões
- ✅ Histórico de atividades

### 📊 Relatórios e Análises
- ✅ Vendas por período
- ✅ Produtos mais vendidos
- ✅ Desempenho de vendedores
- ✅ Exportação de relatórios

---

## 🛠 Tech Stack

### Backend
```
Java 17
Spring Boot 3.5.14
Spring Data JPA
Spring Web
MySQL 8.0+
Maven
```

### Camadas da Aplicação
- **Controller** - REST Controllers
- **Service** - Lógica de negócio
- **Repository** - Acesso a dados (JPA)
- **Entity** - Modelos de domínio
- **DTO** - Transfer de dados

### Dependências Principais
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
</dependency>
```

---

## 📦 Instalação

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8+
- MySQL 8.0+
- Git

### 1. Clone o repositório
```bash
git clone https://github.com/larissaRgj/rootsacai.git
cd rootsacai
```

### 2. Configure o banco de dados

Crie um banco de dados MySQL:
```sql
CREATE DATABASE roots_acai;
USE roots_acai;
```

### 3. Configure as variáveis de ambiente

Crie um arquivo `application.properties` em `src/main/resources/`:

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/roots_acai
spring.datasource.username=root
spring.datasource.password=sua_senha

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Servidor
server.port=8080
server.servlet.context-path=/api
```

### 4. Compile o projeto
```bash
mvn clean install
```

### 5. Inicie a aplicação
```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080/api`

---

## 🚀 Como Usar

### 1. Iniciar o Servidor
```bash
mvn spring-boot:run
```

### 2. Acessar a API
Use ferramentas como Postman ou Insomnia:
- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`

### 3. Exemplos de Requisições

#### Criar Produto
```bash
POST /api/produtos
Content-Type: application/json

{
  "nome": "Açaí Premium",
  "descricao": "Açaí com granola",
  "preco": 25.00,
  "quantidade": 100,
  "categoria": "ACAI"
}
```

#### Listar Produtos
```bash
GET /api/produtos
```

#### Atualizar Produto
```bash
PUT /api/produtos/1
Content-Type: application/json

{
  "nome": "Açaí Premium",
  "preco": 27.00,
  "quantidade": 95
}
```

#### Gerar Relatório
```bash
GET /api/relatorios/vendas?mes=8&ano=2026
```

---

## 🏗 Arquitetura

### Estrutura de Diretórios
```
rootsacai/
├── src/
│   ├── main/
│   │   ├── java/com/rootsacai/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── ProdutoController.java
│   │   │   │   ├── VendaController.java
│   │   │   │   └── RelatorioController.java
│   │   │   │
│   │   │   ├── service/             # Lógica de negócio
│   │   │   │   ├── ProdutoService.java
│   │   │   │   ├── VendaService.java
│   │   │   │   └── RelatorioService.java
│   │   │   │
│   │   │   ├── repository/          # Acesso a dados
│   │   │   │   ├── ProdutoRepository.java
│   │   │   │   ├── VendaRepository.java
│   │   │   │   └── UsuarioRepository.java
│   │   │   │
│   │   │   ├── entity/              # Entidades JPA
│   │   │   │   ├── Produto.java
│   │   │   │   ├── Venda.java
│   │   │   │   └── Usuario.java
│   │   │   │
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   ├── ProdutoDTO.java
│   │   │   │   └── VendaDTO.java
│   │   │   │
│   │   │   ├── exception/           # Tratamento de erros
│   │   │   └── RootsAcaiApplication.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/rootsacai/     # Testes unitários
│
├── pom.xml                          # Dependências Maven
└── README.md
```

---

## 📡 API Endpoints

### Produtos
```
GET    /api/produtos              # Listar todos
POST   /api/produtos              # Criar novo
GET    /api/produtos/{id}         # Obter um
PUT    /api/produtos/{id}         # Atualizar
DELETE /api/produtos/{id}         # Deletar
GET    /api/produtos/estoque/baixo # Alertas
```

### Vendas
```
GET    /api/vendas                # Listar todas
POST   /api/vendas                # Registrar venda
GET    /api/vendas/{id}           # Obter uma
PUT    /api/vendas/{id}           # Atualizar
DELETE /api/vendas/{id}           # Cancelar
```

### Usuários
```
GET    /api/usuarios              # Listar todos
POST   /api/usuarios              # Criar novo
GET    /api/usuarios/{id}         # Obter um
PUT    /api/usuarios/{id}         # Atualizar
DELETE /api/usuarios/{id}         # Deletar
```

### Relatórios
```
GET    /api/relatorios/vendas     # Vendas por período
GET    /api/relatorios/estoque    # Status do estoque
GET    /api/relatorios/financeiro # Análise financeira
GET    /api/relatorios/top-produtos # Produtos mais vendidos
```

---

## 🗃 Modelos de Dados

### Tabela: Produtos
```sql
CREATE TABLE produtos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10, 2),
    quantidade INT,
    categoria VARCHAR(50),
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);
```

### Tabela: Vendas
```sql
CREATE TABLE vendas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    produto_id BIGINT,
    usuario_id BIGINT,
    quantidade INT,
    valor_total DECIMAL(10, 2),
    data_venda TIMESTAMP,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
```

### Tabela: Usuários
```sql
CREATE TABLE usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    senha VARCHAR(255),
    papel VARCHAR(50),
    ativo BOOLEAN,
    data_criacao TIMESTAMP
);
```

---

## 🧪 Testes

### Executar testes
```bash
mvn test
```

### Testes unitários
```bash
mvn test -Dtest=ProdutoServiceTest
```

### Cobertura de testes
```bash
mvn clean test jacoco:report
```

---

## 🔐 Autenticação e Segurança

**Futuro**: Implementar Spring Security com JWT

```java
// Planejado
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // JWT Token validation
    // Role-based access control
}
```

---

## 🎯 Funcionalidades Futuras

- [ ] Autenticação com JWT
- [ ] Controle de acesso por perfil
- [ ] Integração com gateway de pagamento
- [ ] Notificações em tempo real (WebSocket)
- [ ] Exportação de relatórios (PDF)
- [ ] Integração com frontend (React)
- [ ] Cache com Redis
- [ ] Docker support

---

## 📝 Configuração de Banco de Dados

### Criar banco de dados
```sql
CREATE DATABASE roots_acai 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Usuário do banco
```sql
CREATE USER 'roots_user'@'localhost' IDENTIFIED BY 'senha_segura';
GRANT ALL PRIVILEGES ON roots_acai.* TO 'roots_user'@'localhost';
FLUSH PRIVILEGES;
```

---

## 🐛 Troubleshooting

### Erro: "Connection refused"
```bash
# Verifique se o MySQL está rodando
mysql --version
# No Windows
net start MySQL80
# No Linux/Mac
brew services start mysql-community-server
```

### Erro: "Database does not exist"
```bash
# Crie o banco manualmente
mysql -u root -p < init.sql
```

### Erro: "Port already in use"
```bash
# Mude a porta em application.properties
server.port=8081
```

---

## 📞 Suporte

Encontrou um problema? Abra uma issue no repositório!

---

## 👨‍💻 Autor

**Larissa Rodrigues Guimarães**
- GitHub: [@larissaRgj](https://github.com/larissaRgj)
- LinkedIn: [Larissa Guimarães](https://www.linkedin.com/in/larissa-guimaraes-b30489334)

---

## 📝 Licença

Este projeto está sob a licença MIT.

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Add NovaFeature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

---

## 💡 Boas Práticas de Desenvolvimento

- Siga padrão RESTful na API
- Mantenha Controllers enxutos
- Lógica de negócio nos Services
- Use DTOs para transferência de dados
- Teste suas funcionalidades
- Documente suas mudanças

---

**Desenvolvido com ❤️ e ☕ por Larissa Rodrigues**