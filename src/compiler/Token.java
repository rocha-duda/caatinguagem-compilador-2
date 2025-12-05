package compiler;

public class Token {

    private final int linha;
    private final String lexema;
    private final TipoToken tipo;
    private final int coluna;

    public Token(TipoToken tipo, String lexema, int linha, int coluna) {
        this.coluna = coluna;
        this.lexema = lexema;
        this.linha = linha;
        this.tipo = tipo;
       
    }

      public int getColuna() {
        return coluna;
    }
    public int getLinha() {
        return linha;
    }
public String getLexema() {
        return lexema;
    }
 
 public TipoToken getTipo() {
        return tipo;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Token{tyipo=%s, lexema='%s', linha=%d, coluna=%d}",
            tipo, lexema, linha, coluna
        );
    }
}