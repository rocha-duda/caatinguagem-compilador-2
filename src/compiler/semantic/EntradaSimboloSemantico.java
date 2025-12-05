package compiler.semantic;

public class EntradaSimbolo {

    private String lexema;
    private String tipo;
    private String categoria; // variable, function, parameter
    private int escopo;
    private int linha;
    private int coluna;

    public EntradaSimbolo(String lexema, String tipo, String categoria,
                          int escopo, int linha, int coluna) {
        this.lexema = lexema;
        this.tipo = tipo;
        this.categoria = categoria;
        this.escopo = escopo;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getLexema() { return lexema; }
    public String getTipo() { return tipo; }
    public String getCategoria() { return categoria; }
    public int getEscopo() { return escopo; }
    public int getLinha() { return linha; }
    public int getColuna() { return coluna; }

    @Override
    public String toString() {
        return lexema + "\t" + tipo + "\t" + categoria + "\t" +
               escopo + "\t" + linha + "\t" + coluna;
    }
}
