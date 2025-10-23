# language: pt
Funcionalidade: Gerenciamento de Doações de Água
  Como um usuário da API, eu quero poder criar, listar e buscar doações
  para garantir que o sistema de registros funcione corretamente.

  Cenário: Cadastrar uma nova doação com sucesso
    Dado que a API de doações está operacional
    Quando eu envio uma requisição POST para "/api/doacoes" com os seguintes dados:
      | nomeInstituicao        | endereco              | litrosDoados | dataDoacao                  | responsavelRecebimento |
      | Lar dos Idosos Felizes | Rua da Paz, 123       | 150          | 2025-11-10T14:00:00         | Joana Silva            |
    Então o status da resposta deve ser 201
    E o corpo da resposta deve conter a doação salva com o nome da instituição "Lar dos Idosos Felizes"

  Cenário: Listar as doações existentes
    Dado que existem 2 doações cadastradas no banco de dados
    Quando eu envio uma requisição GET para "/api/doacoes"
    Então o status da resposta deve ser 200
    E o corpo da resposta deve ser uma lista com 2 doações

  Cenário: Tentar buscar uma doação com ID inexistente
    Dado que a API de doações está operacional
    Quando eu envio uma requisição GET para "/api/doacoes/id_que_nao_existe"
    Então o status da resposta deve ser 404