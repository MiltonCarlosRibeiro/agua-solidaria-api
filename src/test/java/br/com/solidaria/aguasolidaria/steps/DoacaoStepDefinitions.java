package br.com.solidaria.aguasolidaria.steps;

import br.com.solidaria.aguasolidaria.model.Doacao;
import br.com.solidaria.aguasolidaria.repository.DoacaoRepository;
import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Quando;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class DoacaoStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DoacaoRepository doacaoRepository;

    private ResponseEntity<String> responseEntityString;
    private ResponseEntity<Doacao> responseEntityDoacao;
    private ResponseEntity<Doacao[]> responseEntityDoacaoArray;

    @Before
    public void setup() {
        // Limpa o banco antes de cada cenário para garantir que os testes sejam independentes
        doacaoRepository.deleteAll();
    }

    @Dado("que a API de doações está operacional")
    public void que_a_api_de_doacoes_esta_operacional() {
        // Este passo não precisa de código, pois a anotação @SpringBootTest já garante que a API está no ar.
        // É um passo mais declarativo para a leitura do cenário.
    }

    @Quando("eu envio uma requisição POST para {string} com os seguintes dados:")
    public void eu_envio_uma_requisicao_post_para_com_os_seguintes_dados(String endpoint, io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps(String.class, String.class).get(0);

        Doacao novaDoacao = new Doacao();
        novaDoacao.setNomeInstituicao(data.get("nomeInstituicao"));
        novaDoacao.setEndereco(data.get("endereco"));
        novaDoacao.setLitrosDoados(Integer.parseInt(data.get("litrosDoados")));
        novaDoacao.setDataDoacao(LocalDateTime.parse(data.get("dataDoacao")));
        novaDoacao.setResponsavelRecebimento(data.get("responsavelRecebimento"));

        HttpEntity<Doacao> request = new HttpEntity<>(novaDoacao);
        String url = "http://localhost:" + port + endpoint;

        responseEntityDoacao = restTemplate.postForEntity(url, request, Doacao.class);
    }

    @Entao("o status da resposta deve ser {int}")
    public void o_status_da_resposta_deve_ser(Integer statusCode) {
        int actualStatusCode;
        if (responseEntityString != null) {
            actualStatusCode = responseEntityString.getStatusCode().value();
        } else if (responseEntityDoacao != null) {
            actualStatusCode = responseEntityDoacao.getStatusCode().value();
        } else {
            actualStatusCode = responseEntityDoacaoArray.getStatusCode().value();
        }
        assertEquals(statusCode, actualStatusCode);
    }

    @Entao("o corpo da resposta deve conter a doação salva com o nome da instituição {string}")
    public void o_corpo_da_resposta_deve_conter_a_doacao_salva_com_o_nome_da_instituicao(String nomeInstituicao) {
        Doacao doacaoSalva = responseEntityDoacao.getBody();
        assertNotNull(doacaoSalva);
        assertNotNull(doacaoSalva.getId());
        assertEquals(nomeInstituicao, doacaoSalva.getNomeInstituicao());
    }

    @Dado("que existem {int} doações cadastradas no banco de dados")
    public void que_existem_doacoes_cadastradas_no_banco_de_dados(Integer quantidade) {
        for (int i = 1; i <= quantidade; i++) {
            Doacao doacao = new Doacao();
            doacao.setNomeInstituicao("Instituição de Teste " + i);
            doacao.setEndereco("Endereço " + i);
            doacao.setLitrosDoados(100 * i);
            doacao.setDataDoacao(LocalDateTime.now());
            doacao.setResponsavelRecebimento("Responsável " + i);
            doacaoRepository.save(doacao);
        }
    }

    @Quando("eu envio uma requisição GET para {string}")
    public void eu_envio_uma_requisicao_get_para(String endpoint) {
        String url = "http://localhost:" + port + endpoint;
        responseEntityDoacaoArray = restTemplate.getForEntity(url, Doacao[].class);
    }

    @Entao("o corpo da resposta deve ser uma lista com {int} doações")
    public void o_corpo_da_resposta_deve_ser_uma_lista_com_doacoes(Integer quantidade) {
        Doacao[] doacoes = responseEntityDoacaoArray.getBody();
        assertNotNull(doacoes);
        assertEquals(quantidade, doacoes.length);
    }

    @Quando("eu envio uma requisição GET para {string} que não existe")
    public void eu_envio_uma_requisicao_get_para_que_nao_existe(String endpoint) {
        String url = "http://localhost:" + port + endpoint;
        responseEntityString = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
    }
}