package compiler;

import java.util.Stack;
import compiler.symbols.TabelaSimbolos;
import compiler.symbols.EntradaSimbolos;
import compiler.TipoToken;

public class Escopo {

    private Stack<TabelaSimbolos> pilha;

    public Escopo() {
        pilha = new Stack<>();
        push();  // cria escopo global
    }

    // entra em um novo escopo
    public void push() {
        pilha.push(new TabelaSimbolos());
    }

    // sai do escopo atual
    public void pop() {
        pilha.pop();
    }

    // registrar identificador no escopo atual
    public void declarar(String lexema, TipoToken tipo, int linha) {

        TabelaSimbolos atual = pilha.peek();

        if (atual.buscar(lexema.toUpperCase()) != null) {
            throw new RuntimeException(
                "Erro: identificador '" + lexema + "' redeclarado na linha " + linha
            );
        }

        atual.registrarSimbolo(tipo, lexema, 0, 0, linha);
    }

    // buscar identificador (procura de baixo pra cima)
    public EntradaSimbolos buscar(String lexema) {

        String chave = lexema.toUpperCase();

        for (int i = pilha.size() - 1; i >= 0; i--) {
            EntradaSimbolos ent = pilha.get(i).buscar(chave);
            if (ent != null) return ent;
        }

        return null;
    }
}
