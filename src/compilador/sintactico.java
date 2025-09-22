package compilador;

public class sintactico {
    private nodo p;
    private boolean errorEncontrado = false;
    private boolean errorSintacticoEncontrado = false;
    private nodoVar cabezaVar = null;

    private String[][] errores = {
      {"se espera digito", "500"},
      {"eof inesperado", "501"},
      {"se espera '='", "502"},
      {"se espera '&'", "503"},
      {"se espera '|'", "504"},
      {"eol inesperado", "505"},
      {"simbolo no valido", "506"},
      {"se espera cierre de la cadena", "507"},
      {"se espera 'func'", "508"},
      {"se espera 'main'", "509"},
      {"se espera '('", "510"},
      {"se espera ')'", "511"},
      {"se espera '{'", "512"},
      {"se espera '}'", "513"},
      {"se espera ';'", "514"},
      {"se espera identificador", "515"},
      {"se espera tipo (int, float64, string, bool)", "516"},
      {"se espera '.'", "517"},
      {"se espera 'Scanln' o 'Println'", "518"},
      {"se espera una instruccion", "520"},
      {"se espera una declaracion de variable", "521"},
      {"se espera expresion", "522"},
      {"se espera if", "523"},
      {"se espera operador relacional", "524"},
      {"variable no declarada", "600"},
      {"la variable ya fue declarada", "601"}
    };

    public sintactico(nodo cabeza) {
        this.p = cabeza;
    }

    public void analizar() {
        sintaxis();
        if (!errorSintacticoEncontrado) {
            System.out.println("Analisis Sintactico y Semantico Terminado Exitosamente");
        } else {
            System.out.println("Analisis Sintactico y Semantico Terminado con Errores");
        }
    }

    private void imprimirErrorSintactico(nodo tokenActual, int codigoError) {
        if (tokenActual == null) {
            System.out.println("Error " + codigoError + ": token nulo (posiblemente EOF inesperado)");
        } else {
            for (String[] errore : errores) {
                if (codigoError == Integer.parseInt(errore[1])) {
                    System.out.println("Error " + codigoError + ": " + errore[0] + 
                     " en el renglon " + tokenActual.renglon);
                    break;
                }
            }
        }
        errorEncontrado = true;
        errorSintacticoEncontrado = true;
    }
    
    private void insertarVariable(String nombre, int tipoToken, int renglon) {
        if (buscarVariable(nombre) != null) {
            System.out.println("Error Semantico 601: la variable '" + nombre + "' ya fue declarada. Linea " + renglon);
            errorSintacticoEncontrado = true;
            return;
        }
        nodoVar nuevaVar = new nodoVar(nombre, tipoToken);
        nuevaVar.sig = cabezaVar;
        cabezaVar = nuevaVar;
    }

    private nodoVar buscarVariable(String nombre) {
        nodoVar actual = cabezaVar;
        while (actual != null) {
            if (actual.lexema.equals(nombre)) {
                return actual;
            }
            actual = actual.sig;
        }
        return null;
    }
    
    private void verificarVariableNoDeclarada(String nombre, int renglon) {
        if (buscarVariable(nombre) == null) {
            System.out.println("Error Semantico 600: la variable '" + nombre + "' no ha sido declarada. Linea " + renglon);
            errorSintacticoEncontrado = true;
        }
    }
    
    private void sintaxis() {
        if (p != null && p.token == 200) { // func
            p = p.sig;
            if (p != null && p.token == 201) { // main
                p = p.sig;
                if (p != null && p.token == 119) { // (
                    p = p.sig;
                    if (p != null && p.token == 120) { // )
                        p = p.sig;
                        if (p != null && p.token == 121) { // {
                            p = p.sig;
                            if (p != null) declaracion_var();
                            if (p != null) instrucciones();
                            if (p != null && p.token == 122) { // }
                                p = p.sig;
                                if (p != null) {
                                    imprimirErrorSintactico(p, 501); // EOF inesperado
                                }
                            } else {
                                imprimirErrorSintactico(p, 513); // se espera '}'
                            }
                        } else {
                            imprimirErrorSintactico(p, 512); // se espera '{'
                        }
                    } else {
                        imprimirErrorSintactico(p, 511); // se espera ')'
                    }
                } else {
                    imprimirErrorSintactico(p, 510); // se espera '('
                }
            } else {
                imprimirErrorSintactico(p, 509); // se espera 'main'
            }
        } else {
            imprimirErrorSintactico(p, 508); // se espera 'func'
        }
    }

