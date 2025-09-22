package compilador;

public class nodoVar {
    String lexema;
    int tipo;
    nodoVar sig = null;

    nodoVar(String lexema, int tipo) {
        this.lexema = lexema;
        this.tipo = tipo;
    }
}
