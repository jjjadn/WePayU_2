package br.ufal.ic.p2.wepayu;

import java.io.File;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.Comparator;

import br.ufal.ic.p2.wepayu.models.Cartao;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import br.ufal.ic.p2.wepayu.models.Venda;
import br.ufal.ic.p2.wepayu.persistencia.PersistenciaXML;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.validacao.ValidadorCartao;
import br.ufal.ic.p2.wepayu.validacao.ValidadorEmpregado;
import br.ufal.ic.p2.wepayu.validacao.ValidadorTaxa;
import br.ufal.ic.p2.wepayu.validacao.ValidadorVenda;
import br.ufal.ic.p2.wepayu.calculadora.CalculadoraFolhas;

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
                    throw new Exception("Empregado nao eh comissionado.");
                }
                return String.format(Locale.GERMANY, "%.2f", emp.getComissao());
            case "sindicalizado":
                return String.valueOf(emp.isSindicalizado());

            case "metodoPagamento":
                return emp.getMetodoPagamento();

            case "banco":
                if(!"banco".equals(emp.getMetodoPagamento())){
                throw new Exception("Empregado nao recebe em banco.");
                }
                return emp.getBanco();

            case "agencia":
                if(!"banco".equals(emp.getMetodoPagamento())){
                    throw new Exception("Empregado nao recebe em banco.");
                }
                return emp.getAgencia();

            case "contaCorrente":
                if(!"banco".equals(emp.getMetodoPagamento())){
                    throw new Exception("Empregado nao recebe em banco.");
                }
                return emp.getContaCorrente();

            case "idSindicato":
                if(!emp.isSindicalizado()){
                    throw new Exception("Empregado nao eh sindicalizado.");
                }
                return emp.getIdSindicato();

            case "taxaSindical":
                if(!emp.isSindicalizado()){
                    throw new Exception("Empregado nao eh sindicalizado.");
                }
                return String.format(Locale.GERMANY, "%.2f", emp.getTaxaSindical());

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

    public String getVendasRealizadas(String empId, String dataInicial, String dataFinal) throws Exception{
        Empregado emp = buscarEmpregado(empId);

        if(!"comissionado".equals(emp.getTipo())){
            throw new Exception("Empregado nao eh comissionado.");
        }
        LocalDate inicial = ValidadorCartao.validarData(dataInicial, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinal, "final");
        if(inicial.isAfter(fim)){
            throw new Exception("Data inicial nao pode ser posterior aa data final.");
        }
        double total = 0;
        for (Venda v : emp.getVendas()) {
            if (!v.getData().isBefore(inicial) && v.getData().isBefore(fim)) {
                total += v.getValor();
            }
        }

        return String.format(Locale.GERMANY, "%.2f", total);
    }

    public void lancaVenda(String empId, String data, String valor)throws Exception{
        Empregado emp = buscarEmpregado(empId);

        if(!"comissionado".equals(emp.getTipo())){
            throw new Exception("Empregado nao eh comissionado.");
        }
        LocalDate d = ValidadorCartao.validarData(data, "");
        double v = ValidadorVenda.validarValor(valor);
        Venda venda = new Venda(d, v);
        emp.adicionarVenda(venda);
        PersistenciaXML.salvar(empregados);
    }

    public void lancaTaxaServico(String membro, String data, String valor)throws Exception{
        Empregado emp = buscarEmpregadoPorMembro(membro);

        LocalDate d = ValidadorCartao.validarData(data, "");
        double t = ValidadorVenda.validarValor(valor);

        TaxaServico taxa = new TaxaServico(d,t);
        emp.adicionarTaxaServico(taxa);
        PersistenciaXML.salvar(empregados);
    }
    public String getTaxasServico(String empId, String dataInicial, String dataFinal) throws Exception{
        Empregado emp = buscarEmpregado(empId);
        if (!emp.isSindicalizado()) {
            throw new Exception("Empregado nao eh sindicalizado.");
        }
        LocalDate inicial = ValidadorCartao.validarData(dataInicial, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinal, "final");

        if(inicial.isAfter(fim)){
            throw new Exception("Data inicial nao pode ser posterior aa data final.");
        }
        double total = 0;
        for (TaxaServico t : emp.getTaxaServicos()) {
            if (!t.getData().isBefore(inicial) && t.getData().isBefore(fim)) {
                total += t.getTaxa();
            }
        }
        return String.format(Locale.GERMANY, "%.2f", total);

    }


    private Empregado buscarEmpregadoPorMembro(String membro) throws Exception{
        if (membro == null || membro.trim().isEmpty()){
            throw new Exception("Identificacao do membro nao pode ser nula.");
        }
        for(Empregado e: empregados.values()){
            if(membro.equals(e.getIdSindicato())){
                return e;
            }
        }
        throw new Exception("Membro nao existe.");
    }

    public void alteraEmpregado(String empId, String atributo, String valor) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        switch (atributo) {
            case "nome":
                if (valor == null || valor.trim().isEmpty())
                    throw new Exception("Nome nao pode ser nulo.");
                emp.setNome(valor);
                break;

            case "endereco":
                if (valor == null || valor.trim().isEmpty())
                    throw new Exception("Endereco nao pode ser nulo.");
                emp.setEndereco(valor);
                break;


            case "tipo":
                if (!"horista".equals(valor) && !"assalariado".equals(valor)
                        && !"comissionado".equals(valor))
                    throw new Exception("Tipo invalido.");
                emp.setTipo(valor);
                break;

            case "salario":
                emp.setSalario(ValidadorEmpregado.validarSalario(valor));
                break;

            case "comissao":
                if (!"comissionado".equals(emp.getTipo()))
                    throw new Exception("Empregado nao eh comissionado.");
                emp.setComissao(ValidadorEmpregado.validarComissao(valor));
                break;

            case "metodoPagamento":

                if (!"emMaos".equals(valor) && !"correios".equals(valor))
                    throw new Exception("Metodo de pagamento invalido.");
                emp.setMetodoPagamento(valor);
                emp.setBanco(null);
                emp.setAgencia(null);
                emp.setContaCorrente(null);
                break;

            case "sindicalizado":

                if (!"true".equals(valor) && !"false".equals(valor))

                    throw new Exception("Valor deve ser true ou false.");

                if ("false".equals(valor)) {
                    emp.setSindicalizado(false);
                    emp.setIdSindicato(null);
                    emp.setTaxaSindical(null);
                } else {
                    throw new Exception("Identificacao do sindicato nao pode ser nula.");
                }
                break;

            default:
                throw new Exception("Atributo nao existe.");
        }

        PersistenciaXML.salvar(empregados);
    }
    public void alteraEmpregado(String empId, String atributo, String valor, String extra) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        if (!"tipo".equals(atributo))
            throw new Exception("Atributo nao existe.");

        if (!"horista".equals(valor) && !"assalariado".equals(valor)
                && !"comissionado".equals(valor))
            throw new Exception("Tipo invalido.");

        emp.setTipo(valor);

        if ("comissionado".equals(valor)) {
            emp.setComissao(ValidadorEmpregado.validarComissao(extra));
        } else {
            emp.setSalario(ValidadorEmpregado.validarSalario(extra));
            emp.setComissao(null);
        }

        PersistenciaXML.salvar(empregados);
    }
    public void alteraEmpregado(String empId, String atributo, String valor,
                                String idSindicato, String taxaSindical) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        if (!"sindicalizado".equals(atributo))
            throw new Exception("Atributo nao existe.");

        if (!"true".equals(valor) && !"false".equals(valor))
            throw new Exception("Valor deve ser true ou false.");

        if ("false".equals(valor)) {

            emp.setSindicalizado(false);
            emp.setIdSindicato(null);
            emp.setTaxaSindical(null);
        } else {

            if (idSindicato == null || idSindicato.trim().isEmpty())
                throw new Exception("Identificacao do sindicato nao pode ser nula.");

            if (taxaSindical == null || taxaSindical.trim().isEmpty())
                throw new Exception("Taxa sindical nao pode ser nula.");

            double taxa;

            try {
                taxa = Double.parseDouble(taxaSindical.replace(",", "."));
            } catch (NumberFormatException e) {
                throw new Exception("Taxa sindical deve ser numerica.");
            }

            if (taxa < 0)
                throw new Exception("Taxa sindical deve ser nao-negativa.");

            for (Empregado e : empregados.values()) {
                if (!e.getId().equals(empId) && idSindicato.equals(e.getIdSindicato()))
                    throw new Exception("Ha outro empregado com esta identificacao de sindicato");
            }

            emp.setSindicalizado(true);
            emp.setIdSindicato(idSindicato);
            emp.setTaxaSindical(taxa);
        }

        PersistenciaXML.salvar(empregados);
    }
    public void alteraEmpregado(String empId, String atributo, String valor1, String banco, String agencia, String contaCorrente) throws Exception {
        Empregado emp = buscarEmpregado(empId);

        if (!"metodoPagamento".equals(atributo))
            throw new Exception("Atributo nao existe.");
        if (!"banco".equals(valor1))
            throw new Exception("Metodo de pagamento invalido.");
        if (banco == null || banco.trim().isEmpty())
            throw new Exception("Banco nao pode ser nulo.");
        if (agencia == null || agencia.trim().isEmpty())
            throw new Exception("Agencia nao pode ser nulo.");
        if (contaCorrente == null || contaCorrente.trim().isEmpty())
            throw new Exception("Conta corrente nao pode ser nulo.");

        emp.setMetodoPagamento("banco");
        emp.setBanco(banco);
        emp.setAgencia(agencia);
        emp.setContaCorrente(contaCorrente);

        PersistenciaXML.salvar(empregados);
    }

    public String totalFolha(String dataStr) throws Exception {
        LocalDate data = ValidadorCartao.validarData(dataStr, "");
        double total = 0;
        for (Empregado emp : empregados.values()) {
            total += CalculadoraFolhas.calcular(emp, data);
        }
        return String.format(Locale.GERMANY, "%.2f", total);
    }

    public void rodaFolha(String dataStr, String saida) throws Exception {
        LocalDate data = ValidadorCartao.validarData(dataStr, "");


        List<Empregado> horistas = new ArrayList<>();
        List<Empregado> assalariados = new ArrayList<>();
        List<Empregado> comissionados = new ArrayList<>();

        for (Empregado e : empregados.values()) {
            switch (e.getTipo()) {
                case "horista":      horistas.add(e); break;
                case "assalariado":  assalariados.add(e); break;
                case "comissionado": comissionados.add(e); break;
            }
        }
        horistas.sort(Comparator.comparing(Empregado::getNome));
        assalariados.sort(Comparator.comparing(Empregado::getNome));
        comissionados.sort(Comparator.comparing(Empregado::getNome));

        try (PrintWriter out = new PrintWriter(new FileWriter(saida))) {

            out.println("FOLHA DE PAGAMENTO DO DIA " + data.toString());
            out.println("=".repeat(36));
            out.println();

            double totalGeral = 0;

            totalGeral += escreverHoristas(out, horistas, data);
            totalGeral += escreverAssalariados(out, assalariados, data);
            totalGeral += escreverComissionados(out, comissionados, data);


            out.println("TOTAL FOLHA: " + String.format(Locale.GERMANY, "%.2f", totalGeral));
        }
    }

    private double escreverHoristas(PrintWriter out, List<Empregado> lista, LocalDate data) {
        String titulo = "===================== HORISTAS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-36s %5s %5s %13s %9s %15s %s",
                "Nome", "Horas", "Extra", "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("==================================== ===== ===== ============= ========= =============== ======================================");

        double somaBruto = 0, somaDesc = 0, somaLiq = 0;
        int somaHoras = 0, somaExtra = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            int horas = 0, extra = 0;
            LocalDate inicio = data.minusDays(7);
            for (Cartao c : emp.getCartoes()) {
                if (!c.getData().isBefore(inicio) && c.getData().isBefore(data)) {
                    horas += (int) Math.min(c.getHoras(), 8);
                    extra += (int) Math.max(0, c.getHoras() - 8);
                }
            }

            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);



            out.println(String.format("%-36s %5d %5d %13s %9s %15s %s",
                    emp.getNome(), horas, extra,
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
            somaHoras += horas; somaExtra += extra;
        }

        out.println();
        out.println(String.format("%-36s %5d %5d %13s %9s %15s",
                "TOTAL HORISTAS", somaHoras, somaExtra,
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }
    private double escreverAssalariados(PrintWriter out, List<Empregado> lista, LocalDate data) {
        String titulo = "===================== ASSALARIADOS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-48s %13s %9s %15s %s",
                "Nome", "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("================================================ ============= ========= =============== ======================================");

        double somaBruto = 0, somaDesc = 0, somaLiq = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);



            out.println(String.format("%-48s %13s %9s %15s %s",
                    emp.getNome(),
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
        }

        out.println();
        out.println(String.format("%-48s %13s %9s %15s",
                "TOTAL ASSALARIADOS",
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }

    private double escreverComissionados(PrintWriter out, List<Empregado> lista, LocalDate data) {
        String titulo = "===================== COMISSIONADOS ";
        out.println("=".repeat(127));
        out.println(titulo + "=".repeat(127 - titulo.length()));
        out.println("=".repeat(127));
        out.println(String.format("%-21s %-8s %-8s %-8s %13s %9s %15s %s",
                "Nome", "Fixo", "Vendas", "Comissao", "Salario Bruto", "Descontos", "Salario Liquido", "Metodo"));
        out.println("===================== ======== ======== ======== ============= ========= =============== ======================================");

        double somaFixo = 0, somaVendas = 0, somaComissao = 0;
        double somaBruto = 0, somaDesc = 0, somaLiq = 0;

        for (Empregado emp : lista) {
            if (!CalculadoraFolhas.deveSerPago(emp, data)) continue;

            double base = Math.floor(emp.getSalario() * 12.0 / 26.0 * 100) / 100;
            double vendas = 0;
            LocalDate inicio = data.minusDays(14);
            for (Venda v : emp.getVendas()) {
                if (!v.getData().isBefore(inicio) && v.getData().isBefore(data)) {
                    vendas += v.getValor();
                }
            }
            double comissao = vendas * (emp.getComissao() == null ? 0 : emp.getComissao());
            comissao = Math.floor(comissao * 100) / 100;
            double bruto = CalculadoraFolhas.calcular(emp, data);
            double desc  = CalculadoraFolhas.calcularDesconto(emp, data);
            double liq   = Math.max(0, bruto - desc);



            out.println(String.format("%-21s %8s %8s %8s %13s %9s %15s %s",
                    emp.getNome(),
                    String.format(Locale.GERMANY, "%.2f", base),
                    String.format(Locale.GERMANY, "%.2f", vendas),
                    String.format(Locale.GERMANY, "%.2f", comissao),
                    String.format(Locale.GERMANY, "%.2f", bruto),
                    String.format(Locale.GERMANY, "%.2f", desc),
                    String.format(Locale.GERMANY, "%.2f", liq),
                    formatarMetodo(emp)));

            somaFixo += base; somaVendas += vendas; somaComissao += comissao;
            somaBruto += bruto; somaDesc += desc; somaLiq += liq;
        }

        out.println();
        out.println(String.format("%-21s %8s %8s %8s %13s %9s %15s",
                "TOTAL COMISSIONADOS",
                String.format(Locale.GERMANY, "%.2f", somaFixo),
                String.format(Locale.GERMANY, "%.2f", somaVendas),
                String.format(Locale.GERMANY, "%.2f", somaComissao),
                String.format(Locale.GERMANY, "%.2f", somaBruto),
                String.format(Locale.GERMANY, "%.2f", somaDesc),
                String.format(Locale.GERMANY, "%.2f", somaLiq)));
        out.println();

        return somaBruto;
    }

    private String formatarMetodo(Empregado emp) {
        String m = emp.getMetodoPagamento();
        if ("emMaos".equals(m)) return "Em maos";
        if ("correios".equals(m)) return "Correios, " + emp.getEndereco();
        if ("banco".equals(m)) {
            return emp.getBanco() + ", Ag. " + emp.getAgencia() + " CC " + emp.getContaCorrente();
        }
        return m;
    }
}