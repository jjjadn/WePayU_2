package br.ufal.ic.p2.wepayu.servicos;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;

import br.ufal.ic.p2.wepayu.models.Cartao;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import br.ufal.ic.p2.wepayu.models.Venda;
import br.ufal.ic.p2.wepayu.validacao.ValidadorCartao;
import br.ufal.ic.p2.wepayu.validacao.ValidadorVenda;
import br.ufal.ic.p2.wepayu.exception.*;

public class GerenciadorPagamentos {

    private Map<String, Empregado> empregados;
    private GerenciadorEmpregados gerenciadorEmp;

    public GerenciadorPagamentos(Map<String, Empregado> empregados,
                                 GerenciadorEmpregados gerenciadorEmp) {
        this.empregados = empregados;
        this.gerenciadorEmp = gerenciadorEmp;
    }

    public void lancaCartao(String empId, String dataStr, String horasStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregado(empId);

        if (!"horista".equals(emp.getTipo())) {
            throw new EmpregadoNaoHoristaException();
        }

        LocalDate data = ValidadorCartao.validarData(dataStr, "");
        double horas = ValidadorCartao.validarHoras(horasStr);

        emp.adicionarCartao(new Cartao(data, horas));
    }

    public void lancaVenda(String empId, String dataStr, String valorStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregado(empId);

        if (!"comissionado".equals(emp.getTipo())) {
            throw new EmpregadoNaoComissionadoException();
        }

        LocalDate data = ValidadorCartao.validarData(dataStr, "");
        double valor = ValidadorVenda.validarValor(valorStr);

        emp.adicionarVenda(new Venda(data, valor));
    }

    public void lancaTaxaServico(String membro, String dataStr, String valorStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregadoPorMembro(membro);

        LocalDate data = ValidadorCartao.validarData(dataStr, "");
        double valor = ValidadorVenda.validarValor(valorStr);

        emp.adicionarTaxaServico(new TaxaServico(data, valor));
    }



    public String getHorasNormaisTrabalhadas(String empId, String dataInicialStr, String dataFinalStr) throws Exception {

        Empregado emp = validarHoristaPeriodo(empId, dataInicialStr, dataFinalStr);
        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        double total = 0;

        for (Cartao c : emp.getCartoes()) {
            LocalDate d = c.getData();
            if (!d.isBefore(inicio) && d.isBefore(fim)) {
                total += Math.min(c.getHoras(), 8);
            }
        }
        return formatarHoras(total);
    }

    public String getHorasExtrasTrabalhadas(String empId, String dataInicialStr, String dataFinalStr) throws Exception {Empregado emp = validarHoristaPeriodo(empId, dataInicialStr, dataFinalStr);
        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        double total = 0;
        for (Cartao c : emp.getCartoes()) {
            LocalDate d = c.getData();
            if (!d.isBefore(inicio) && d.isBefore(fim)) {
                total += Math.max(0, c.getHoras() - 8);
            }
        }
        return formatarHoras(total);
    }

    public String getVendasRealizadas(String empId, String dataInicialStr, String dataFinalStr) throws Exception {

        Empregado emp = validarComissionadoPeriodo(empId, dataInicialStr, dataFinalStr);
        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        double total = 0;
        for (Venda v : emp.getVendas()) {
            LocalDate d = v.getData();
            if (!d.isBefore(inicio) && d.isBefore(fim)) {
                total += v.getValor();
            }
        }
        return String.format(Locale.GERMANY, "%.2f", total);
    }

    public String getTaxasServico(String empId, String dataInicialStr, String dataFinalStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregado(empId);

        if (!emp.isSindicalizado()) {
            throw new EmpregadoNaoSindicalizadoException();
        }

        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        if (inicio.isAfter(fim)) {
            throw new DataInicialNaoPosteriorDataFinalException();
        }

        double total = 0;

        for (TaxaServico t : emp.getTaxaServicos()) {
            LocalDate d = t.getData();
            if (!d.isBefore(inicio) && d.isBefore(fim)) {
                total += t.getTaxa();
            }
        }
        return String.format(Locale.GERMANY, "%.2f", total);
    }


    private Empregado validarHoristaPeriodo(String empId, String dataInicialStr, String dataFinalStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregado(empId);

        if (!"horista".equals(emp.getTipo())) {
            throw new EmpregadoNaoHoristaException();
        }

        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        if (inicio.isAfter(fim)) {
            throw new DataInicialNaoPosteriorDataFinalException();
        }
        return emp;
    }

    private Empregado validarComissionadoPeriodo(String empId, String dataInicialStr, String dataFinalStr) throws Exception {

        Empregado emp = gerenciadorEmp.buscarEmpregado(empId);

        if (!"comissionado".equals(emp.getTipo())) {
            throw new EmpregadoNaoComissionadoException();
        }

        LocalDate inicio = ValidadorCartao.validarData(dataInicialStr, "inicial");
        LocalDate fim = ValidadorCartao.validarData(dataFinalStr, "final");

        if (inicio.isAfter(fim)) {
            throw new DataInicialNaoPosteriorDataFinalException();
        }
        return emp;
    }

    private String formatarHoras(double h) {

        java.text.DecimalFormatSymbols simbolos =
                new java.text.DecimalFormatSymbols(Locale.forLanguageTag("pt-BR"));
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##", simbolos);
        return df.format(h);
    }

    public class AgendaPagamento {
        private String descricao;

        public AgendaPagamento(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() { return descricao; }

        public boolean devePagar(LocalDate data) {

            switch (descricao) {
                case "semanal 5":
                    return data.getDayOfWeek() == DayOfWeek.FRIDAY;

                case "mensal $":
                    return data.getDayOfMonth() == data.lengthOfMonth();

                case "semanal 2 5":
                    if (data.getDayOfWeek() != DayOfWeek.FRIDAY) return false;
                    long semanas = ChronoUnit.WEEKS.between(LocalDate.of(2005, 1, 1), data);
                    return semanas % 2 == 1;
            }
            return false;
        }
    }



}