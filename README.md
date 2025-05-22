# Projeto Spotifei

**Aluno:** Lucas Masashi Yamashiro – RA: 22.124.047-6  
**Disciplina:** CCM310 / Turma: 420

---

## Introdução

O projeto **Spotifei** tem como objetivo desenvolver um sistema de gerenciamento de músicas inspirado na plataforma de streaming Spotify. É uma aplicação desktop desenvolvida em **Java** utilizando a interface gráfica **Swing**, permitindo aos usuários:

- Realizar cadastro e login
- Criar playlists personalizadas
- Adicionar e organizar músicas
- Buscar artistas e bandas
- Gerenciar faixas favoritas

---

## Objetivos Específicos

- **Cadastro de Usuário:** Permitir o registro de novos usuários na plataforma, criando perfis individuais.
- **Login de Usuário:** Implementar autenticação para garantir acesso seguro às informações dos usuários.
- **Busca:** Permitir a busca eficiente por músicas, artistas ou gêneros.
- **Curtir/Descurtir Músicas:** Possibilitar que usuários curtam e descurtam músicas, organizando suas favoritas.
- **Gerenciamento de Playlists:** Permitir criar e remover playlists de forma simples e intuitiva.
- **Histórico de Busca:** Registrar e exibir as buscas realizadas por cada usuário.

---

## Descrição do Projeto

### Arquitetura do Sistema

O Spotifei utiliza o padrão **MVC (Model-View-Controller)** para garantir separação de responsabilidades, modularidade e facilidade de manutenção. Além disso, adota o padrão **DAO** para acesso ao banco de dados.

- **Model:** Classes que representam as entidades do sistema (Usuário, Música, Playlist, etc.).
- **View:** Interfaces gráficas desenvolvidas com Swing (JFrames) para interação do usuário.
- **Controller:** Camada intermediária que processa requisições, aplica regras de negócio e coordena a comunicação entre Model, DAO e View.
- **DAO:** Classes responsáveis pelas operações CRUD no banco de dados.
- **ConnectionBD:** Classe centralizada para conexão segura com o banco de dados.
- **ManagerSession:** Gerencia as informações do usuário autenticado no sistema.

---

## Estrutura e Fluxo do Sistema

O Spotifei oferece uma navegação intuitiva, com os seguintes fluxos principais:

- **Tela de Login:** Primeira tela exibida, com opção de acesso ao cadastro.
- **Tela de Cadastro:** Permite criar uma nova conta de usuário.
- **Tela Home:** Permite pesquisar músicas, curtir, visualizar históricos e acessar playlists.
- **Tela de Histórico:** Exibe as últimas pesquisas realizadas, além das músicas curtidas e descurtidas. Permite descurtir músicas.
- **Tela de Playlists:** Exibe as playlists do usuário, permite entrar, criar ou remover playlists.
- **Tela de Músicas da Playlist:** Mostra as músicas dentro da playlist selecionada, possibilitando adicionar ou remover músicas.

---

## Principais Funcionalidades Implementadas

- Cadastro de usuário
- Login e autenticação
- Busca de músicas
- Curtir e descurtir músicas
- Criação e remoção de playlists
- Adição e remoção de músicas em playlists
- Visualização de playlists
- Visualização de músicas curtidas e descurtidas
- Histórico de buscas

---

## Como Executar o Projeto

### Pré-requisitos

- Java Development Kit (JDK) 8 ou superior
- NetBeans (ou outra IDE Java de sua preferência)
- Driver JDBC do PostgreSQL
- PostgreSQL instalado e configurado

### Obtendo o Projeto

1. Tenha o Git instalado em sua máquina.
2. Clone o repositório do projeto para um diretório local.

### Configuração do Banco de Dados

1. Utilize o arquivo SQL `projsql` localizado na pasta `sql` do projeto.
2. Importe esse arquivo em seu banco de dados PostgreSQL.

### Executando o Código Fonte

1. Abra o projeto Spotifei no NetBeans.
2. Configure o banco de dados na classe `DatabaseConnection`, no pacote `connection`.
3. Execute a aplicação (botão "Run" ou F6 no NetBeans).
4. A tela de login será exibida e o sistema estará pronto para uso.

---

## Observações Importantes

- **Documentação:** A pasta `docs` contém a documentação gerada pelo Javadoc, com detalhes das classes e métodos do sistema.
