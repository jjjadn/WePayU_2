package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.validacao.ValidadorEmpregado;

import java.util.*;

public class Facade {
    private Map<String, Empregado> empregados = new LinkedHashMap<>();

    public void zerarSistema() {
        empregados.clear();
    }

    public void encerrarSistema() {
        // Reservado para salvar os dados em arquivo
    }

    // Criar Empregado SEM Comissão (Horista / Assalariado)
    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception {
        if ("comissionado".equals(tipo)) {
            throw new Exception("Tipo nao aplicavel.");
        }
        return salvarEmpregado(nome, endereco, tipo, salarioStr, null);
    }

    // Criar Empregado COM Comissão (Comissionado)
    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {
        if (tipo != null && !"comissionado".equals(tipo)) {
            throw new Exception("Tipo nao aplicavel.");
        }
        return salvarEmpregado(nome, endereco, tipo, salarioStr, comissaoStr);
    }

    private String salvarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {
        // Valida atributos textuais e converte números
        ValidadorEmpregado.validarAtributos(nome, endereco, tipo);
        double salario = ValidadorEmpregado.validarSalario(salarioStr);
        Double comissao = null;

        if ("comissionado".equals(tipo)) {
            comissao = ValidadorEmpregado.validarComissao(comissaoStr);
        }

        // Gera o ID, instancia o Empregado e salva no repositório
        String id = UUID.randomUUID().toString();
        Empregado emp = new Empregado(id, nome, endereco, tipo, salario, comissao);
        empregados.put(id, emp);
        return id;
    }

    public String getAtributoEmpregado(String empId, String atributo) throws Exception {
        if (empId == null || empId.trim().isEmpty()) {
            throw new Exception("Identificacao do empregado nao pode ser nula.");
        }

        Empregado emp = empregados.get(empId);
        if (emp == null) {
            throw new Exception("Empregado nao existe.");
        }

        switch (atributo) {
            case "nome":
                return emp.getNome();
            case "endereco":
                return emp.getEndereco();
            case "tipo":
                return emp.getTipo();
            case "salario":
                return String.format(Locale.GERMANY, "%.2f", emp.getSalario());
            case "comissao":
                if (emp.getComissao() == null) {
                    throw new Exception("Atributo nao existe.");
                }
                return String.format(Locale.GERMANY, "%.2f", emp.getComissao());
            case "sindicalizado":
                return String.valueOf(emp.isSindicalizado());
            default:
                throw new Exception("Atributo nao existe.");
        }
    }

    public String getEmpregadoPorNome(String nome, int indice) throws Exception {
        List<Empregado> encontrados = new ArrayList<>();
        for (Empregado emp : empregados.values()) {
            if (emp.getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontrados.add(emp);
            }
        }

        if (encontrados.isEmpty() || indice < 1 || indice > encontrados.size()) {
            throw new Exception("Nao ha empregado com esse nome.");
        }

        return encontrados.get(indice - 1).getId();
    }
}