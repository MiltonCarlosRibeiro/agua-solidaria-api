package br.com.solidaria.aguasolidaria.service;

import br.com.solidaria.aguasolidaria.model.Doacao;
import br.com.solidaria.aguasolidaria.repository.DoacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Anotação que define esta classe como um serviço de negócio no Spring
public class DoacaoService {

    @Autowired // Injeção de dependência: O Spring vai nos fornecer uma instância do DoacaoRepository
    private DoacaoRepository doacaoRepository;

    // Método para LISTAR todas as doações
    public List<Doacao> listarTodas() {
        return doacaoRepository.findAll();
    }

    // Método para BUSCAR uma doação pelo seu ID
    public Optional<Doacao> buscarPorId(String id) {
        return doacaoRepository.findById(id);
    }

    // Método para SALVAR uma nova doação (ou atualizar uma existente)
    public Doacao salvar(Doacao doacao) {
        return doacaoRepository.save(doacao);
    }

    // Método para DELETAR uma doação pelo seu ID
    public void deletar(String id) {
        doacaoRepository.deleteById(id);
    }
}