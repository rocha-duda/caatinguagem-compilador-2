package compiler;

	public enum TipoToken {

	    // PALAVRAS CHAVE
	    PROGRAM,
	    DECLARATIONS,
	    END_DECLARATIONS,
	    FUNCTIONS,
	    END_FUNCTIONS,
	    END_FUNCTION,
	    END_PROGRAM,
	    VAR_TYPE,
	    FUNC_TYPE,
	    PARAM_TYPE,
	
	    // TIPOS
	    INTEGER,
	    REAL,
	    CHARACTER,
	    STRING,
	    BOOLEAN,
	    VOID,
	
	    // CONSTANTES BOOLEANAS
	    TRUE,
	    FALSE,
	
	    // COMANDOS
	    IF,
	    ELSE,
	    END_IF,
	    WHILE,
	    END_WHILE,
	    RETURN,
	    BREAK,
	    PRINT,
	
	    // OPERADORES ARITMÉTICOS
	    ADICAO, // +
	    SUBTRACAO, // - 
	    MULTIPLICACAO, // *
	    DIVISAO, // /
	    RESTO, // %
	
	    // OPERADORES RELACIONAIS
	    MENOR_IGUAL, // <=
	    MENOR, // <
	    MAIOR, // >
	    MAIOR_IGUAL, // >=
	    IGUALDADE, // ==
	    DESIGUALDADE_EXCL, // !=
	    DESIGUALDADE_HASH, // #
	
	    // SÍMBOLOS
	    ATRIBUIR, // :=
	    DOIS_PONTOS, // :
	    PONTO_VIRGULA, // ;
	    VIRGULA, // ,
	    PARENTESE_ESQ, // (
	    PARENTESE_DIR, // )
	    CHAVE_ESQ, // {
	    CHAVE_DIR, // }
	    COLCHETE_ESQ, // [
	    COLCHETE_DIR, // ]
	    INTERROGACAO, // ?
	
	    // IDENTIFICADORES E CONSTANTES
	    IDENTIFIER,
	    INT_CONST,
	    REAL_CONST,
	    STRING_CONST,
	    CHAR_CONST,
	
	    // OUTROS
	    EOF,
	    UNKNOWN
	}
