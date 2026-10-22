package br.ufal.ic.p2.wepayu;
import br.ufal.ic.p2.wepayu.codificadores.PersistenciaXML;
import br.ufal.ic.p2.wepayu.exception.SistemaEncerradoException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Sistema;
import br.ufal.ic.p2.wepayu.servicos.GerenciadorEmpregados;
import br.ufal.ic.p2.wepayu.servicos.GerenciadorPagamentos;
import br.ufal.ic.p2.wepayu.servicos.HistoricoOperacoes;
import br.ufal.ic.p2.wepayu.servicos.ServicoFolha;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Facade {
    private Map<String, Empregado> empregados;
    private GerenciadorEmpregados gerenciadorEmp;
    private GerenciadorPagamentos gerenciadorPag;
    private ServicoFolha servicoFolha;
    private HistoricoOperacoes historico;
    private boolean encerrado = false;

    public Facade() {
        Sistema sistema = PersistenciaXML.carregar();
        this.empregados = sistema.getEmpregados();
        this.gerenciadorEmp = new GerenciadorEmpregados(empregados, sistema.getAgendas());
        this.gerenciadorPag = new GerenciadorPagamentos(empregados, gerenciadorEmp);
        this.servicoFolha = new ServicoFolha(empregados);
        this.historico = new HistoricoOperacoes();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            String id = gerenciadorEmp.criarEmpregado(nome, endereco, tipo, salarioStr);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
            return id;
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            String id = gerenciadorEmp.criarEmpregado(nome, endereco, tipo, salarioStr, comissaoStr);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
            return id;
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void criarAgendaDePagamentos(String descricao) throws Exception {
        verificarEncerrado();
        gerenciadorEmp.criarAgendaDePagamentos(descricao);
    }

    public void removerEmpregado(String empId) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorEmp.removerEmpregado(empId);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public String getAtributoEmpregado(String empId, String atributo) throws Exception {
        verificarEncerrado();
        return gerenciadorEmp.getAtributoEmpregado(empId, atributo);
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        verificarEncerrado();
        return gerenciadorEmp.getEmpregadoPorNome(nome, indice);
    }

    public int getNumeroDeEmpregados() throws Exception {
        verificarEncerrado();
        return gerenciadorEmp.getNumeroDeEmpregados();
    }

    public void lancaCartao(String empId, String data, String horas) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorPag.lancaCartao(empId, data, horas);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void lancaVenda(String empId, String data, String valor) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorPag.lancaVenda(empId, data, valor);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void lancaTaxaServico(String membro, String data, String valor) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorPag.lancaTaxaServico(membro, data, valor);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public String getHorasNormaisTrabalhadas(String empId, String dataInicial, String dataFinal) throws Exception {
        verificarEncerrado();
        return gerenciadorPag.getHorasNormaisTrabalhadas(empId, dataInicial, dataFinal);
    }

    public String getHorasExtrasTrabalhadas(String empId, String dataInicial, String dataFinal) throws Exception {
        verificarEncerrado();
        return gerenciadorPag.getHorasExtrasTrabalhadas(empId, dataInicial, dataFinal);
    }

    public String getVendasRealizadas(String empId, String dataInicial, String dataFinal) throws Exception {
        verificarEncerrado();
        return gerenciadorPag.getVendasRealizadas(empId, dataInicial, dataFinal);
    }

    public String getTaxasServico(String empId, String dataInicial, String dataFinal) throws Exception {
        verificarEncerrado();
        return gerenciadorPag.getTaxasServico(empId, dataInicial, dataFinal);
    }

    public void alteraEmpregado(String empId, String atributo, String valor) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorEmp.alteraEmpregado(empId, atributo, valor);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void alteraEmpregado(String empId, String atributo, String valor, String extra) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorEmp.alteraEmpregado(empId, atributo, valor, extra);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void alteraEmpregado(String empId, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorEmp.alteraEmpregado(empId, atributo, valor, idSindicato, taxaSindical);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void alteraEmpregado(String empId, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            gerenciadorEmp.alteraEmpregado(empId, atributo, valor1, banco, agencia, contaCorrente);
            PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }
    public String totalFolha(String dataStr) throws Exception {
        verificarEncerrado();
        return servicoFolha.totalFolha(dataStr);
    }

    public void rodaFolha(String dataStr, String saida) throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);

        try {
            servicoFolha.rodaFolha(dataStr, saida);
        } catch (Exception e) {
            historico.descartarUltimo();
            throw e;
        }
    }

    public void undo() throws Exception {
        verificarEncerrado();
        empregados = historico.undo(empregados);
        reconfigurarGerenciadores();
        PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
    }

    public void redo() throws Exception {
        verificarEncerrado();
        empregados = historico.redo(empregados);
        reconfigurarGerenciadores();
        PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
    }

    public void zerarSistema() throws Exception {
        verificarEncerrado();
        historico.salvarEstado(empregados);
        this.empregados.clear();
        gerenciadorEmp.resetarAgendas();
        PersistenciaXML.apagar();
    }

    public void encerrarSistema() {
        encerrado = true;
        PersistenciaXML.salvar(new Sistema(empregados, gerenciadorEmp.getAgendasDisponiveis()));
    }

    private void verificarEncerrado() throws Exception {
        if (encerrado) {
            throw new SistemaEncerradoException();
        }
    }

    private void reconfigurarGerenciadores() {
        Set<String> agendas = this.gerenciadorEmp.getAgendasDisponiveis();

        this.gerenciadorEmp = new GerenciadorEmpregados(empregados, agendas);
        this.gerenciadorPag = new GerenciadorPagamentos(empregados, gerenciadorEmp);
        this.servicoFolha = new ServicoFolha(empregados);
    }
}