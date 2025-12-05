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

    private void pularEspacos() {
        while (!leitor.ehEOF()) {
            char caractere = leitor.olhar();
            if (caractere == ' ' || caractere == '\t' || caractere == '\r' || caractere == '\n') {
                leitor.lerCaractere();
            } else {
                break;
            }
        }
    }

    private Token lerIdentOuReservado(int linhaInicio, int colunaInicio) {
        StringBuilder buffer = new StringBuilder();
        int caracteresLidos = 0;

        while (!leitor.ehEOF()) {
            char caractere = leitor.olhar();
            if (Character.isLetterOrDigit(caractere) || caractere == '_') {
                caracteresLidos++;
                if (buffer.length() < TAM_MAX_LEXEMA) {
                    buffer.append(Character.toUpperCase(caractere));
                }
                leitor.lerCaractere();
            } else {
                break;
            }
        }

        String lexema = buffer.toString();
        int tamanhoFinal = lexema.length();

        if (TabelaReservados.ehReservado(lexema)) {
            TipoToken tipo = TabelaReservados.buscarToken(lexema);
            return new Token(tipo, lexema, linhaInicio, colunaInicio);
        }

        tabelaSimbolos.registrarSimbolo(
                TipoToken.IDENTIFIER,
                lexema,
                caracteresLidos,
                tamanhoFinal,
                linhaInicio
        );

        return new Token(TipoToken.IDENTIFIER, lexema, linhaInicio, colunaInicio);
    }

    private Token lerNumero(int linhaInicio, int colunaInicio) {
        StringBuilder buffer = new StringBuilder();
        boolean possuiPonto = false;

        while (!leitor.ehEOF()) {
            char caractere = leitor.olhar();
            if (Character.isDigit(caractere)) {
                if (buffer.length() < TAM_MAX_LEXEMA) {
                    buffer.append(caractere);
                }
                leitor.lerCaractere();
            } else if (caractere == '.' && !possuiPonto) {
                possuiPonto = true;
                if (buffer.length() < TAM_MAX_LEXEMA) {
                    buffer.append(caractere);
                }
                leitor.lerCaractere();
            } else {
                break;
            }
        }

        String lexema = buffer.toString();

        if (possuiPonto) {
            return new Token(TipoToken.REAL_CONST, lexema, linhaInicio, colunaInicio);
        } else {
            return new Token(TipoToken.INT_CONST, lexema, linhaInicio, colunaInicio);
        }
    }

    private Token lerString(int linhaInicio, int colunaInicio) {
        StringBuilder buffer = new StringBuilder();
        leitor.lerCaractere();

        while (!leitor.ehEOF()) {
            char caractere = leitor.olhar();

            if (caractere == '"') {
                leitor.lerCaractere();
                break;
            }

            if (buffer.length() < TAM_MAX_LEXEMA) {
                buffer.append(caractere);
            }

            leitor.lerCaractere();
        }

        String lexema = buffer.toString();
        return new Token(TipoToken.STRING_CONST, lexema, linhaInicio, colunaInicio);
    }

    private Token lerChar(int linhaInicio, int colunaInicio) {
        StringBuilder buffer = new StringBuilder();
        leitor.lerCaractere();

        if (!leitor.ehEOF()) {
            char caractere = leitor.olhar();
            buffer.append(caractere);
            leitor.lerCaractere();
        }

        if (!leitor.ehEOF() && leitor.olhar() == '\'') {
            leitor.lerCaractere();
        }

        String lexema = buffer.toString();
        return new Token(TipoToken.CHAR_CONST, lexema, linhaInicio, colunaInicio);
    }

    private Token lerOperadorOuSimbolo(int linhaInicio, int colunaInicio) {
        char caractere = leitor.olhar();

        switch (caractere) {
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
                leitor.lerCaractere();

                if (!leitor.ehEOF()) {
                    char proximo = leitor.olhar();

                    if (proximo == '/') {
                        while (!leitor.ehEOF() && leitor.olhar() != '\n') {
                            leitor.lerCaractere();
                        }
                        if (!leitor.ehEOF()) {
                            leitor.lerCaractere();
                        }
                        return proximoToken();
                    }

                    if (proximo == '*') {
                        leitor.lerCaractere();
                        char anterior = 0;
                        while (!leitor.ehEOF()) {
                            char atual = leitor.lerCaractere();
                            if (anterior == '*' && atual == '/') {
                                break;
                            }
                            anterior = atual;
                        }
                        return proximoToken();
                    }
                }
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
                leitor.lerCaractere();
                return new Token(TipoToken.UNKNOWN, String.valueOf(caractere), linhaInicio, colunaInicio);
        }
    }

    public Token proximoToken() {
        pularEspacos();

        if (leitor.ehEOF()) {
            return new Token(TipoToken.EOF, "EOF", leitor.getLinha(), leitor.getColuna());
        }

        char caractere = leitor.olhar();
        int linhaInicio = leitor.getLinha();
        int colunaInicio = leitor.getColuna();

        if (Character.isLetter(caractere)) {
            return lerIdentOuReservado(linhaInicio, colunaInicio);
        } else if (Character.isDigit(caractere)) {
            return lerNumero(linhaInicio, colunaInicio);
        } else if (caractere == '"') {
            return lerString(linhaInicio, colunaInicio);
        } else if (caractere == '\'') {
            return lerChar(linhaInicio, colunaInicio);
        } else {
            return lerOperadorOuSimbolo(linhaInicio, colunaInicio);
        }
    }
}