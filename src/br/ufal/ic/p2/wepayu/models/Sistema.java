package br.ufal.ic.p2.wepayu.models;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Empregado> empregados;
    private Set<String> agendas;

    public Sistema() {}

    
    public Sistema(Map<String, Empregado> empregados, Set<String> agendas) {
        this.empregados = empregados;
        this.agendas = agendas;
    }

    public Map<String, Empregado> getEmpregados() { return empregados; }
    public Set<String> getAgendas() { return agendas; }
}