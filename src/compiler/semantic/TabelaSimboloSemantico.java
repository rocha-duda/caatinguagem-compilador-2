package compiler.semantic;

import java.util.HashMap;

public class TabelaSimbolos {

    private HashMap<String, EntradaSimbolo> tabela;

    public TabelaSimbolos() {
        tabela = new HashMap<>();
    }

    public boolean existeNoMesmoEscopo(String lexema) {
        return tabela.containsKey(lexema);
    }

    public void inserir(EntradaSimbolo entrada) {
        tabela.put(entrada.getLexema(), entrada);
    }

    public EntradaSimbolo buscar(String lexema) {
        return tabela.get(lexema);
    }

    public HashMap<String, EntradaSimbolo> getTabela() {
        return tabela;
    }
}
