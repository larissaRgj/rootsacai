# Sistema de Gerenciamento Roots Açaí

Sistema completo de gerenciamento para a rede de lojas Roots Açaí, desenvolvido com HTML, Java, JavaScript e Docker.

## 📋 Sobre

Plataforma de gerenciamento que integra frontend e backend para controlar operações da Roots Açaí, incluindo gestão de produtos, pedidos e operações gerais.

## 🛠️ Tech Stack

- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Java
- **DevOps**: Docker
- **Banco de Dados**: (Configure conforme necessário)

## 🚀 Como Começar

### Pré-requisitos
- Java 11+
- Docker & Docker Compose
- Node.js (opcional, para frontend)
- Git

### Instalação

1. Clone o repositório:
```bash
git clone https://github.com/larissaRgj/rootsacai.git
cd rootsacai
```

2. Configure as variáveis de ambiente:
```bash
cp .env.example .env
# Edite o arquivo .env com suas configurações
```

3. Inicie com Docker:
```bash
docker-compose up -d
```

Ou para desenvolvimento local:
```bash
# Backend
mvn spring-boot:run

# Frontend (em outro terminal)
cd frontend
npm install
npm start
```

## 📁 Estrutura do Projeto

```
rootsacai/
├── backend/          # Código Java/Spring Boot
├── frontend/         # Código HTML/CSS/JavaScript
├── docker/          # Configurações Docker
├── docker-compose.yml
└── README.md
```

## 📖 Documentação

- [Guia de Instalação](docs/instalacao.md)
- [Guia de Contribuição](CONTRIBUTING.md)
- [API Documentation](docs/api.md)

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 📧 Suporte

Para dúvidas ou problemas, abra uma [issue](https://github.com/larissaRgj/rootsacai/issues).

## 👤 Autor

[larissaRgj](https://github.com/larissaRgj)
