package br.ufal.ic.p2.wepayu.calculadora;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import br.ufal.ic.p2.wepayu.models.Cartao;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.TaxaServico;
import br.ufal.ic.p2.wepayu.models.Venda;

/**
 * Calcula a maioria das pendencias da folha de pagamento
 */
public class CalculadoraFolhas {

    /**
     *
     * @param emp valida o ID
     * @param data recebe a data e valida se deve ser pago
     * @return
     */
    public static double calcular(Empregado emp, LocalDate data){
        if(!deveSerPago(emp, data)) return 0;

        switch (emp.getTipo()) {
            case "horista":
                return calcularHorista(emp, data);
            case "assalariado":
                return calcularAssalariado(emp, data);
            case "comissionado":
                return calcularComissionado(emp, data);
        }
        return 0;
    }


    public static boolean deveSerPago(Empregado emp, LocalDate data) {
        if (data.getDayOfWeek() != DayOfWeek.FRIDAY) {
            if ("assalariado".equals(emp.getTipo()))
                return data.getDayOfMonth() == data.lengthOfMonth();
            return false;
        }

        LocalDate contrato = LocalDate.of(2005, 1, 1);
        long semanas = ChronoUnit.WEEKS.between(contrato, data);

        switch (emp.getTipo()) {
            case "horista":      return true;
            case "comissionado": return semanas % 2 == 1;
            default:             return false;
        }
    }

    private static double calcularHorista(Empregado emp, LocalDate data) {
        LocalDate inicio = data.minusDays(7);
        double normais = 0, extras = 0;

        for (Cartao c : emp.getCartoes()) {
            if (!c.getData().isBefore(inicio) && c.getData().isBefore(data)) {
                normais += Math.min(c.getHoras(), 8);
                extras  += Math.max(0, c.getHoras() - 8);
            }
        }

        return (normais * emp.getSalario()) + (extras * 1.5 * emp.getSalario());
    }

    private static double calcularAssalariado(Empregado emp, LocalDate data) {
        return emp.getSalario();
    }

    private static double calcularComissionado(Empregado emp, LocalDate data) {
        LocalDate inicio = data.minusDays(15);
        double base = emp.getSalario() * 12.0 / 26.0;
        base = Math.floor(base * 100) / 100;

        double vendas = 0;
        for (Venda v : emp.getVendas()) {
            if (!v.getData().isBefore(inicio) && v.getData().isBefore(data)) {
                vendas += v.getValor();
            }
        }

        double bruto = base + (vendas * (emp.getComissao() == null ? 0 : emp.getComissao()));
        return Math.floor(bruto * 100) / 100;
    }

    public static double calcularDesconto(Empregado emp, LocalDate data) {
        if (!emp.isSindicalizado()) return 0;
        if (calcular(emp, data) == 0) return 0;

        LocalDate inicio = encontrarUltimoPagamento(emp, data);

        long dias = ChronoUnit.DAYS.between(inicio, data);
        double taxa = emp.getTaxaSindical() == null ? 0 : emp.getTaxaSindical();
        double desconto = dias * taxa;

        for (TaxaServico t : emp.getTaxaServicos()) {
            if (!t.getData().isBefore(inicio) && t.getData().isBefore(data)) {
                desconto += t.getTaxa();
            }
        }
        return desconto;
    }

    private static LocalDate encontrarUltimoPagamento(Empregado emp, LocalDate data) {
        LocalDate piso = LocalDate.of(2005, 1, 1);
        LocalDate candidato = dataAnterior(data, emp);

        while (candidato != null && !candidato.isBefore(piso)) {
            if (calcular(emp, candidato) > 0) return candidato;
            candidato = dataAnterior(candidato, emp);
        }
        return dataAnterior(data, emp);
    }

    /**
     *
     * @param d olha qual é a última data e se serve para
     * @param emp
     * @return retorna o valor das datas
     */
    private static LocalDate dataAnterior(LocalDate d, Empregado emp) {
        switch (emp.getTipo()) {
            case "horista":      return d.minusDays(7);
            case "comissionado": return d.minusDays(14);
            case "assalariado":  return d.withDayOfMonth(1).minusDays(1);
        }
        return d.minusDays(30);
    }

    private static double calcularDescontoSindical(Empregado emp, LocalDate inicio, LocalDate fim) {
        if (!emp.isSindicalizado()) return 0;

        long dias = ChronoUnit.DAYS.between(inicio, fim);
        double diario = emp.getTaxaSindical() == null ? 0 : emp.getTaxaSindical();
        double desconto = dias * diario;

        for (TaxaServico t : emp.getTaxaServicos()) {
            if (!t.getData().isBefore(inicio) && t.getData().isBefore(fim)) {
                desconto += t.getTaxa();
            }
        }
        return desconto;
    }
}


