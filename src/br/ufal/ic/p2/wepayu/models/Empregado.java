package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
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
    private List<Cartao> cartoes = new ArrayList<>();
    private List<Venda> vendas = new ArrayList<>();


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

    public void setId(String id) { this.id = id; }
    public void setNome(String nome) {this.nome = nome;}
    public void setEndereco(String endereco) {this.endereco = endereco;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public void setSalario(double salario) {this.salario = salario;}
    public void setComissao(Double comissao) {this.comissao = comissao;}
    public void setSindicalizado(boolean sindicalizado) {this.sindicalizado = sindicalizado;}

    public void setCartoes(List<Cartao> cartoes) {this.cartoes = cartoes;}
    public void setVendas(List<Venda> vendas){this.vendas = vendas;}

    public void adicionarCartao(Cartao c){
        this.cartoes.add(c);
    }
    public void adicionarVenda(Venda v) {this.vendas.add(v);}


}