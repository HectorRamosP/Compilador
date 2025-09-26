package compilador;

import java.io.IOException;

public class Compilador {

    public static void main(String[] args) throws IOException {
        lexico lexico = new lexico();
        if (!lexico.errorEncontrado) {
            sintactico sintactico = new sintactico(lexico.getCabeza());
            sintactico.analizar();
            
            System.out.println("Proceso de Compilacion Completado");
        }
    }
}