    private boolean tipo() {
        if (p != null && (p.token == 203 || p.token == 204 || p.token == 205 || p.token == 206)) {
            p = p.sig;
            return true;
        }
        return false;
    }

    private void declaracion_var() {
        while (p != null && p.token == 202) {  // 'var'
            p = p.sig;
            if (p != null && p.token == 100) {  
                String nombreVar = p.lexema; 
                int renglonVar = p.renglon;
                p = p.sig;
                if (p != null) {
                    int tipoToken = p.token;
                    if (tipo()) {
                        insertarVariable(nombreVar, tipoToken, renglonVar);
                    } else {
                        imprimirErrorSintactico(p, 516);  
                        break;
                    }
                }
                else {
                    imprimirErrorSintactico(null, 516);
                    break;
                }
            } else {
                imprimirErrorSintactico(p, 515); 
                break;
            }
        }
    }
            
    private void instrucciones() {
        if (p != null && p.token == 122) {
           imprimirErrorSintactico(p, 520);
           return;
        }
        
        while (p != null && p.token != 122) {  // Mientras no sea '}'
            if (p.token == 100 || p.token == 207 || 
                p.token == 209 || p.token == 210) {
                instruccionInicial(p.token);
            } else {
                imprimirErrorSintactico(p, 520);
                // Avanzamos para evitar un bucle infinito si hay un token inesperado
                p = p.sig; 
            }
        }
    }

    private void instruccionInicial(int token) {
        switch (token) {
            case 100:  // Identificador (asignacion)
                asignacion();
                break;
            case 210:  // fmt
                instruccionFmt();
                break;
            case 207:  // if
                if_else();
                break;
            case 209:  // for (while)
                while_loop();
                break;
            default:
                imprimirErrorSintactico(p, 520); 
                p = p.sig;
                break;
        }   
    }

    private void asignacion() {
        if (p != null && p.token == 100){
            String nombreVar = p.lexema;
            if (buscarVariable(nombreVar) == null) {
                System.out.println("Error Semantico 600: la variable '" + nombreVar + "' no ha sido declarada. Linea " + p.renglon);
                errorSintacticoEncontrado = true;
            }
            p = p.sig;
            if(p != null && p.token == 127){
                p = p.sig;
                expresion();
            }else{
                imprimirErrorSintactico(p, 502);
            }    
        }else{
            imprimirErrorSintactico(p, 515);
        }
    }

    private void expresion() {
        termino();
        while(p != null && (p.token >= 104 && p.token <= 107)){
            p = p.sig;
            termino();
        }
    }
    
    private void termino() {
        factor();
        while (p != null && (p.token == 106 || p.token == 107)) { // * /
            p = p.sig;
            factor();
        }
    }

    private void factor() {
        if (p != null) {
            if (p.token == 100) { // Identificador
                if (buscarVariable(p.lexema) == null) {
                    System.out.println("Error Semantico 600: la variable '" + p.lexema + "' no ha sido declarada. Linea " + p.renglon);
                    errorSintacticoEncontrado = true;
                }
                p = p.sig;
            } else if (p.token == 101 || p.token == 102 || p.token == 103 || p.token == 213 || p.token == 214) { // Numero, cadena, true/false
                p = p.sig;
            } else if (p.token == 119) { // '('
                p = p.sig;
                expresion();
                if (p != null && p.token == 120) { // ')'
                    p = p.sig;
                } else {
                    imprimirErrorSintactico(p, 511);
                }
            } else {
                imprimirErrorSintactico(p, 522);
            }
        } else {
            imprimirErrorSintactico(null, 522);
        }
    }
    
    private void instruccionFmt() {
        p = p.sig;
        if (p != null && p.token == 126) { //.
            p = p.sig;
            if(p != null){
                if(p.token == 211){
                    lectura();
                }else if(p.token == 212){
                    escritura();
                }else{    
                    imprimirErrorSintactico(p, 518);
                }
            }else{
                imprimirErrorSintactico(p, 518);
            }
        }else{
            imprimirErrorSintactico(p, 517);
        }
    }

