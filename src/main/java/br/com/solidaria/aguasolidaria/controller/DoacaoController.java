package br.com.solidaria.aguasolidaria.controller;

import br.com.solidaria.aguasolidaria.model.Doacao;
import br.com.solidaria.aguasolidaria.service.DoacaoService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Data
@RestController // Define que esta classe é um Controller de API REST
@RequestMapping("/api/doacoes") // Define a URL base para todos os endpoints neste controller
public class DoacaoController {

    @Autowired
    private DoacaoService doacaoService;

    // Endpoint para LISTAR TODAS as doações (GET /api/doacoes)
    @GetMapping
    public List<Doacao> listarTodas() {
        return doacaoService.listarTodas();
    }

    // Endpoint para BUSCAR UMA doação por ID (GET /api/doacoes/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Doacao> buscarPorId(@PathVariable String id) {
        Optional<Doacao> doacao = doacaoService.buscarPorId(id);
        return doacao.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para CRIAR uma nova doação (POST /api/doacoes)
    @PostMapping
    public ResponseEntity<Doacao> criar(@RequestBody Doacao doacao) {
        Doacao novaDoacao = doacaoService.salvar(doacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaDoacao);
    }

    // Endpoint para ATUALIZAR uma doação existente (PUT /api/doacoes/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Doacao> atualizar(@PathVariable String id, @RequestBody Doacao doacaoDetalhes) {
        return doacaoService.buscarPorId(id)
                .map(doacaoExistente -> {
                    // Atualiza os campos do objeto existente com os novos dados
                    doacaoExistente.setNomeInstituicao(doacaoDetalhes.getNomeInstituicao());
                    doacaoExistente.setEndereco(doacaoDetalhes.getEndereco());
                    doacaoExistente.setLitrosDoados(doacaoDetalhes.getLitrosDoados());
                    doacaoExistente.setDataDoacao(doacaoDetalhes.getDataDoacao());
                    doacaoExistente.setResponsavelRecebimento(doacaoDetalhes.getResponsavelRecebimento());

                    Doacao doacaoAtualizada = doacaoService.salvar(doacaoExistente);
                    return ResponseEntity.ok(doacaoAtualizada);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint para DELETAR uma doação (DELETE /api/doacoes/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable String id) {
        if (doacaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        doacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}