package compiler;

import compiler.symbols.TabelaSimbolos;

public class AnalisadorLexico {

    private LeitorFonte leitor;
    private TabelaSimbolos tabelaSimbolos;

    private static final int TAM_MAX_LEXEMA = 35;

    public AnalisadorLexico(LeitorFonte leitor, TabelaSimbolos tabelaSimbolos) {
        this.leitor = leitor;
        this.tabelaSimbolos = tabelaSimbolos;
    }

    // função para pular os espaços em branco

    private void pularEspacos() {
        while (!leitor.ehEOF()) {
            char c = leitor.olhar();
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                leitor.lerCaractere();
            } else {
                break;
            }
        }
    }

    // função para ler identificador ou símbolo reservado
    private Token lerIdentOuReservado(int linhaInicio, int colunaInicio) {

        StringBuilder sb = new StringBuilder();
        int caracPre = 0;

        while (!leitor.ehEOF()) {
            char c = leitor.olhar();
            if (Character.isLetterOrDigit(c) || c == '_') {
                caracPre++;
                if (sb.length() < TAM_MAX_LEXEMA) {
                    sb.append(Character.toUpperCase(c));
                }
                leitor.lerCaractere();
            } else {
                break;
            }
        }

        String lexema = sb.toString();
        int caracPos = lexema.length();

        // verifica se é reservado
        if (TabelaReservados.ehReservado(lexema)) {
            TipoToken tipo = TabelaReservados.buscarToken(lexema);
            return new Token(tipo, lexema, linhaInicio, colunaInicio);
        }

        // não é reservado → identificador (entra na tabela de símbolos)
        tabelaSimbolos.registrarSimbolo(
                TipoToken.IDENTIFIER,
                lexema,
                caracPre,
                caracPos,
                linhaInicio
        );

        return new Token(TipoToken.IDENTIFIER, lexema, linhaInicio, colunaInicio);
    }

    // ------------------------------------------------------------------------
    // NÚMEROS (INT / REAL)
    // ------------------------------------------------------------------------

    private Token lerNumero(int linhaInicio, int colunaInicio) {

        StringBuilder sb = new StringBuilder();
        boolean temPonto = false;

        while (!leitor.ehEOF()) {
            char c = leitor.olhar();
            if (Character.isDigit(c)) {
                if (sb.length() < TAM_MAX_LEXEMA) {
                    sb.append(c);
                }
                leitor.lerCaractere();
            } else if (c == '.' && !temPonto) {
                temPonto = true;
                if (sb.length() < TAM_MAX_LEXEMA) {
                    sb.append(c);
                }
                leitor.lerCaractere();
            } else {
                break;
            }
        }

        String lexema = sb.toString();

        if (temPonto) {
            return new Token(TipoToken.REAL_CONST, lexema, linhaInicio, colunaInicio);
        } else {
            return new Token(TipoToken.INT_CONST, lexema, linhaInicio, colunaInicio);
        }
    }


    // ------------------------------------------------------------------------
    // STRINGS
    // ------------------------------------------------------------------------

    private Token lerString(int linhaInicio, int colunaInicio) {

        StringBuilder sb = new StringBuilder();

        // consumir a aspa inicial "
        leitor.lerCaractere();

        while (!leitor.ehEOF()) {
            char c = leitor.olhar();

            if (c == '"') {
                // fim da string
                leitor.lerCaractere();
                break;
            }

            if (sb.length() < TAM_MAX_LEXEMA) {
                sb.append(c);
            }

            leitor.lerCaractere();
        }

        String lexema = sb.toString();
        return new Token(TipoToken.STRING_CONST, lexema, linhaInicio, colunaInicio);
    }


    // CHAR

    private Token lerChar(int linhaInicio, int colunaInicio) {

        StringBuilder sb = new StringBuilder();

        // consumir a aspa simples de abertura '
        leitor.lerCaractere();

        // tenta ler um caractere de conteúdo
        if (!leitor.ehEOF()) {
            char c = leitor.olhar();
            sb.append(c);
            leitor.lerCaractere();
        }

        // tenta ler a aspa simples de fechamento
        if (!leitor.ehEOF() && leitor.olhar() == '\'') {
            leitor.lerCaractere();
        }

        String lexema = sb.toString();
        return new Token(TipoToken.CHAR_CONST, lexema, linhaInicio, colunaInicio);
    }

    // operador ou símbolo

    private Token lerOperadorOuSimbolo(int linhaInicio, int colunaInicio) {

        char c = leitor.olhar();

        // vamos consumir pelo menos 1 caractere
        switch (c) {

            case '+':
                leitor.lerCaractere();
                return new Token(TipoToken.ADICAO, "+", linhaInicio, colunaInicio);

            case '-':
                leitor.lerCaractere();
                return new Token(TipoToken.SUBTRACAO, "-", linhaInicio, colunaInicio);

            case '*':
                leitor.lerCaractere();
                return new Token(TipoToken.MULTIPLICACAO, "*", linhaInicio, colunaInicio);

            case '%':
                leitor.lerCaractere();
                return new Token(TipoToken.RESTO, "%", linhaInicio, colunaInicio);

            case '/': {
                leitor.lerCaractere(); // consome '/'

                if (!leitor.ehEOF()) {
                    char prox = leitor.olhar();

                    // comentário de linha: //...
                    if (prox == '/') {
                        // consumir até o fim da linha
                        while (!leitor.ehEOF() && leitor.olhar() != '\n') {
                            leitor.lerCaractere();
                        }
                        // consumir o '\n' se quiser
                        if (!leitor.ehEOF()) {
                            leitor.lerCaractere();
                        }
                        // depois de pular o comentário, pegar o próximo token
                        return proximoToken();
                    }

                    // comentário de bloco: /* ... */
                    if (prox == '*') {
                        leitor.lerCaractere(); // consome '*'
                        char anterior = 0;
                        while (!leitor.ehEOF()) {
                            char atual = leitor.lerCaractere();
                            if (anterior == '*' && atual == '/') {
                                break;
                            }
                            anterior = atual;
                        }
                        // depois de pular o comentário, pegar o próximo token
                        return proximoToken();
                    }
                }

                // não era comentário → é operador divisão
                return new Token(TipoToken.DIVISAO, "/", linhaInicio, colunaInicio);
            }

            case '<': {
                leitor.lerCaractere();
                if (!leitor.ehEOF() && leitor.olhar() == '=') {
                    leitor.lerCaractere();
                    return new Token(TipoToken.MENOR_IGUAL, "<=", linhaInicio, colunaInicio);
                }
                return new Token(TipoToken.MENOR, "<", linhaInicio, colunaInicio);
            }

            case '>': {
                leitor.lerCaractere();
                if (!leitor.ehEOF() && leitor.olhar() == '=') {
                    leitor.lerCaractere();
                    return new Token(TipoToken.MAIOR_IGUAL, ">=", linhaInicio, colunaInicio);
                }
                return new Token(TipoToken.MAIOR, ">", linhaInicio, colunaInicio);
            }

            case '=': {
                leitor.lerCaractere();
                if (!leitor.ehEOF() && leitor.olhar() == '=') {
                    leitor.lerCaractere();
                    return new Token(TipoToken.IGUALDADE, "==", linhaInicio, colunaInicio);
                }
                // se a linguagem não tiver '=' sozinho, pode ser UNKNOWN
                return new Token(TipoToken.UNKNOWN, "=", linhaInicio, colunaInicio);
            }

            case '!': {
                leitor.lerCaractere();
                if (!leitor.ehEOF() && leitor.olhar() == '=') {
                    leitor.lerCaractere();
                    return new Token(TipoToken.DESIGUALDADE_EXCL, "!=", linhaInicio, colunaInicio);
                }
                return new Token(TipoToken.UNKNOWN, "!", linhaInicio, colunaInicio);
            }

            case '#':
                leitor.lerCaractere();
                return new Token(TipoToken.DESIGUALDADE_HASH, "#", linhaInicio, colunaInicio);

            case ':': {
                leitor.lerCaractere();
                if (!leitor.ehEOF() && leitor.olhar() == '=') {
                    leitor.lerCaractere();
                    return new Token(TipoToken.ATRIBUIR, ":=", linhaInicio, colunaInicio);
                }
                return new Token(TipoToken.DOIS_PONTOS, ":", linhaInicio, colunaInicio);
            }

            case ';':
                leitor.lerCaractere();
                return new Token(TipoToken.PONTO_VIRGULA, ";", linhaInicio, colunaInicio);

            case ',':
                leitor.lerCaractere();
                return new Token(TipoToken.VIRGULA, ",", linhaInicio, colunaInicio);

            case '(':
                leitor.lerCaractere();
                return new Token(TipoToken.PARENTESE_ESQ, "(", linhaInicio, colunaInicio);

            case ')':
                leitor.lerCaractere();
                return new Token(TipoToken.PARENTESE_DIR, ")", linhaInicio, colunaInicio);

            case '{':
                leitor.lerCaractere();
                return new Token(TipoToken.CHAVE_ESQ, "{", linhaInicio, colunaInicio);

            case '}':
                leitor.lerCaractere();
                return new Token(TipoToken.CHAVE_DIR, "}", linhaInicio, colunaInicio);

            case '[':
                leitor.lerCaractere();
                return new Token(TipoToken.COLCHETE_ESQ, "[", linhaInicio, colunaInicio);

            case ']':
                leitor.lerCaractere();
                return new Token(TipoToken.COLCHETE_DIR, "]", linhaInicio, colunaInicio);

            case '?':
                leitor.lerCaractere();
                return new Token(TipoToken.INTERROGACAO, "?", linhaInicio, colunaInicio);

            default:
                // caractere inválido: descarta e segue
                leitor.lerCaractere();
                return new Token(TipoToken.UNKNOWN, String.valueOf(c), linhaInicio, colunaInicio);
        }
    }
    
    public Token proximoToken() {

        // 1. pular espaços em branco
        pularEspacos();

        // 2. se chegou no fim do arquivo, devolve EOF
        if (leitor.ehEOF()) {
            return new Token(TipoToken.EOF, "EOF", leitor.getLinha(), leitor.getColuna());
        }

        char c = leitor.olhar();
        int linhaInicio = leitor.getLinha();
        int colunaInicio = leitor.getColuna();

        // 3. decidir o tipo de token pelo caractere inicial
        if (Character.isLetter(c)) {
            return lerIdentOuReservado(linhaInicio, colunaInicio);
        } else if (Character.isDigit(c)) {
            return lerNumero(linhaInicio, colunaInicio);
        } else if (c == '"') {
            return lerString(linhaInicio, colunaInicio);
        } else if (c == '\'') {
            return lerChar(linhaInicio, colunaInicio);
        } else {
            return lerOperadorOuSimbolo(linhaInicio, colunaInicio);
        }
    }
}

 