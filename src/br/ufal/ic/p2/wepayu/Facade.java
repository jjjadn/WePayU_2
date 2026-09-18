package br.ufal.ic.p2.wepayu;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.ufal.ic.p2.wepayu.models.Cartao;
import br.ufal.ic.p2.wepayu.persistencia.PersistenciaXML;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.validacao.ValidadorCartao;
import br.ufal.ic.p2.wepayu.validacao.ValidadorEmpregado;

public class Facade {
    private Map<String, Empregado> empregados;

    public Facade() {
        this.empregados = PersistenciaXML.carregar();
        System.err.println("Carregados: " + empregados.size() + " empregados de " +
                new File("empregados.xml").getAbsolutePath());
        if (this.empregados == null) this.empregados = new LinkedHashMap<>();
    }

    public void encerrarSistema() {
        PersistenciaXML.salvar(this.empregados);
    }

    public void zerarSistema() {
        this.empregados.clear();
        PersistenciaXML.apagar();
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr) throws Exception {
        if ("comissionado".equals(tipo)) {
            throw new Exception("Tipo nao aplicavel.");
        }
        return salvarEmpregado(nome, endereco, tipo, salarioStr, null);
    }

    public String criarEmpregado(String nome, String endereco, String tipo, String salarioStr, String comissaoStr) throws Exception {
        if (tipo != null && !"comissionado".equals(tipo)) {
            throw new Exception("Tipo nao aplicavel.");
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
        PersistenciaXML.salvar(empregados);
        return id;
    }
    public void removerEmpregado(String empId)throws Exception{
        Empregado emp = buscarEmpregado(empId);
        empregados.remove(empId);

        PersistenciaXML.salvar(empregados);

    }

    private Empregado buscarEmpregado(String empId) throws Exception {
        if (empId == null || empId.trim().isEmpty()) {
            throw new Exception("Identificacao do empregado nao pode ser nula.");
        }
        Empregado emp = empregados.get(empId);
        if (emp == null) {
            throw new Exception("Empregado nao existe.");
        }
        return emp;
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
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("Nome nao pode ser nulo.");
        }
        List<Empregado> encontrados = new ArrayList<>();
        for (Empregado emp : empregados.values()) {
            if (emp.getNome() != null && emp.getNome().contains(nome)) {
                encontrados.add(emp);
            }
        }
        if (encontrados.isEmpty() || indice < 1 || indice > encontrados.size()) {
            throw new Exception("Nao ha empregado com esse nome.");
        }
        return encontrados.get(indice - 1).getId();
    }

    public void lancaCartao(String empId, String data, String horas) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        if(!"horista".equals(emp.getTipo())){
            throw new Exception("Empregado nao eh horista.");
        }
        LocalDate d = ValidadorCartao.validarData(data, "");
        double h = ValidadorCartao.validarHoras(horas);

        Cartao c =new Cartao(d,h);
        emp.adicionarCartao(c);
        PersistenciaXML.salvar(empregados);


    }

    public String getHorasNormaisTrabalhadas(String empId, String dataInicial, String dataFinal) throws Exception{
        Empregado emp = buscarEmpregado(empId);

        if (!"horista".equals(emp.getTipo())) {
            throw new Exception("Empregado nao eh horista.");
        }


        LocalDate inicial = ValidadorCartao.validarData(dataInicial, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinal, "final");


        if (inicial.isAfter(fim)) {
            throw new Exception("Data inicial nao pode ser posterior aa data final.");
        }
        double total = 0;

        for (Cartao c : emp.getCartoes()) {
            LocalDate d = c.getData();
            if (!d.isBefore(inicial) && d.isBefore(fim)) {
                total += Math.min(c.getHoras(), 8);
            }
        }
        return formatarHoras(total);

    }

    public String getHorasExtrasTrabalhadas(String empId, String dataInicial, String dataFinal) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        if (!"horista".equals(emp.getTipo())) {
            throw new Exception("Empregado nao eh horista.");
        }


        LocalDate inicial = ValidadorCartao.validarData(dataInicial, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinal, "final");


        if (inicial.isAfter(fim)) {
            throw new Exception("Data inicial nao pode ser posterior aa data final.");
        }

        double total = 0;
        for (Cartao c : emp.getCartoes()) {
            LocalDate d = c.getData();
            if (!d.isBefore(inicial) && d.isBefore(fim)) {
                total += Math.max(0, c.getHoras() - 8);
            }
        }
        return formatarHoras(total);
    }

    private String formatarHoras(double h) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        DecimalFormat df = new DecimalFormat("#.##", simbolos);
        return df.format(h);
    }


}