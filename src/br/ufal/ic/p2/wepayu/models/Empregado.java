package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empregado implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private String endereco;
    private String tipo;
    private double salario;
    private Double comissao;
    private boolean sindicalizado;
    private String idSindicato;
    private Double taxaSindical;
    private List<Cartao> cartoes = new ArrayList<>();
    private List<Venda> vendas = new ArrayList<>();
    private List<TaxaServico> taxaServicos = new ArrayList<>();
    private String metodoPagamento = "emMaos";
    private String banco;
    private String agencia;
    private String contaCorrente;
    private LocalDate ultimaDataPagamento;


    public Empregado(){}

    public Empregado(String id, String nome, String endereco, String tipo, double salario, Double comissao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.tipo = tipo;
        this.salario = salario;
        this.comissao = comissao;
        this.sindicalizado = false;
    }


    public String getId() {return id;}
    public String getNome() {return nome;}
    public String getEndereco() {return endereco;}
    public String getTipo() {return tipo;}
    public double getSalario() {return salario;}
    public Double getComissao() {return comissao;}
    public boolean isSindicalizado() {return sindicalizado;}
    public List<Cartao> getCartoes() {return cartoes;}
    public List<Venda> getVendas(){return vendas;}
    public List<TaxaServico> getTaxaServicos(){return taxaServicos;}
    public String getIdSindicato(){return idSindicato;}
    public Double getTaxaSindical(){return taxaSindical;}
    public String getMetodoPagamento(){return metodoPagamento;}
    public String getBanco(){return banco;}
    public String getAgencia(){return agencia;}
    public String getContaCorrente(){return contaCorrente;}
    public LocalDate getUltimaDataPagamento(){return ultimaDataPagamento; }


    public void setId(String id) { this.id = id; }
    public void setNome(String nome) {this.nome = nome;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setSalario(double salario) {this.salario = salario;}
    public void setComissao(Double comissao) {this.comissao = comissao;}
    public void setSindicalizado(boolean sindicalizado) {this.sindicalizado = sindicalizado;}

    public void setCartoes(List<Cartao> cartoes) {this.cartoes = cartoes;}
    public void setVendas(List<Venda> vendas){this.vendas = vendas;}

    public void setTaxaServicos(List<TaxaServico> taxaServicos) {this.taxaServicos = taxaServicos;}
    public void setIdSindicato(String idSindicato){this.idSindicato = idSindicato;}
    public void setTaxaSindical(Double taxaSindical){this.taxaSindical = taxaSindical;}

    public void setContaCorrente(String contaCorrente) {this.contaCorrente = contaCorrente;}
    public void setBanco(String banco) {this.banco = banco;}
    public void setAgencia(String agencia) {this.agencia = agencia;}
    public void setMetodoPagamento(String metodoPagamento) {this.metodoPagamento = metodoPagamento;}

    public void adicionarCartao(Cartao c){this.cartoes.add(c);}
    public void adicionarVenda(Venda v) {this.vendas.add(v);}
    public void adicionarTaxaServico(TaxaServico t){this.taxaServicos.add(t);}
    public void setUltimaDataPagamento(LocalDate d){this.ultimaDataPagamento = d;}
}