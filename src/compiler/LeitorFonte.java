package compiler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorFonte {

    private BufferedReader leitor;
    private int caracAtual;
    private boolean endOfFIle = false;

    private int linha = 1;
    private int coluna = 0;

    public LeitorFonte(String caminhoArquivo) {
        try {
            leitor = new BufferedReader(new FileReader(caminhoArquivo));
            avançar();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao abrir o arquivo: " + caminhoArquivo, e);
        }
    }

    private void avançar() {
        try {
        	caracAtual = leitor.read();

            if (caracAtual == -1) {
            	endOfFIle = true;
            } else {
                if (caracAtual == '\n') {
                    linha++;
                    coluna = 0;
                } else {
                    coluna++;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Erro na leitura do arquivo.", e);
        }
    }

    // apenas olha o caractere atual
    public char olhar() {
        return endOfFIle ? (char) -1 : (char) caracAtual;
    }

    // consome o caractere atual e avança para o próximo
    public char lerCaractere() {
        char c = olhar();
        avançar();
        return c;
    }

    public boolean ehEOF() {
        return endOfFIle;
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }
}
