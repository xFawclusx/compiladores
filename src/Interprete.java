import java.util.ArrayList;

public class Interprete {
    private TablaDeSimbolos tabla = new TablaDeSimbolos();
    private boolean retorno = false; // se activa con un return para cortar tambien los if/while

    public void ejecutar(NodoMain main) {
        System.out.println("\n--- INICIANDO INTÉRPRETE ---");

        // 1. Guarda las declaraciones en memoria
        for (NodoDeclaracion d : main.declaraciones) {
            tabla.declarar(d.id, d.tipo);
        }

        // 2. Ejecuta las sentencias matematica
        ejecutarSentencias(main.sentencias);

        // Muestra como quedaron las variables tras hacer todas las cuentas
        tabla.imprimirMemoria();
    }

    private void ejecutarSentencias(ArrayList<Sentencia> sentencias) {
        for (Sentencia s : sentencias) {
            if (s instanceof NodoAsignacion) {
                NodoAsignacion asig = (NodoAsignacion) s;
                // Resuelve la matematica a la derecha del igual
                Object resultado = evaluar(asig.expr);
                tabla.asignar(asig.id, resultado);

            } else if (s instanceof NodoReturn) {
                NodoReturn ret = (NodoReturn) s;
                if (ret.expr != null) {
                    System.out.println(">> EL PROGRAMA RETORNO: " + evaluar(ret.expr));
                } else {
                    System.out.println(">> EL PROGRAMA RETORNO: void");
                }
                retorno = true;

            } else if (s instanceof NodoIf) {
                NodoIf nIf = (NodoIf) s;
                if ((Boolean) evaluar(nIf.condicion)) ejecutarSentencias(nIf.entonces);
                else if (nIf.sino != null) ejecutarSentencias(nIf.sino);

            } else if (s instanceof NodoWhile) {
                NodoWhile nWhile = (NodoWhile) s;
                while (!retorno && (Boolean) evaluar(nWhile.condicion)) {
                    ejecutarSentencias(nWhile.cuerpo);
                }
            }
            if (retorno) break; // Corta la ejecucion como un return real
        }
    }

    // Método recursivo para resolver ecuaciones de abajo hacia arriba
    private Object evaluar(Expresion expr) {
        if (expr instanceof NodoNumero) return ((NodoNumero) expr).valor;
        if (expr instanceof NodoBooleano) return ((NodoBooleano) expr).valor;
        if (expr instanceof NodoIdentificador) return tabla.obtener(((NodoIdentificador) expr).id);

        if (expr instanceof NodoOperacion) {
            NodoOperacion op = (NodoOperacion) expr;
            // Se llama a sí mismo hasta resolver toda la rama izquierda y derecha
            Integer valIzq = (Integer) evaluar(op.izq);
            Integer valDer = (Integer) evaluar(op.der);

            if (op.operador.equals("+")) return valIzq + valDer;
            if (op.operador.equals("*")) return valIzq * valDer;
        }
        return null;
    }
}
