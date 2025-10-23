// URL base da nossa API
const API_URL = 'http://localhost:8080/api/doacoes';

// Elementos do DOM
const form = document.getElementById('doacao-form');
const doacoesLista = document.getElementById('doacoes-lista');
const doacaoIdInput = document.getElementById('doacaoId');
const btnCancelar = document.getElementById('btn-cancelar');

// Função para formatar a data para a tabela
const formatarData = (dataString) => {
    if (!dataString) return 'N/A';
    const data = new Date(dataString);
    return data.toLocaleString('pt-BR');
};

// Função para buscar e exibir todas as doações
const listarDoacoes = async () => {
    try {
        const response = await fetch(API_URL);
        const doacoes = await response.json();
        doacoesLista.innerHTML = ''; // Limpa a lista antes de adicionar os novos itens

        doacoes.forEach(doacao => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${doacao.nomeInstituicao}</td>
                <td>${doacao.litrosDoados} L</td>
                <td>${formatarData(doacao.dataDoacao)}</td>
                <td>
                    <button class="action-button edit-button" onclick="editarDoacao('${doacao.id}')">Editar</button>
                    <button class="action-button delete-button" onclick="deletarDoacao('${doacao.id}')">Deletar</button>
                </td>
            `;
            doacoesLista.appendChild(tr);
        });
    } catch (error) {
        console.error('Erro ao listar doações:', error);
    }
};

// Função para preencher o formulário para edição
const editarDoacao = async (id) => {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const doacao = await response.json();

        // Preenche os campos do formulário
        doacaoIdInput.value = doacao.id;
        document.getElementById('nomeInstituicao').value = doacao.nomeInstituicao;
        document.getElementById('endereco').value = doacao.endereco;
        document.getElementById('litrosDoados').value = doacao.litrosDoados;
        // Ajusta o formato da data para o input datetime-local
        document.getElementById('dataDoacao').value = doacao.dataDoacao.slice(0, 16);
        document.getElementById('responsavelRecebimento').value = doacao.responsavelRecebimento;

        btnCancelar.classList.remove('hidden');
        window.scrollTo(0, 0); // Rola a página para o topo
    } catch (error) {
        console.error('Erro ao buscar doação para edição:', error);
    }
};

// Função para deletar uma doação
const deletarDoacao = async (id) => {
    if (confirm('Tem certeza que deseja deletar esta doação?')) {
        try {
            await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
            listarDoacoes(); // Atualiza a lista
        } catch (error) {
            console.error('Erro ao deletar doação:', error);
        }
    }
};

// Event listener para o formulário (Criação e Atualização)
form.addEventListener('submit', async (event) => {
    event.preventDefault(); // Impede o recarregamento da página

    const id = doacaoIdInput.value;
    const doacao = {
        nomeInstituicao: document.getElementById('nomeInstituicao').value,
        endereco: document.getElementById('endereco').value,
        litrosDoados: parseInt(document.getElementById('litrosDoados').value),
        dataDoacao: document.getElementById('dataDoacao').value,
        responsavelRecebimento: document.getElementById('responsavelRecebimento').value,
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL;

    try {
        await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(doacao)
        });

        form.reset();
        doacaoIdInput.value = '';
        btnCancelar.classList.add('hidden');
        listarDoacoes(); // Atualiza a lista
    } catch (error) {
        console.error('Erro ao salvar doação:', error);
    }
});

// Event listener para o botão de cancelar edição
btnCancelar.addEventListener('click', () => {
    form.reset();
    doacaoIdInput.value = '';
    btnCancelar.classList.add('hidden');
});

// Carrega a lista de doações quando a página é carregada
document.addEventListener('DOMContentLoaded', listarDoacoes);