    private void lectura() {
        p = p.sig;
        if (p != null && p.token == 119) { // (
            p = p.sig;
            if (p != null && p.token == 128) { // &
                p = p.sig;
                if (p != null && p.token == 100 ) {  // identificador
                    if (buscarVariable(p.lexema) == null) {
                        System.out.println("Error Semantico 600: la variable '" + p.lexema + "' no ha sido declarada. Linea " + p.renglon);
                        errorSintacticoEncontrado = true;
                    }
                    p = p.sig;
                    if (p != null && p.token == 120){ // )
                        p = p.sig;
                    }else{
                        imprimirErrorSintactico(p, 511); 
                    }
                }else{
                    imprimirErrorSintactico(p, 515); 
                }
            } else {
                imprimirErrorSintactico(p, 503);
            }
        } else {
            imprimirErrorSintactico(p, 510);
        }
    }

    private void escritura() {
        p = p.sig;
        if (p != null && p.token == 119) {  // '('
            p = p.sig;
            if (p != null && p.token != 120) {  
                expresion();
                while (p != null && p.token == 123) {  // ','
                    p = p.sig;
                    expresion();
               }
            }
            if (p != null && p.token == 120) {  // ')'
                p = p.sig;
            } else {
                imprimirErrorSintactico(p, 511);
            }
        } else {
            imprimirErrorSintactico(p, 510);
        }
    }

    private void if_else() {
        p = p.sig;  
        condicion();
        
        if (p != null && p.token == 121) { // '{'
            p = p.sig;
            instrucciones();
            
            if (p != null && p.token == 122) { // '}'
                p = p.sig;
                
                if (p != null && p.token == 208) { // else
                    p = p.sig;
                    if (p != null && p.token == 121) { // '{'
                        p = p.sig;
                        instrucciones();
                        if (p != null && p.token == 122) { // '}'
                            p = p.sig;
                        } else {
                            imprimirErrorSintactico(p, 513); 
                        }
                    } else {
                        imprimirErrorSintactico(p, 512); 
                    }
                }
            } else {
                imprimirErrorSintactico(p, 513);
            }
        } else {
            imprimirErrorSintactico(p, 512); 
        }
    }

    private void while_loop() {
        p = p.sig; 
        condicion();
        if (p != null && p.token == 121) { // '{'
            p = p.sig;
            instrucciones();
            if (p != null && p.token == 122) { // '}'
                p = p.sig;
            } else {
                imprimirErrorSintactico(p, 513);  // '}'
            }
        } else {
            imprimirErrorSintactico(p, 512);  // '{'
        }
    }

    private void condicion() {
        expresion_logica();
    }

    private void expresion_logica() {
        expresion_logica_simple();
        while (p != null && (p.token == 114 || p.token == 115)) {
            p = p.sig;
            expresion_logica_simple();
        }
    }

    private void expresion_logica_simple() {
        if (p != null && p.token == 116) { // '!'
            p = p.sig;
            expresion_logica_simple();
        } else if (p != null && p.token == 119) { // '('
            p = p.sig;
            expresion_logica();
            if (p != null && p.token == 120) { // ')'
                p = p.sig;
            } else {
                imprimirErrorSintactico(p, 511);
            }
        } else {
            if (esInicioDeComparacion()) {
                expresion_comparacion();
            } else if (p != null && (p.token == 100 || p.token == 213 || p.token == 214)) {
                if (p.token == 100 && buscarVariable(p.lexema) == null) {
                     System.out.println("Error Semantico 600: la variable '" + p.lexema + "' no ha sido declarada. Linea " + p.renglon);
                     errorSintacticoEncontrado = true;
                }
                p = p.sig; 
            } else {
                imprimirErrorSintactico(p, 522);
            }
        }
    }

    private boolean esInicioDeComparacion() {
        return (p != null && (p.token == 100 || p.token == 101 || p.token == 102 || 
            p.token == 119 || p.token == 213 || p.token == 214));
    }

    private void expresion_comparacion() {
        expresion_aritmetica();
        if (p != null && p.token >= 108 && p.token <= 113) {  // Operadores relacionales
            p = p.sig;
            expresion_aritmetica();
        }
    }
           
    private void expresion_aritmetica() {
        termino();
        while (p != null && (p.token == 104 || p.token == 105)) { // + -
            p = p.sig;
            termino();
        }
    }
}