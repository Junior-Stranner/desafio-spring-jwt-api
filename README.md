# desafio-spring-jwt-api
# Desafio Spring Boot + Spring Security com JWT

## Descrição
Este projeto é uma aplicação web para gerenciamento de clientes e seus produtos, desenvolvida com **Spring Boot** e protegida com **Spring Security** utilizando **JWT (JSON Web Token)** para autenticação e autorização.

O sistema permite que cada cliente crie uma conta, faça login e acesse funcionalidades apenas para os seus próprios produtos.

---

## Funcionalidades

### Cliente
- Cadastro de cliente com nome, e-mail e senha.
- Login com e-mail e senha, recebendo um **token JWT**.
- Acesso apenas às suas próprias funcionalidades mediante autenticação.

### Produto
- CRUD completo de produtos (Create, Read, Update, Delete) associado a cada cliente.
- Apenas clientes autenticados podem acessar seus produtos.
- Cada produto está vinculado ao cliente que o criou.

---

## Tecnologias Utilizadas
- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- JPA/Hibernate
- H2 Database (memória para testes)
- Maven

---

## Estrutura do Projeto

- `controller` → Contém os endpoints REST.
- `service` → Lógica de negócios e manipulação de dados.
- `repository` → Interfaces para acesso a dados com Spring Data JPA.
- `model` → Entidades do sistema (Cliente, Produto).
- `security` → Configuração de autenticação e filtros JWT.
- `dto` → Objetos de transferência de dados (Requests e Responses).

---

## Autenticação e Autorização

O projeto utiliza duas formas principais de obter o usuário autenticado:

### 1. `@AuthenticationPrincipal`
- Injetado diretamente nos métodos do controller.
- Permite acessar o usuário autenticado (`Cliente`) sem precisar recuperar manualmente.
- Garante que o usuário só manipule os próprios dados.

### 2. `SecurityContextHolder + UserDetails`
- Forma manual de obter o usuário autenticado.
- `SecurityContextHolder` mantém o contexto de segurança da requisição.
- `UserDetails` contém informações do usuário (nome, e-mail, roles).
- Útil em casos onde `@AuthenticationPrincipal` não pode ser usado ou para lógica mais complexa.

**Exemplo de uso:**
```java
var authentication = SecurityContextHolder.getContext().getAuthentication();
Cliente cliente = (Cliente) authentication.getPrincipal();

Endpoints
Cliente
Método	Endpoint	                     Descrição	                    Autenticação
POST	/api/v1/clientes/register	   Cadastro de cliente	                  Não
POST	/api/v1/clientes/login	  Login e retorno do token JWT	            Não

Produto
Método	Endpoint	                     Descrição                      Autenticação
POST	/api/v1/produtos	             Criar produto	                     Sim
GET	/api/v1/produtos	          Listar produtos do cliente	             Sim
PUT	/api/v1/produtos/{id}	        Atualizar produto pelo id	             Sim
DELETE	/api/v1/produtos/{id}	      Deletar produto pelo id	             Sim


Todos os endpoints de produtos exigem Bearer Token JWT no header Authorization:

Authorization: Bearer <token>

Como Rodar o Projeto

Clone o repositório:

git clone <repo-url>


Entre na pasta do projeto:

cd desafioju


Configure as variáveis no application.properties:

jwt.secret.key=sua_chave_secreta
jwt.expiration=3600
spring.h2.console.enabled=true
spring.datasource.url=jdbc:h2:mem:db


Rode a aplicação:

mvn spring-boot:run


Teste os endpoints via Postman ou Insomnia:

Cadastro de cliente → gerar token JWT

Login → obter token

Testar endpoints de produto com e sem token (401 quando não autenticado)

Observações de Segurança

Nunca expor IDs de clientes nos endpoints públicos.

Sempre verificar o usuário autenticado via @AuthenticationPrincipal ou SecurityContextHolder.

JWT protege o backend contra acessos indevidos.

Conclusão

Este projeto foi desenvolvido como desafio para praticar:

Spring Boot

Spring Security com JWT

Estruturação de aplicação em camadas

Controle de acesso seguro por cliente

Aprendi conceitos cruciais de autenticação e autorização, além de boas práticas para proteger endpoints e manipular dados sensíveis.

Autor

Nome: Heinz Stranner Junior

LinkedIn: Heinz Stranner Junior
