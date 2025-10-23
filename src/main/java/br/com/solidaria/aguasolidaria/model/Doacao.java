package br.com.solidaria.aguasolidaria.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data // Anotação do Lombok para gerar Getters, Setters, etc.
@Document("doacoes") // Mapeia esta classe para a coleção "doacoes" no MongoDB
public class Doacao {

    @Id // Marca este campo como a chave primária (_id) no banco
    private String id;

    private String nomeInstituicao;
    private String endereco;
    private Integer litrosDoados;
    private LocalDateTime dataDoacao;
    private String responsavelRecebimento;
}