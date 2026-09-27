package br.ufal.ic.p2.wepayu.Exception;

//Posteriormente, farei todos os Exception nesse package, mas no momento, já tinha iniciado sem a utilização do mesmo,
//Na segunda parte do projeto, irei resolver as pendencias



public class EmpregadoNaoExisteException extends Exception{
    public EmpregadoNaoExisteException(){
        super("Empregado nao existe.");
    }
}
