

public class ImpresorAST {
    public static void imprimir(NodoAST nodo, String prefijo) {
        if (nodo instanceof NodoMain) {
            NodoMain n = (NodoMain) nodo;
            System.out.println(prefijo + "Programa (Main) - Retorna: " + n.tipoRetorno);
            System.out.println(prefijo + "  Declaraciones:");
            for (NodoDeclaracion d : n.declaraciones) imprimir(d, prefijo + "    ");
            System.out.println(prefijo + "  Sentencias:");
            for (Sentencia s : n.sentencias) imprimir(s, prefijo + "    ");

        } else if (nodo instanceof NodoDeclaracion) {
            NodoDeclaracion n = (NodoDeclaracion) nodo;
            System.out.println(prefijo + "Declara: " + n.tipo + " " + n.id);

        } else if (nodo instanceof NodoAsignacion) {
            NodoAsignacion n = (NodoAsignacion) nodo;
            System.out.println(prefijo + "Asignacion a variable '" + n.id + "':");
            imprimir(n.expr, prefijo + "  ");

        } else if (nodo instanceof NodoReturn) {
            NodoReturn n = (NodoReturn) nodo;
            System.out.println(prefijo + "Return:");
            if (n.expr != null) imprimir(n.expr, prefijo + "  ");
            else System.out.println(prefijo + "  (vacío)");

        } else if (nodo instanceof NodoIf) {
            NodoIf n = (NodoIf) nodo;
            System.out.println(prefijo + "If:");
            System.out.println(prefijo + "  Condicion:");
            imprimir(n.condicion, prefijo + "    ");
            System.out.println(prefijo + "  Entonces:");
            for (Sentencia s : n.entonces) imprimir(s, prefijo + "    ");
            if (n.sino != null) {
                System.out.println(prefijo + "  Sino:");
                for (Sentencia s : n.sino) imprimir(s, prefijo + "    ");
            }

        } else if (nodo instanceof NodoWhile) {
            NodoWhile n = (NodoWhile) nodo;
            System.out.println(prefijo + "While:");
            System.out.println(prefijo + "  Condicion:");
            imprimir(n.condicion, prefijo + "    ");
            System.out.println(prefijo + "  Cuerpo:");
            for (Sentencia s : n.cuerpo) imprimir(s, prefijo + "    ");

        } else if (nodo instanceof NodoOperacion) {
            NodoOperacion n = (NodoOperacion) nodo;
            System.out.println(prefijo + "Operacion: [ " + n.operador + " ]");
            imprimir(n.izq, prefijo + "  ");
            imprimir(n.der, prefijo + "  ");

        } else if (nodo instanceof NodoIdentificador) {
            System.out.println(prefijo + "Variable: " + ((NodoIdentificador) nodo).id);

        } else if (nodo instanceof NodoNumero) {
            System.out.println(prefijo + "Numero: " + ((NodoNumero) nodo).valor);

        } else if (nodo instanceof NodoBooleano) {
            System.out.println(prefijo + "Booleano: " + ((NodoBooleano) nodo).valor);
        }
    }
}