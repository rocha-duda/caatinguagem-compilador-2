package compiler.symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import compiler.TipoToken;

public class TabelaSimbolos {
    private Map<String, EntradaSimbolos> tabela = new HashMap<>();
    
    private List<EntradaSimbolos> entradas = new ArrayList<>();
    
    public int registrarSimbolo(TipoToken codAtomo, String lexema, int caracPreTruncagem,
    		int caracPosTruncagem, int linhaOcorrencia) {
        String chave = lexema.toUpperCase();
        EntradaSimbolos existente = tabela.get(chave);

        if (existente != null) {
            existente.addLine(linhaOcorrencia);
            return existente.getNumEntrada();
        }

        // Criar nova entrada
        int novoNumero = entradas.size() + 1;

        EntradaSimbolos novaEntrada = new EntradaSimbolos(novoNumero, codAtomo, chave,
        		caracPreTruncagem, caracPosTruncagem,linhaOcorrencia);

        tabela.put(chave, novaEntrada);
        entradas.add(novaEntrada);

        return novoNumero;
    }

    public List<EntradaSimbolos> getEntradas() {
        return entradas;
    }
}
