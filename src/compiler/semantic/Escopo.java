package compiler.semantic;

import java.util.Stack;

public class Escopo {

    private Stack<TabelaSimbolos> pilha;
    private int nivelAtual; 

    public Escopo() {
        pilha = new Stack<>();
        nivelAtual = -1;
        push(); 
    }

    public void push() {
        nivelAtual++;
        pilha.push(new TabelaSimbolos());
    }

    public void pop() {
        pilha.pop();
        nivelAtual--;
    }

    public void declararSimbolo(String lexema, String tipo, String categoria, int linha, int coluna) {
        TabelaSimbolos topo = pilha.peek();

        if (topo.existeNoMesmoEscopo(lexema)) {
            throw new RuntimeException(
                "Erro semântico: identificação '" + lexema + 
                "' redeclarada no mesmo escopo (linha " + linha + ", coluna " + coluna + ")."
            );
        }

        EntradaSimbolo entrada = new EntradaSimbolo(
            lexema, tipo, categoria, nivelAtual, linha, coluna
        );

        topo.inserir(entrada);
    }

    public EntradaSimbolo buscar(String lexema) {
        for (int i = pilha.size() - 1; i >= 0; i--) {
            EntradaSimbolo e = pilha.get(i).buscar(lexema);
            if (e != null) return e;
        }
        return null;
    }
}
