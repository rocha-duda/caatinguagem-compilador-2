package compiler;

public class Escopo {

    private int nivelAtual;

    public Escopo() {
        this.nivelAtual = 0; 
    }

    public void entrarBloco() {
        nivelAtual++;
    }

    public void sairBloco() {
        if (nivelAtual == 0) {
            throw new RuntimeException("Tentativa de sair do escopo global.");
        }
        nivelAtual--;
    }

    public int getNivelAtual() {
        return nivelAtual;
    }
}

