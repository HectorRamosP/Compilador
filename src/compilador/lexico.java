package compilador;
  
  import java.io.RandomAccessFile;
  import java.util.ArrayList;
  
  public class lexico {
      nodo cabeza = null, p;
      int estado = 0, columna = 0, valorMT, numRenglon = 1;
      int caracter = 0;
      String lexema = "";
      boolean errorEncontrado = false;
      boolean errorSintacticoEncontrado = false;
      ArrayList<String> variablesDeclaradas = new ArrayList<>();
      
      String archivo = "C:\\Users\\Hector\\Documents\\NetBeansProjects\\Compilador\\src\\compilador\\codigo.txt";
      
      int[][] matriz = {
          //          l      d      "      +      -      *      /      =      !      <      >      &      |      [      ]      (      )      {      }      ,      ;      :      .     eb     rt    nl     tab    eof    oc
          //          0      1      2      3      4      5      6      7      8      9     10     11     12     13     14     15     16     17     18     19     20     21     22     23     24     25     26     27     28
          /*0*/   {   1,     2,     5,   104,   105,   106,     6,    10,    11,    12,    13,    14,    15,   117,   118,   119,   120,   121,   122,   123,   124,   125,   126,     0,     0,     0,     0,     0,   506 },
          /*1*/   {   1,     1,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100,   100 },
          /*2*/   { 101,     2,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,   101,     3,   101,   101,   101,   101,   101,   101 },
          /*3*/   { 500,     4,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500,   500 },
          /*4*/   { 102,     4,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102,   102 },
          /*5*/   {   5,     5,   103,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,     5,   505,     5,     5,     5,     5 },
          /*6*/   { 107,   107,   107,   107,   107,     7,     8,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107,   107 },
          /*7*/   {   7,     7,     7,     7,     7,     9,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,   501,     7 },
          /*8*/   {   0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0,     0 },
          /*9*/   {   7,     7,     7,     7,     7,     7,     0,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7,     7 },
          /*10*/  { 127,   127,   127,   127,   127,   127,   127,   108,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127,   127 },
          /*11*/  { 116,   116,   116,   116,   116,   116,   116,   109,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116,   116 },
          /*12*/  { 112,   112,   112,   112,   112,   112,   112,   110,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112,   112 },
          /*13*/  { 113,   113,   113,   113,   113,   113,   113,   111,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113,   113 },
          /*14*/  { 128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   114,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128,   128 },
          /*15*/  { 504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   115,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504,   504 }
      };
  
      String[][] palabrasReservadas = {
          {"func", "200"},
          {"main", "201"},
          {"var", "202"},
          {"int", "203"},
          {"float64", "204"},
          {"string", "205"},
          {"bool", "206"},
          {"if", "207"},
          {"else", "208"},
          {"for", "209"},
          {"fmt", "210"},
          {"Scanln", "211"},
          {"Println", "212"},
          {"true", "213"},
          {"false", "214"},
          
      };
      
        String[][] errores = {
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
          {"se espera operador relacional", "524"}
        };
  
      RandomAccessFile file = null;
  
      public lexico() {
    try {
        file = new RandomAccessFile(archivo, "r");
        while ((caracter = file.read()) != -1) {
            if (Character.isLetter((char) caracter)) columna = 0;
            else if (Character.isDigit((char) caracter)) columna = 1;
            else switch ((char) caracter) {
                case '"': columna = 2; break;
                case '+': columna = 3; break;
                case '-': columna = 4; break;
                case '*': columna = 5; break;
                case '/': columna = 6; break;
                case '=': columna = 7; break;
                case '!': columna = 8; break;
                case '<': columna = 9; break;
                case '>': columna = 10; break;
                case '&': columna = 11; break;
                case '|': columna = 12; break;
                case '[': columna = 13; break;
                case ']': columna = 14; break;
                case '(': columna = 15; break;
                case ')': columna = 16; break;
                case '{': columna = 17; break;
                case '}': columna = 18; break;
                case ',': columna = 19; break;
                case ';': columna = 20; break;
                case ':': columna = 21; break;
                case '.': columna = 22; break;
                case ' ': columna = 23; break;// eb (espacio en blanco)
                case 13 : columna = 24; break; // rt (retorno de carro)
                case 10 : {columna = 25; // nl (nueva línea)
                      numRenglon = numRenglon + 1;
                  }
                  break;
                case 9 : columna = 26; break; // tab (tabulador horizontal)
                default: columna = 28; break; // oc (otra cosa)  
            }
            // Cálculo de valorMT
            valorMT = matriz[estado][columna];

            if (valorMT < 100) {
                estado = valorMT;
                if(estado == 0){
                    lexema = "";
                } else {
                    lexema = lexema + (char) caracter;
                }
            } else if (valorMT >= 100 && valorMT < 500) {
                if (valorMT == 100){
                    validarSiEsPalabraReservada();
                }
                if (valorMT == 100 || valorMT == 101 || valorMT == 102 ||
                    valorMT == 107 || valorMT == 112 || valorMT == 113 ||
                    valorMT == 116 || valorMT == 127 || valorMT == 128 || valorMT >= 200) {
                    file.seek(file.getFilePointer() - 1);
                } else {
                    lexema = lexema + (char) caracter;
                }
                insertarNodo();
                estado = 0;
                lexema = "";
            } else {
                imprimirMensajeError();
                break;
            }
        } //while
        
        // Procesar último token si el archivo terminó y el estado no es 0
        if (estado != 0 && !errorEncontrado) {
            if (estado == 5) { // Cadena sin cerrar
                imprimirErrorSintactico(p, 507); // Error 507: se espera cierre de la cadena
            } else {
                insertarNodo();
            }
        }
        
        if (!errorEncontrado) {
            System.out.println("Analisis Lexico Terminado Exitosamente");
            imprimirNodos();
            sintaxis();
            if (!errorSintacticoEncontrado) {
                System.out.println("Analisis Sintactico Terminado Exitosamente");
            } else {
                System.out.println("Analisis Sintactico Terminado con Errores");
            }
        } else {
            System.out.println("Analisis Lexico Terminado con Errores");
        }
    } catch (Exception e) {
        System.out.println("Error inesperado: " + e.getMessage());
    } finally {
        try {
            if (file != null) {
                file.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
      
      private void imprimirNodos() {
        p = cabeza;
        while (p != null) {
            System.out.println(p.lexema + " " + p.token + " " + p.renglon);
            p = p.sig;
        }
    }
    
    private void validarSiEsPalabraReservada() {
        for (String[] palabrasReservada : palabrasReservadas) {
            if (lexema.equals(palabrasReservada[0])) {
                valorMT = Integer.parseInt(palabrasReservada[1]);
            }
        }
    }

    private void imprimirMensajeError() {
        if(caracter != -1 && valorMT >= 500) {
            for (String[] errore : errores){
                if (valorMT == Integer.valueOf(errore[1])) {
                    System.out.println("El error encontrado es: " + errore[0] + " error " + valorMT + " caracter " + caracter + " en el renglon " + numRenglon);
                }
            }
            errorEncontrado = true;   
        }
    }
    
   private void imprimirErrorSintactico(nodo tokenActual, int codigoError) {
    if (tokenActual == null) {
        System.out.println("Error " + codigoError + ": token nulo (posiblemente EOF inesperado)");
        errorEncontrado = true;
        return;
    }
    for (String[] errore : errores) {
        if (codigoError == Integer.parseInt(errore[1])) {
            System.out.println("Error " + codigoError + ": " + errore[0] + 
             " en el renglon " + tokenActual.renglon);
            break;
        }
    }
    errorEncontrado = true;
    errorSintacticoEncontrado = true;

}

    
    private void insertarNodo() {
        nodo nodo = new nodo(lexema, valorMT, numRenglon);
        if (cabeza == null) {
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }
    }

    private void sintaxis() {
    p = cabeza;
    while (p != null && !errorEncontrado) {
        if (p.token == 200) { // func
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
        break;
    }
}
    

    private boolean tipo() {
        if (p.token == 203 || p.token == 204 || p.token == 205 || p.token == 206) {
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
            p = p.sig;
            if (p != null && tipo()) {
             
                if (variablesDeclaradas.contains(nombreVar)) {
                    System.out.println("Error: la variable '" + nombreVar + 
                                       "' ya fue declarada en el programa. Linea " + p.renglon);
                    errorSintacticoEncontrado = true;
                    errorEncontrado = true;
     
                } else {
                    variablesDeclaradas.add(nombreVar);
                }
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
        if (p.token == 122) {
           imprimirErrorSintactico(p, 520);
           return;
        }
        
        while (p != null && p.token != 122) {  // Mientras no sea '}'
            if (p.token == 100 || p.token == 207 || 
                p.token == 209 || p.token == 210) {
                instruccionInicial(p.token);
            } else {
            break;
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
        if (p.token == 100){
            p = p.sig;
            if(p.token == 127){
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
        terminoSimple();
        if(p.token >= 104 && p.token <= 107){
            p = p.sig;
            expresion();
        }
    }

    private void terminoSimple() {
        if ((p.token >= 100 && p.token <= 103) || 
            p.token == 213 || p.token == 214) { 
            p = p.sig;
        } else {
        imprimirErrorSintactico(p, 522);
        }
    }    
    
    private void instruccionFmt() {
        p = p.sig;
        if (p.token == 126) { //.
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
        if (p.token == 119) { // (
            p = p.sig;
            if (p.token == 128) { // &
                p = p.sig;
                if (p.token == 100 ) {  // identificador
                    p = p.sig;
                    if (p.token == 120){ // )
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
        if (p.token == 119) {  // '('
            p = p.sig;
            if (p.token != 120) {  
                expresion();
                while (p.token == 123) {  // ','
                    p = p.sig;
                    expresion();
               }
            }
            if (p.token == 120) {  // ')'
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
    
    // Verificar llave de apertura
    if (p != null && p.token == 121) { // '{'
        p = p.sig;
        instrucciones();
        
        // Verificar llave de cierre
        if (p != null && p.token == 122) { // '}'
            p = p.sig;
            
            // Manejar 'else' si existe
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
        if (p.token == 121) { // '{'
            p = p.sig;
            instrucciones();
            if (p.token == 122) { // '}'
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
        while (p.token == 114 || p.token == 115) {
            p = p.sig;
            expresion_logica_simple();
        }
    }

    private void expresion_logica_simple() {
        if (p.token == 116) { // '!'
            p = p.sig;
            expresion_logica_simple();
        } else if (p.token == 119) { // '('
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
            } else if (p.token == 100 || p.token == 213 || p.token == 214) {
             p = p.sig; 
            } else {
            imprimirErrorSintactico(p, 522);
            }
        }
    }

    private boolean esInicioDeComparacion() {
        return (p.token == 100 || p.token == 101 || p.token == 102 || 
            p.token == 119 || p.token == 213 || p.token == 214);
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
    
    private void termino() {
        factor();
        while (p != null && (p.token == 106 || p.token == 107)) { // * /
            p = p.sig;
            factor();
        }
    }
   
    private void factor() {
        if (p.token == 100 || p.token == 101 || p.token == 102 ||  // Ident, número, cadena
            p.token == 213 || p.token == 214) { // true/false
            p = p.sig;
        } else if (p.token == 119) { // '('
            p = p.sig;
            expresion_aritmetica();
            if (p != null && p.token == 120) { // ')'
                p = p.sig;
            } else {
            imprimirErrorSintactico(p, 511); // Error 511: se espera ')'
            }
        } else {
        imprimirErrorSintactico(p, 522); // Error 522: se espera expresión
        }
    }
}
