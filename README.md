# Web Service do projeto HomeTasks

Documentação disponível em [HomeTasks - Docs](https://github.com/best-software-company/docs)

> Em Construção

## Como montar localmente um ambiente de DEV para o HomeTasks Server

### Pré-requisitos

* IntelliJ IDEA (com Gradle)
* Schema HomeTasksDB criado no MySQL

### Importando projeto no IntelliJ


1. Faça o download do repositório Server para o diretório de sua preferência.

   ```bash
   $ git clone https://github.com/best-software-company/server.git
   ```

2. No tela inicial do IntelliJ, selecione a opção `Import Project`  e após informe o diretório do repositório baixado no item 2. 

3. Na tela seguinte, selecione a opção `Import project from external_model` , indique o Plugin `Gradle` na lista e clique no botão `Finish`.

4. Na tela de projeto, a mensagem `IntelliJ IDEA found a Gradle build script` será exibida. Clique em `Import Gradle Project` e aguarde enquanto o projeto é importado.

	* **Obs.:** Verifique no rodapé a mensagem `processes running...` ou `Indexing...` , que não será mais exibida quando a importação terminar.  

### Configurando Banco de Dados

> Carregar as variáveis do arquivo .env no ambiente. (alterando se for necessário)

### TODO

1. Adicionar métodos PUT. Nesse caso deve-se lembrar que quando a intenção não é atualizar todo o objeto então o PUT deve ser em uma URI única específica. Ex: "PUT /apiHomeTasks/User/Roque/name novoNome" para alterar o nome.
2. Adicionar serviço de geração de token para novos usuários.
