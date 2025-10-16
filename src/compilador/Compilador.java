package compilador;

import java.io.IOException;
import java.util.List; 

public class Compilador {

    public static void main(String[] args) throws IOException {
        lexico lexico = new lexico();
        
        if (!lexico.errorEncontrado) {
            sintactico sintactico = new sintactico(lexico.getCabeza());
            sintactico.analizar();
            
            System.out.println("\nProceso de Compilación Completado");

            List<String> notacionPolaca = sintactico.getPolaca();
            System.out.println("\n--- Notacion Polaca Inversa ---");
            for (String elemento : notacionPolaca) {
                System.out.print(elemento + "|");
            }
            System.out.println("\n-----------------------------");
        }
    }
}