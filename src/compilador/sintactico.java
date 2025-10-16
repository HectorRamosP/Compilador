package compilador;

import java.util.ArrayList; 
import java.util.List; 

public class sintactico {
    private nodo p;
    private boolean errorEncontrado = false;
    private boolean errorSintacticoEncontrado = false;
    private nodoVar cabezaVar = null;
    private List<String> polaca = new ArrayList<>();

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
      {"la variable ya fue declarada", "601"},
      {"Incompatibilidad de tipos", "602"},
      {"La condicion debe ser de tipo booleano", "603"}
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
    
    private void imprimirErrorSemantico(String mensaje, int renglon) {
        System.out.println("Error Semantico 602: Incompatibilidad de tipos. " + mensaje + ". Linea " + renglon);
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
    
    private String tipoAString(int tipoToken) {
        switch (tipoToken) {
            case MatrizTipos.T_INT: return "int";
            case MatrizTipos.T_FLOAT64: return "float64";
            case MatrizTipos.T_STRING: return "string";
            case MatrizTipos.T_BOOL: return "bool";
            default: return "desconocido";
        }
    }
    
    private int getTipoDeToken(nodo token) {
        if (token == null) return MatrizTipos.T_ERROR;
        switch (token.token) {
            case 100: // Identificador
                nodoVar v = buscarVariable(token.lexema);
                if (v != null) {
                    return v.tipo;
                }
                verificarVariableNoDeclarada(token.lexema, token.renglon);
                return MatrizTipos.T_ERROR;
            case 101: return MatrizTipos.T_INT;
            case 102: return MatrizTipos.T_FLOAT64;
            case 103: return MatrizTipos.T_STRING;
            case 213: case 214: // true, false
                return MatrizTipos.T_BOOL; 
            default:
                return MatrizTipos.T_ERROR;
        }
    }

    private void sintaxis() {
        if (p != null && p.token == 200) { p = p.sig;
            if (p != null && p.token == 201) { p = p.sig;
                if (p != null && p.token == 119) { p = p.sig;
                    if (p != null && p.token == 120) { p = p.sig;
                        if (p != null && p.token == 121) { p = p.sig;
                            declaracion_var();
                            instrucciones();
                            if (p != null && p.token == 122) { p = p.sig;
                                if (p != null) {
                                    imprimirErrorSintactico(p, 501);
                                }
                            } else {
                                imprimirErrorSintactico(p, 513);
                            }
                        } else {
                            imprimirErrorSintactico(p, 512);
                        }
                    } else {
                        imprimirErrorSintactico(p, 511);
                    }
                } else {
                    imprimirErrorSintactico(p, 510);
                }
            } else {
                imprimirErrorSintactico(p, 509);
            }
        } else {
            imprimirErrorSintactico(p, 508);
        }
    }
    
    private void declaracion_var() {
        while (p != null && p.token == 202) {
            p = p.sig;
            if (p != null && p.token == 100) {
                String nombreVar = p.lexema;
                int renglonVar = p.renglon;
                p = p.sig;
                if (p != null && (p.token >= 203 && p.token <= 206)) {
                    insertarVariable(nombreVar, p.token, renglonVar);
                    p = p.sig;
                } else {
                    imprimirErrorSintactico(p, 516);
                    break;
                }
            } else {
                imprimirErrorSintactico(p, 515);
                break;
            }
        }
    }
    
    private void instrucciones() {
        while (p != null && p.token != 122) {
            if (p.token == 100 || p.token == 207 || p.token == 209 || p.token == 210) {
                instruccionInicial(p.token);
            } else {
                imprimirErrorSintactico(p, 520);
                p = p.sig;
            }
        }
    }

    private void instruccionInicial(int token) {
        switch (token) {
            case 100: asignacion(); 
                break;
            case 210: instruccionFmt(); 
                break;
            case 207: if_else(); 
                break;
            case 209: while_loop(); 
                break;
            default:
                imprimirErrorSintactico(p, 520);
                p = p.sig;
                break;
        }
    }

   private void asignacion() {
    polaca.add(p.lexema); // Agrega la variable a la lista

    int tipoVar = getTipoDeToken(p);
    nodo varNode = p;
    p = p.sig;
    if (p != null && p.token == 127) { // '='
        nodo opAsignacion = p;
        p = p.sig;
        int tipoExpr = expresion(); 
        polaca.add(opAsignacion.lexema); // Agrega el operador de asignación

        if (tipoVar != MatrizTipos.T_ERROR && tipoExpr != MatrizTipos.T_ERROR) {
            int tipoFinal = MatrizTipos.verificar(opAsignacion.lexema, tipoVar, tipoExpr);
            if (tipoFinal == MatrizTipos.T_ERROR) {
                String detalle = "No se puede asignar una expresion de tipo '" + tipoAString(tipoExpr) + "' a la variable '" + varNode.lexema + "' de tipo '" + tipoAString(tipoVar) + "'";
                imprimirErrorSemantico(detalle, varNode.renglon);
            }
        }
    } else {
        imprimirErrorSintactico(p, 502);
    }
}

   private int expresion() {
    int tipoIzq = termino();
    while (p != null && (p.token == 104 || p.token == 105)) { // + -
        nodo operador = p;
        p = p.sig;
        int tipoDer = termino();
        polaca.add(operador.lexema); //Agrega el operador a la lista

        if (tipoIzq == MatrizTipos.T_ERROR || tipoDer == MatrizTipos.T_ERROR) return MatrizTipos.T_ERROR;
        
        int tipoResultante = MatrizTipos.verificar(operador.lexema, tipoIzq, tipoDer);
        if (tipoResultante == MatrizTipos.T_ERROR) {
            imprimirErrorSemantico("Operador '" + operador.lexema + "' no se puede aplicar a operandos de tipo '" + tipoAString(tipoIzq) + "' y '" + tipoAString(tipoDer) + "'", operador.renglon);
            return MatrizTipos.T_ERROR;
        }
        tipoIzq = tipoResultante;
    }
    return tipoIzq;
}

    private int termino() {
    int tipoIzq = factor();
    while (p != null && (p.token == 106 || p.token == 107)) { // "*" "/"
        nodo operador = p;
        p = p.sig;
        int tipoDer = factor();
        polaca.add(operador.lexema); // Añade el operador a la lista de polish

        if (tipoIzq == MatrizTipos.T_ERROR || tipoDer == MatrizTipos.T_ERROR) return MatrizTipos.T_ERROR;

        int tipoResultante = MatrizTipos.verificar(operador.lexema, tipoIzq, tipoDer);
        if (tipoResultante == MatrizTipos.T_ERROR) {
            imprimirErrorSemantico("Operador '" + operador.lexema + "' no se puede aplicar a operandos de tipo '" + tipoAString(tipoIzq) + "' y '" + tipoAString(tipoDer) + "'", operador.renglon);
            return MatrizTipos.T_ERROR;
        }
        tipoIzq = tipoResultante;
    }
    return tipoIzq;
}

    private int factor() {
    if (p != null) {
        // si es digito, cadena, identificador, true o false
        if ((p.token >= 100 && p.token <= 103) || p.token == 213 || p.token == 214) {
            int tipo = getTipoDeToken(p);
            polaca.add(p.lexema); // Añade el operador a la lista de polish
            p = p.sig;
            return tipo;
        } else if (p.token == 119) { // '('
            p = p.sig;
            int tipo = expresion();
            if (p != null && p.token == 120) { // ')'
                p = p.sig;
            } else {
                imprimirErrorSintactico(p, 511); // espera )
            }
            return tipo;
        } else {
            imprimirErrorSintactico(p, 522);
        }
    } else {
        imprimirErrorSintactico(null, 522);
    }
    return MatrizTipos.T_ERROR;
}

   private void if_else() {
    p = p.sig; // consume 'if'
    condicion(); // Procesa la condición

    polaca.add("BRF"); 

    if (p != null && p.token == 121) { // {
        p = p.sig;
        instrucciones(); // Cuerpo del 'if'
        if (p != null && p.token == 122) { // }
            p = p.sig;

            if (p != null && p.token == 208) { // Si existe un 'else'
                p = p.sig;
                polaca.add("BRI"); 

                if (p != null && p.token == 121) { // { del else
                    p = p.sig;
                    instrucciones(); // Cuerpo del 'else'
                    if (p != null && p.token == 122) { // } del else
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
    p = p.sig; // consume 'while'

    // 1. Evaluar la condición
    condicion();

   
    polaca.add("BRF");

    if (p != null && p.token == 121) { // {
        p = p.sig;
        instrucciones(); // 3. Ejecutar el cuerpo del bucle
        if (p != null && p.token == 122) { // }
            p = p.sig;

            polaca.add("BRI");

        } else {
            imprimirErrorSintactico(p, 513);
        }
    } else {
        imprimirErrorSintactico(p, 512);
    }
}

    private void condicion() {
        int tipoCond = expresion_logica();
        if (tipoCond != MatrizTipos.T_BOOL && tipoCond != MatrizTipos.T_ERROR) {
            System.out.println("Error Semantico 603: La condicion debe ser de tipo booleano, pero se recibio '" + tipoAString(tipoCond) + "'. Linea " + (p != null ? p.renglon : 0));
            errorSintacticoEncontrado = true;
        }
    }
    
   private int expresion_logica() {
    int tipoIzq = expresion_logica_simple();
    while (p != null && (p.token == 114 || p.token == 115)) { // &&, ||
        nodo operador = p;
        p = p.sig;
        int tipoDer = expresion_logica_simple();
        
        polaca.add(operador.lexema); // Agrega el operador lógico

        if (tipoIzq == MatrizTipos.T_ERROR || tipoDer == MatrizTipos.T_ERROR) return MatrizTipos.T_ERROR;

        int tipoResultante = MatrizTipos.verificar(operador.lexema, tipoIzq, tipoDer);
        if (tipoResultante == MatrizTipos.T_ERROR) {
            imprimirErrorSemantico("Operador '" + operador.lexema + "' no se puede aplicar a operandos de tipo '" + tipoAString(tipoIzq) + "' y '" + tipoAString(tipoDer) + "'", operador.renglon);
            return MatrizTipos.T_ERROR;
        }
        tipoIzq = tipoResultante;
    }
    return tipoIzq;
}

    private int expresion_logica_simple() {
    if (p != null && p.token == 116) { // '!'
        nodo operador = p;
        p = p.sig;
        int tipoExpr = expresion_logica_simple(); // Procesa el operando primero
        
        polaca.add(operador.lexema); //Agrega el '!' después del operando

        if (tipoExpr == MatrizTipos.T_ERROR) return MatrizTipos.T_ERROR;

        int tipoResultante = MatrizTipos.verificar(operador.lexema, tipoExpr);
        if(tipoResultante == MatrizTipos.T_ERROR){
            imprimirErrorSemantico("Operador '!' solo se puede aplicar a operandos de tipo 'bool'", operador.renglon);
            return MatrizTipos.T_ERROR;
        }
        return tipoResultante;
    } 
    else if (p != null && p.token == 119) { // '('
        p = p.sig;
        int tipo = expresion_logica();
        if (p != null && p.token == 120) { // ')'
            p = p.sig;
        } else {
            imprimirErrorSintactico(p, 511);
        }
        return tipo;
    } else {
        return expresion_comparacion();
    }
}
    
   private int expresion_comparacion() {
    int tipoIzq = expresion();
    if (p != null && p.token >= 108 && p.token <= 113) {
        nodo operador = p;
        p = p.sig;
        int tipoDer = expresion();
        
        polaca.add(operador.lexema); // Agrega el operador de comparación

        if (tipoIzq == MatrizTipos.T_ERROR || tipoDer == MatrizTipos.T_ERROR) return MatrizTipos.T_ERROR;
        
        int tipoResultante = MatrizTipos.verificar(operador.lexema, tipoIzq, tipoDer);
         if (tipoResultante == MatrizTipos.T_ERROR) {
            imprimirErrorSemantico("No se puede comparar '" + tipoAString(tipoIzq) + "' con '" + tipoAString(tipoDer) + "' usando el operador '"+ operador.lexema +"'", operador.renglon);
            return MatrizTipos.T_ERROR;
         }
         return tipoResultante;
    }
    return tipoIzq;
}

    private void instruccionFmt() {
        p = p.sig;
        if (p != null && p.token == 126) {
            p = p.sig;
            if (p != null) {
                if (p.token == 211) {
                    lectura();
                } else if (p.token == 212) {
                    escritura();
                } else {
                    imprimirErrorSintactico(p, 518);
                }
            } else {
                imprimirErrorSintactico(p, 518);
            }
        } else {
            imprimirErrorSintactico(p, 517);
        }
    }

    private void lectura() {
        p = p.sig;
        if (p != null && p.token == 119) {
            p = p.sig;
            if (p != null && p.token == 128) {
                p = p.sig;
                if (p != null && p.token == 100) {
                    verificarVariableNoDeclarada(p.lexema, p.renglon);
                    p = p.sig;
                    if (p != null && p.token == 120) {
                        p = p.sig;
                    } else {
                        imprimirErrorSintactico(p, 511);
                    }
                } else {
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
        p = p.sig; // Consume 'Println'
        if (p != null && p.token == 119) { // (
            p = p.sig;
            if (p != null && p.token != 120) { // Mientras no sea )
                // Este bucle procesa múltiples argumentos para Println, como fmt.Println("Hola", variable)
                while (true) {
                    expresion(); // Procesa el argumento y lo añade a la pila polaca
                    if (p != null && p.token == 123) { // Si hay una coma, viene otro argumento
                        p = p.sig;
                    } else {
                        break; // No hay más argumentos
                    }
                }
            }
            if (p != null && p.token == 120) { // )
                p = p.sig;
                polaca.add("Println"); 
            } else {
                imprimirErrorSintactico(p, 511); // Error: se esperaba ')'
            }
        } else {
            imprimirErrorSintactico(p, 510); // Error: se esperaba '('
        }
    }
    
    public List<String> getPolaca() {
    return this.polaca;
}
}

