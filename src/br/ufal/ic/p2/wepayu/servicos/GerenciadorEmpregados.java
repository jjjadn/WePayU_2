package br.ufal.ic.p2.wepayu.servicos;

import java.util.Map;
import java.util.UUID;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.validacao.ValidadorEmpregado;
import br.ufal.ic.p2.wepayu.exception.*;
import br.ufal.ic.p2.wepayu.servicos.AgendaPagamento;

public class GerenciadorEmpregados {

    private Map<String, Empregado> empregados;

    private Set<String> agendasDisponiveis = new HashSet<>(Arrays.asList(
            "semanal 5",
            "mensal $",
            "semanal 2 5"
    ));

    public GerenciadorEmpregados(Map<String, Empregado> empregados) {
        this.empregados = empregados;
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception {

        if ("comissionado".equals(tipo)) {
            throw new TipoNaoAplicavelException();
        }
        return salvarEmpregado(nome, endereco, tipo, salarioStr, null);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {

        if (tipo != null && !"comissionado".equals(tipo)) {
            throw new TipoNaoAplicavelException();
        }
        return salvarEmpregado(nome, endereco, tipo, salarioStr, comissaoStr);
    }

    private String salvarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {

        ValidadorEmpregado.validarAtributos(nome, endereco, tipo);

        double salario = ValidadorEmpregado.validarSalario(salarioStr);

        Double comissao = null;

        if ("comissionado".equals(tipo)) {
            comissao = ValidadorEmpregado.validarComissao(comissaoStr);
        }

        String id = UUID.randomUUID().toString();
        Empregado emp = new Empregado(id, nome, endereco, tipo, salario, comissao);
        empregados.put(id, emp);
        return id;
    }

    public void removerEmpregado(String empId) throws Exception {
        buscarEmpregado(empId);
        empregados.remove(empId);
    }


    public Empregado buscarEmpregado(String empId) throws Exception {

        if (empId == null || empId.trim().isEmpty()) {
            throw new IdentificacaoNaoNulaException();
        }

        Empregado emp = empregados.get(empId);

        if (emp == null) {
            throw new EmpregadoNaoExisteException();
        }

        return emp;
    }

    public Empregado buscarEmpregadoPorMembro(String membro) throws Exception {

        if (membro == null || membro.trim().isEmpty()) {
            throw new IdentificacaoMembroNulaException();
        }

        for (Empregado emp : empregados.values()) {
            if (membro.equals(emp.getIdSindicato())) {
                return emp;
            }
        }
        throw new MembroNaoExisteException();
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {

        if (nome == null || nome.trim().isEmpty()) {
            throw new NomeNaoNuloException();
        }

        java.util.List<Empregado> encontrados = new java.util.ArrayList<>();

        for (Empregado emp : empregados.values()) {
            if (emp.getNome() != null && emp.getNome().contains(nome)) {
                encontrados.add(emp);
            }
        }

        if (encontrados.isEmpty() || indice < 1 || indice > encontrados.size()) {
            throw new NaoHaEmpregadoComNomeException();
        }
        return encontrados.get(indice - 1).getId();
    }

    public int getNumeroDeEmpregados() {
        return empregados.size();
    }


    public String getAtributoEmpregado(String empId, String atributo) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        switch (atributo) {
            case "nome":

                return emp.getNome();
            case "endereco":

                return emp.getEndereco();
            case "tipo":

                return emp.getTipo();
            case "salario":

                return String.format(java.util.Locale.GERMANY, "%.2f", emp.getSalario());
            case "comissao":

                if (emp.getComissao() == null) {
                    throw new EmpregadoNaoComissionadoException();
                }

                return String.format(java.util.Locale.GERMANY, "%.2f", emp.getComissao());
            case "sindicalizado":

                return String.valueOf(emp.isSindicalizado());
            case "metodoPagamento":

                return emp.getMetodoPagamento();
            case "agendaPagamento":

                return emp.getAgendaPagamento();
            case "banco":

                if (!"banco".equals(emp.getMetodoPagamento())) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return emp.getBanco();
            case "agencia":

                if (!"banco".equals(emp.getMetodoPagamento())) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return emp.getAgencia();
            case "contaCorrente":

                if (!"banco".equals(emp.getMetodoPagamento())) {
                    throw new EmpregadoNaoRecebeEmBancoException();
                }
                return emp.getContaCorrente();
            case "idSindicato":

                if (!emp.isSindicalizado()) {
                    throw new EmpregadoNaoSindicalizadoException();
                }
                return emp.getIdSindicato();
            case "taxaSindical":

                if (!emp.isSindicalizado()) {
                    throw new EmpregadoNaoSindicalizadoException();
                }
                return String.format(java.util.Locale.GERMANY, "%.2f", emp.getTaxaSindical());
            default:
                throw new AtributoNaoExisteException();
        }
    }


    public void alteraEmpregado(String empId, String atributo, String valor) throws Exception {

        Empregado emp = buscarEmpregado(empId);

        if ("agendaPagamento".equals(atributo)) {
            if (!agendasDisponiveis.contains(valor)) {
                throw new AgendaNaoDisponivelException();
            }
            emp.setAgendaPagamento(valor);
            return;
        }

        switch (atributo) {
            case "nome":

                if (valor == null || valor.trim().isEmpty())
                    throw new NomeNaoNuloException();
                break;

            case "endereco":
                if (valor == null || valor.trim().isEmpty())
                    throw new EnderecoNaoNuloException();
                break;

            case "tipo":
                if (!"horista".equals(valor) && !"assalariado".equals(valor) && !"comissionado".equals(valor))
                    throw new TipoEmpregadoNaoValidoException();
                break;

            case "salario":
                ValidadorEmpregado.validarSalario(valor);
                break;

            case "comissao":
                if (!"comissionado".equals(emp.getTipo()))
                    throw new EmpregadoNaoComissionadoException();
                ValidadorEmpregado.validarComissao(valor);
                break;

            case "metodoPagamento":
                if (!"emMaos".equals(valor) && !"correios".equals(valor))
                    throw new MetodoPagamentoInvalidoException();
                break;

            case "sindicalizado":
                if (!"true".equals(valor) && !"false".equals(valor))
                    throw new SindicatoBooleanException();

                if ("true".equals(valor))
                    throw new IdentificacaoSindicatoNaoNulaException();
                break;

            default:
                throw new AtributoNaoExisteException();
        }

        switch (atributo) {
            case "nome":     emp.setNome(valor); break;
            case "endereco": emp.setEndereco(valor); break;
            case "tipo":     emp.setTipo(valor); break;
            case "salario":  emp.setSalario(ValidadorEmpregado.validarSalario(valor)); break;
            case "comissao": emp.setComissao(ValidadorEmpregado.validarComissao(valor)); break;

            case "metodoPagamento":
                emp.setMetodoPagamento(valor);
                emp.setBanco(null);
                emp.setAgencia(null);
                emp.setContaCorrente(null);
                break;

            case "sindicalizado":
                emp.setSindicalizado(false);
                emp.setIdSindicato(null);
                emp.setTaxaSindical(null);
                break;
        }
    }


    public void alteraEmpregado(String empId, String atributo, String valor, String extra) throws Exception {

        Empregado emp = buscarEmpregado(empId);


        if ("agendaPagamento".equals(atributo)) {

            if (!agendasDisponiveis.contains(valor)) {
                throw new AgendaNaoDisponivelException();
            }
            emp.setAgendaPagamento(valor);
            return;
        }

        if (!"tipo".equals(atributo))
            throw new AtributoNaoExisteException();

        if (!"horista".equals(valor) && !"assalariado".equals(valor) && !"comissionado".equals(valor))
            throw new TipoEmpregadoNaoValidoException();

        if ("comissionado".equals(valor)) {
            ValidadorEmpregado.validarComissao(extra);
        } else {
            ValidadorEmpregado.validarSalario(extra);
        }

        emp.setTipo(valor);

        if ("comissionado".equals(valor)) {
            emp.setComissao(ValidadorEmpregado.validarComissao(extra));
        } else {
            emp.setSalario(ValidadorEmpregado.validarSalario(extra));
            emp.setComissao(null);
        }
    }


    public void alteraEmpregado(String empId, String atributo, String valor, String idSindicato, String taxaSindical) throws Exception {

        Empregado emp = buscarEmpregado(empId);

        if (!"sindicalizado".equals(atributo))
            throw new AtributoNaoExisteException();
        if (!"true".equals(valor) && !"false".equals(valor))
            throw new SindicatoBooleanException();

        double taxa = 0;

        if ("true".equals(valor)) {
            if (idSindicato == null || idSindicato.trim().isEmpty())
                throw new IdentificacaoSindicatoNaoNulaException();
            if (taxaSindical == null || taxaSindical.trim().isEmpty())
                throw new TaxaSindicalNaoNulaException();

            try {
                taxa = Double.parseDouble(taxaSindical.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new TaxaSindicalNaoNumericaException();
            }
            if (taxa < 0)
                throw new TaxaSindicalNaoNegativaException();

            for (Empregado e : empregados.values()) {
                if (!e.getId().equals(empId) && idSindicato.equals(e.getIdSindicato()))
                    throw new EmpregadoMesmaIdentificacaoSindicatoException();
            }
        }

        if ("false".equals(valor)) {
            emp.setSindicalizado(false);
            emp.setIdSindicato(null);
            emp.setTaxaSindical(null);

        } else {
            emp.setSindicalizado(true);
            emp.setIdSindicato(idSindicato);
            emp.setTaxaSindical(taxa);
        }
    }

    public void criarAgendaDePagamentos(String descricao) throws Exception {
        AgendaPagamento.validarDescricao(descricao);

        if (agendasDisponiveis.contains(descricao)) {
            throw new AgendaJaExisteException();
        }
        agendasDisponiveis.add(descricao);
    }

    public void resetarAgendas() {
        agendasDisponiveis.clear();
        agendasDisponiveis.add("semanal 5");
        agendasDisponiveis.add("mensal $");
        agendasDisponiveis.add("semanal 2 5");
    }
    public void alteraEmpregado(String empId, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {

        Empregado emp = buscarEmpregado(empId);

        if (!"metodoPagamento".equals(atributo))
            throw new AtributoNaoExisteException();
        if (!"banco".equals(valor1))
            throw new MetodoPagamentoInvalidoException();
        if (banco == null || banco.trim().isEmpty())
            throw new BancoNaoNuloException();
        if (agencia == null || agencia.trim().isEmpty())
            throw new AgenciaNaoNulaException();
        if (contaCorrente == null || contaCorrente.trim().isEmpty())
            throw new ContaCorrenteNaoNulaException();

        emp.setMetodoPagamento("banco");
        emp.setBanco(banco);
        emp.setAgencia(agencia);
        emp.setContaCorrente(contaCorrente);
    }
}