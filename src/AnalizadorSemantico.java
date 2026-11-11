import java.util.ArrayList;
import java.util.HashMap;

// Controla declaraciones y tipos antes de generar codigo.
public class AnalizadorSemantico {
    private final HashMap<String, String> tipos = new HashMap<>();
    private final ArrayList<String> errores = new ArrayList<>();

    public boolean analizar(NodoMain main) {
        for (NodoDeclaracion d : main.declaraciones) {
            if (tipos.containsKey(d.id)) {
                errores.add("La variable '" + d.id + "' ya fue declarada.");
            } else {
                tipos.put(d.id, d.tipo);
            }
        }

        analizarSentencias(main.sentencias, main);
        return errores.isEmpty();
    }

    private void analizarSentencias(ArrayList<Sentencia> sentencias, NodoMain main) {
        for (Sentencia s : sentencias) analizarSentencia(s, main);
    }

    private void analizarSentencia(Sentencia s, NodoMain main) {
        if (s instanceof NodoAsignacion) {
            NodoAsignacion asig = (NodoAsignacion) s;
            String tipoExpr = tipoDe(asig.expr);
            if (!tipos.containsKey(asig.id)) {
                errores.add("Se asigna a '" + asig.id + "' pero no esta declarada.");
            } else if (tipoExpr != null && !tipoExpr.equals(tipos.get(asig.id))) {
                errores.add("No se puede asignar un valor " + tipoExpr + " a la variable "
                        + tipos.get(asig.id) + " '" + asig.id + "'.");
            }

        } else if (s instanceof NodoReturn) {
            NodoReturn ret = (NodoReturn) s;
            if (main.tipoRetorno.equals("void")) {
                if (ret.expr != null) {
                    tipoDe(ret.expr);
                    errores.add("main es void y no puede retornar un valor.");
                }
            } else if (ret.expr == null) {
                errores.add("main es " + main.tipoRetorno + " y el return debe devolver un valor.");
            } else {
                String tipoExpr = tipoDe(ret.expr);
                if (tipoExpr != null && !tipoExpr.equals(main.tipoRetorno)) {
                    errores.add("main debe retornar " + main.tipoRetorno + " pero retorna " + tipoExpr + ".");
                }
            }

        } else if (s instanceof NodoIf) {
            NodoIf nIf = (NodoIf) s;
            analizarCondicion(nIf.condicion, "if");
            analizarSentencias(nIf.entonces, main);
            if (nIf.sino != null) analizarSentencias(nIf.sino, main);

        } else if (s instanceof NodoWhile) {
            NodoWhile nWhile = (NodoWhile) s;
            analizarCondicion(nWhile.condicion, "while");
            analizarSentencias(nWhile.cuerpo, main);
        }
    }

    // La condicion de un if/while tiene que ser bool (true, false o una expresion bool)
    private void analizarCondicion(Expresion cond, String sentencia) {
        String tipo = tipoDe(cond);
        if (tipo != null && !tipo.equals("bool")) {
            errores.add("La condicion del " + sentencia + " debe ser bool pero es " + tipo + ".");
        }
    }

    private String tipoDe(Expresion expr) {
        if (expr instanceof NodoNumero) return "int";
        if (expr instanceof NodoBooleano) return "bool";

        if (expr instanceof NodoIdentificador) {
            String id = ((NodoIdentificador) expr).id;
            if (!tipos.containsKey(id)) {
                errores.add("Se usa la variable '" + id + "' pero no esta declarada.");
                return null;
            }
            return tipos.get(id);
        }

        NodoOperacion op = (NodoOperacion) expr;
        String izq = tipoDe(op.izq);
        String der = tipoDe(op.der);
        if (izq == null || der == null) return null;
        if (!izq.equals("int") || !der.equals("int")) {
            errores.add("El operador '" + op.operador + "' solo se aplica a int (se recibio "
                    + izq + " " + op.operador + " " + der + ").");
            return null;
        }
        return "int";
    }

    public void imprimirErrores() {
        for (String e : errores) System.err.println("Error Semántico: " + e);
    }
}
