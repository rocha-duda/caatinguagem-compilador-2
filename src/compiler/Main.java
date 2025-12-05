package compiler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import compiler.symbols.TabelaSimbolos;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Erro: Informe o nome do arquivo (sem extensao) como argumento.");
            System.out.println("Exemplo: java Main Teste01");
            return;
        }

        String nomeBase = args[0];
        String arquivoEntrada = nomeBase + ".252";
        String arquivoLexico = nomeBase + ".LEX";
        String arquivoTabela = nomeBase + ".TAB";

        System.out.println("Iniciando compilacao de: " + arquivoEntrada);

        try {
            TabelaSimbolos tabelaSimbolos = new TabelaSimbolos();
            
            LeitorFonte leitor = new LeitorFonte(arquivoEntrada);
            AnalisadorLexico lexico = new AnalisadorLexico(leitor, tabelaSimbolos);

            List<Token> tokensEncontrados = new ArrayList<>();
            Token tokenAtual = null;

            do {
                tokenAtual = lexico.proximoToken();

                if (tokenAtual != null) {
                    tokensEncontrados.add(tokenAtual);
                    System.out.println("Token lido: " + tokenAtual.getLexema()); 
                }

            } while (tokenAtual != null && tokenAtual.getTipo() != TipoToken.EOF); 

            gerarRelatorioLexico(arquivoLexico, tokensEncontrados);
            gerarRelatorioTabela(arquivoTabela, tabelaSimbolos);

            System.out.println("Sucesso! Relatorios gerados: " + arquivoLexico + " e " + arquivoTabela);

        } catch (Exception e) {
            System.err.println("Erro fatal durante a compilacao:");
            e.printStackTrace();
        }
    }

    private static void gerarRelatorioLexico(String nomeArquivo, List<Token> tokens) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("Código da Equipe: [E03]");
            writer.println("Componentes: Maria Lucas Caio Pedro");
            writer.println("Seu Nome; maria.r@aln.senaicimatec.edu.br; (71) 99286-7863");
            writer.println("RELATÓRIO DA ANÁLISE LÉXICA. Texto fonte analisado: " + nomeArquivo.replace(".LEX", ".252"));
            
            for (Token t : tokens) {
                writer.printf("Lexeme: %s, Código: %s, ÍndiceTabSimb: %s, Linha: %d.\n",
                        t.getLexema(), 
                        t.getTipo(), 
                        "placeholder",
                        t.getLinha());
            }
        } catch (IOException e) {
            System.err.println("Erro ao escrever relatório léxico: " + e.getMessage());
        }
    }

    private static void gerarRelatorioTabela(String nomeArquivo, TabelaSimbolos tabela) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            writer.println("RELATÓRIO DA TABELA DE SÍMBOLOS.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever relatório de símbolos: " + e.getMessage());
        }
    }
}