package compilador;

import java.io.IOException;

public class Compilador {

    nodo p;
//hola
    public static void main(String[] args) throws IOException {
        lexico lexico = new lexico();
        if (!lexico.errorEncontrado) {
            System.out.println("Proceso de Compilacion Completado");
        }
    }
}
