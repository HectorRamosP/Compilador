package compilador;

import java.io.IOException;

public class Compilador {

    public static void main(String[] args) throws IOException {
        // Primero: análisis léxico
        lexico lexico = new lexico();
        
        // Solo si no hay errores léxicos, continuar con análisis sintáctico
        if (!lexico.errorEncontrado) {
            // Segundo: análisis sintáctico y semántico
            sintactico sintactico = new sintactico(lexico.getCabeza());
            sintactico.analizar();
            
            System.out.println("Proceso de Compilacion Completado");
        }
    }
}