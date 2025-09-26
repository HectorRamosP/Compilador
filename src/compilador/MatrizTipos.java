package compilador;

public class MatrizTipos {
    public static final int T_INT = 203;
    public static final int T_FLOAT64 = 204;
    public static final int T_STRING = 205;
    public static final int T_BOOL = 206;
    public static final int T_ERROR = -1;

    public static int verificar(String operador, int tipo1, int tipo2) {
        switch (operador) {
            // Operadores Aritméticos
            case "+":
                if (tipo1 == T_INT && tipo2 == T_INT) return T_INT;
                if (tipo1 == T_INT && tipo2 == T_FLOAT64) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_INT) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_FLOAT64) return T_FLOAT64;
                if (tipo1 == T_STRING && tipo2 == T_STRING) return T_STRING;
                return T_ERROR;

            case "-":
            case "*":
                if (tipo1 == T_INT && tipo2 == T_INT) return T_INT;
                if (tipo1 == T_INT && tipo2 == T_FLOAT64) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_INT) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_FLOAT64) return T_FLOAT64;
                return T_ERROR;

            case "/":
                if (tipo1 == T_INT && tipo2 == T_INT) return T_FLOAT64;
                if (tipo1 == T_INT && tipo2 == T_FLOAT64) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_INT) return T_FLOAT64;
                if (tipo1 == T_FLOAT64 && tipo2 == T_FLOAT64) return T_FLOAT64;
                return T_ERROR;

            // Operadores Relacionales de Orden
            case "<":
            case ">":
            case "<=":
            case ">=":
                if ((tipo1 == T_INT || tipo1 == T_FLOAT64) && (tipo2 == T_INT || tipo2 == T_FLOAT64)) {
                    return T_BOOL;
                }
                return T_ERROR;

            // Operadores Relacionales de Igualdad
            case "==":
            case "!=":
                if ((tipo1 == T_INT || tipo1 == T_FLOAT64) && (tipo2 == T_INT || tipo2 == T_FLOAT64)) {
                    return T_BOOL;
                }
                if (tipo1 == T_STRING && tipo2 == T_STRING) {
                    return T_BOOL;
                }
                if (tipo1 == T_BOOL && tipo2 == T_BOOL) {
                    return T_BOOL;
                }
                return T_ERROR;

            // Operadores Lógicos
            case "&&":
            case "||":
                if (tipo1 == T_BOOL && tipo2 == T_BOOL) {
                    return T_BOOL;
                }
                return T_ERROR;
            
            // Operador de Asignación
            case "=":
                if (tipo1 == tipo2) {
                    return tipo1;
                }
                if (tipo1 == T_FLOAT64 && tipo2 == T_INT) {
                    return T_FLOAT64; 
                }
                return T_ERROR;

            default:
                // Si el operador no es reconocido
                return T_ERROR;
        }
    }

    public static int verificar(String operador, int tipo) {
        switch (operador) {
            case "!":
                if (tipo == T_BOOL) {
                    return T_BOOL;
                }
                return T_ERROR;
            default:
                // Si el operador no es unario o no es reconocido
                return T_ERROR;
        }
    }
}