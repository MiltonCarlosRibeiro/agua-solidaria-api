package br.com.solidaria.aguasolidaria.repository;

import br.com.solidaria.aguasolidaria.model.Doacao;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoacaoRepository extends MongoRepository<Doacao, String> {
    // A mágica do Spring Data acontece aqui.
    // Esta interface já nos dá todos os métodos de CRUD (salvar, buscar, deletar, etc.)
}