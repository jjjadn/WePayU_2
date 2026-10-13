package br.ufal.ic.p2.wepayu.servicos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Stack;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.exception.SemComandoParaDesfazerException;
import br.ufal.ic.p2.wepayu.exception.SemComandoParaRefazerException;

public class HistoricoOperacoes {

    private Stack<Map<String, Empregado>> pilhaUndo = new Stack<>();
    private Stack<Map<String, Empregado>> pilhaRedo = new Stack<>();

    public void salvarEstado(Map<String, Empregado> empregados) {
        pilhaUndo.push(deepCopy(empregados));
        pilhaRedo.clear();
    }

    public Map<String, Empregado> undo(Map<String, Empregado> atual) throws Exception {

        if (pilhaUndo.isEmpty()) {
            throw new SemComandoParaDesfazerException();
        }
        pilhaRedo.push(deepCopy(atual));
        return pilhaUndo.pop();
    }

    public void descartarUltimo() {
        if (!pilhaUndo.isEmpty()) {
            pilhaUndo.pop();
        }
    }

    public Map<String, Empregado> redo(Map<String, Empregado> atual) throws Exception {

        if (pilhaRedo.isEmpty()) {
            throw new SemComandoParaRefazerException();
        }
        pilhaUndo.push(deepCopy(atual));
        return pilhaRedo.pop();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Empregado> deepCopy(Map<String, Empregado> original) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            new ObjectOutputStream(baos).writeObject(original);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            return (Map<String, Empregado>) new ObjectInputStream(bais).readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}