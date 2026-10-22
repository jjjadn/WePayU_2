package br.ufal.ic.p2.wepayu.codificadores;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.Sistema;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class PersistenciaXML {
    private static final String ARQUIVO = "empregados.dat";

    public static void salvar(Sistema sistema) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO))) {
            out.writeObject(sistema);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar", e);
        }
    }

    public static Sistema carregar() {
        File f = new File(ARQUIVO);

        if (!f.exists() || f.length() == 0) {
            return criarSistemaPadrao();
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(f))) {
            Object obj = in.readObject();

            if (obj instanceof Sistema) {
                return (Sistema) obj;
            }
            return criarSistemaPadrao();

        } catch (Exception e) {
            return criarSistemaPadrao();
        }
    }

    public static void apagar() {
        File f = new File(ARQUIVO);
        if (f.exists()) {
            f.delete();
        }
    }

    private static Sistema criarSistemaPadrao() {
        Map<String, Empregado> empregados = new LinkedHashMap<>();
        Set<String> agendas = new HashSet<>(Arrays.asList(
                "semanal 5",
                "mensal $",
                "semanal 2 5"
        ));
        return new Sistema(empregados, agendas);
    }
}