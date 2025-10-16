package compilador;
  
import java.io.RandomAccessFile;
  
public class lexico {
    nodo cabeza = null, p;
    int estado = 0, columna = 0, valorMT, numRenglon = 1;
    int caracter = 0;
    String lexema = "";
    boolean errorEncontrado = false;
    boolean errorSintacticoEncontrado = false;
      
    String archivo = "C:\\Users\\Usuario\\Documents\\NetBeansProjects\\Compilador\\src\\compilador\\codigo.txt";
      
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
        {"se espera cierre de la cadena", "507"}
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
            } //fin del while
        
            // Procesar último token si el archivo terminó y el estado no es 0
            if (estado != 0 && !errorEncontrado) {
                insertarNodo();
            }
            
            if (!errorEncontrado) {
                System.out.println("Analisis Lexico Terminado Exitosamente");
                imprimirNodos();
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
    
    public nodo getCabeza() {
        return this.cabeza;
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
}