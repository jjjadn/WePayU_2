package br.ufal.ic.p2.wepayu.persistencia;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class PersistenciaXML {
    private static final String ARQUIVO = "empregados.dat";

    @SuppressWarnings("unchecked")
    public static void salvar(Map<String, Empregado> empregados) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO))) {
            out.writeObject(new LinkedHashMap<>(empregados));
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Empregado> carregar() {
        File f = new File(ARQUIVO);
        if (!f.exists() || f.length() == 0) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof Map<?, ?>) {
                return (Map<String, Empregado>) obj;
            }
            return new LinkedHashMap<>();
        } catch (Exception e) {
            return new LinkedHashMap<>();
        }
    }

    public static void apagar() {
        File f = new File(ARQUIVO);
        if (f.exists()) f.delete();
    }
}