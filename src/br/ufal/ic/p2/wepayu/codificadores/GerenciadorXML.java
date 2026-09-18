package br.ufal.ic.p2.wepayu.codificadores;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class GerenciadorXML {

    public static void gravarObjeto(Object objeto, String caminhoArquivo) {
        try (XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(caminhoArquivo)))) {
            e.writeObject(objeto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}