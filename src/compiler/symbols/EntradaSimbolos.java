package compiler.symbols;

import java.util.ArrayList;
import java.util.List;
import compiler.TipoToken;

public class EntradaSimbolos {

    private int numEntrada;
    private TipoToken codAtomo;
    private String lexema;
    private String tipo;
    private int caracPreTruncagem;
    private int caracPosTruncagem;
    private List<Integer> ocorrencias = new ArrayList<>();

    public EntradaSimbolos(int numEntrada, TipoToken codAtomo, String lexema, 
    		int caracPreTruncagem, int caracPosTruncagem, int linhaOcorrencia) {
        this.numEntrada = numEntrada;
        this.codAtomo = codAtomo;
        this.lexema = lexema;
        this.caracPreTruncagem = caracPreTruncagem;
        this.caracPosTruncagem = caracPosTruncagem;
        this.tipo = null;
        this.ocorrencias.add(linhaOcorrencia);
    }

    public int getNumEntrada() {
        return numEntrada;
    }

    public TipoToken getCodAtomo() {
        return codAtomo;
    }

    public void setCodAtomo(TipoToken codAtomo) {
        this.codAtomo = codAtomo;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCaracPreTruncagem() {
        return caracPreTruncagem;
    }

    public int getCaracPosTruncagem() {
        return caracPosTruncagem;
    }

    public List<Integer> getOcorrencias() {
        return ocorrencias;
    }

    public void addLine(int linhaOcorrencia) {
        // guarda até as 5 primeiras linhas
        if (ocorrencias.contains(linhaOcorrencia) == false && ocorrencias.size() < 5) {
            ocorrencias.add(linhaOcorrencia);
        }
    }
}
