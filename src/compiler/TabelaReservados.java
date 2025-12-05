package compiler;

import java.util.HashMap;

public class TabelaReservados {

    private static final HashMap<String, TipoToken> reservados = new HashMap<>();

    static { // carrega os dados na tabela de reservados

        // DECLARAÇÕES
        reservados.put("VARTYPE", TipoToken.VAR_TYPE);
        reservados.put("FUNCTYPE", TipoToken.FUNC_TYPE);
        reservados.put("PARAMTYPE", TipoToken.PARAM_TYPE);

        // ESTRUTURA DO PROGRAMA
        reservados.put("PROGRAM", TipoToken.PROGRAM);
        reservados.put("DECLARATIONS", TipoToken.DECLARATIONS);
        reservados.put("ENDDECLARATIONS", TipoToken.END_DECLARATIONS);
        reservados.put("FUNCTIONS", TipoToken.FUNCTIONS);
        reservados.put("ENDFUNCTIONS", TipoToken.END_FUNCTIONS);
        reservados.put("ENDFUNCTION", TipoToken.END_FUNCTION);
        reservados.put("ENDPROGRAM", TipoToken.END_PROGRAM);

        // TIPOS
        reservados.put("INTEGER", TipoToken.INTEGER);
        reservados.put("REAL", TipoToken.REAL);
        reservados.put("CHARACTER", TipoToken.CHARACTER);
        reservados.put("STRING", TipoToken.STRING);
        reservados.put("BOOLEAN", TipoToken.BOOLEAN);
        reservados.put("VOID", TipoToken.VOID);

        // COMANDOS
        reservados.put("IF", TipoToken.IF);
        reservados.put("ELSE", TipoToken.ELSE);
        reservados.put("ENDIF", TipoToken.END_IF);
        reservados.put("WHILE", TipoToken.WHILE);
        reservados.put("ENDWHILE", TipoToken.END_WHILE);
        reservados.put("RETURN", TipoToken.RETURN);
        reservados.put("BREAK", TipoToken.BREAK);
        reservados.put("PRINT", TipoToken.PRINT);

        // BOOLEANOS
        reservados.put("TRUE", TipoToken.TRUE);
        reservados.put("FALSE", TipoToken.FALSE);

        // SÍMBOLO '?'
        reservados.put("?", TipoToken.INTERROGACAO);
    }

    public static boolean ehReservado(String lexeme) {
        if (lexeme == null) return false;
        return reservados.containsKey(lexeme.toUpperCase());
    }

    public static TipoToken buscarToken(String lexeme) {
        if (lexeme == null) return TipoToken.UNKNOWN;
        return reservados.get(lexeme.toUpperCase());
    }
}
