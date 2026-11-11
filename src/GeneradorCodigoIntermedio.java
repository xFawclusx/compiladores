import java.util.ArrayList;

// AST -> codigo de tres direcciones.
public class GeneradorCodigoIntermedio {
    private final ArrayList<Instruccion> codigo = new ArrayList<>();
    private int contadorTemporales = 0;
    private int contadorEtiquetas = 0;

    private String nuevoTemporal() {
        contadorTemporales++;
        return "t" + contadorTemporales;
    }

    private String nuevaEtiqueta() {
        contadorEtiquetas++;
        return "L" + contadorEtiquetas;
    }

    public ArrayList<Instruccion> generar(NodoMain main) {
        generarSentencias(main.sentencias);
        return codigo;
    }

    private void generarSentencias(ArrayList<Sentencia> sentencias) {
        for (Sentencia s : sentencias) {
            if (s instanceof NodoAsignacion) {
                NodoAsignacion asig = (NodoAsignacion) s;
                String lugar = generarExpresion(asig.expr);
                codigo.add(new Instruccion(Instruccion.Op.ASIGNACION, lugar, null, asig.id));

            } else if (s instanceof NodoReturn) {
                NodoReturn ret = (NodoReturn) s;
                String lugar = (ret.expr != null) ? generarExpresion(ret.expr) : null;
                codigo.add(new Instruccion(Instruccion.Op.RETURN, lugar, null, null));

            } else if (s instanceof NodoIf) {
                // ifFalse cond goto Lsino ; entonces ; goto Lfin ; Lsino: sino ; Lfin:
                NodoIf nIf = (NodoIf) s;
                String cond = generarExpresion(nIf.condicion);
                String lSino = nuevaEtiqueta();
                codigo.add(new Instruccion(Instruccion.Op.SALTO_SI_FALSO, cond, null, lSino));
                generarSentencias(nIf.entonces);
                if (nIf.sino != null) {
                    String lFin = nuevaEtiqueta();
                    codigo.add(new Instruccion(Instruccion.Op.SALTO, null, null, lFin));
                    codigo.add(new Instruccion(Instruccion.Op.ETIQUETA, null, null, lSino));
                    generarSentencias(nIf.sino);
                    codigo.add(new Instruccion(Instruccion.Op.ETIQUETA, null, null, lFin));
                } else {
                    codigo.add(new Instruccion(Instruccion.Op.ETIQUETA, null, null, lSino));
                }

            } else if (s instanceof NodoWhile) {
                // Linicio: ifFalse cond goto Lfin ; cuerpo ; goto Linicio ; Lfin:
                NodoWhile nWhile = (NodoWhile) s;
                String lInicio = nuevaEtiqueta();
                String lFin = nuevaEtiqueta();
                codigo.add(new Instruccion(Instruccion.Op.ETIQUETA, null, null, lInicio));
                String cond = generarExpresion(nWhile.condicion);
                codigo.add(new Instruccion(Instruccion.Op.SALTO_SI_FALSO, cond, null, lFin));
                generarSentencias(nWhile.cuerpo);
                codigo.add(new Instruccion(Instruccion.Op.SALTO, null, null, lInicio));
                codigo.add(new Instruccion(Instruccion.Op.ETIQUETA, null, null, lFin));
            }
        }
    }

    private String generarExpresion(Expresion expr) {
        if (expr instanceof NodoNumero)        return String.valueOf(((NodoNumero) expr).valor);
        if (expr instanceof NodoBooleano)      return String.valueOf(((NodoBooleano) expr).valor);
        if (expr instanceof NodoIdentificador) return ((NodoIdentificador) expr).id;

        NodoOperacion op = (NodoOperacion) expr;
        String izq = generarExpresion(op.izq);
        String der = generarExpresion(op.der);
        String t = nuevoTemporal();
        Instruccion.Op tipo = op.operador.equals("+") ? Instruccion.Op.SUMA : Instruccion.Op.MULTIPLICACION; // SI  no  es suma entonces el tipo es multiplicacion
        codigo.add(new Instruccion(tipo, izq, der, t));
        return t;
    }

    public void imprimir() {
        System.out.println("\n--- CODIGO INTERMEDIO (tres direcciones) ---");
        for (int i = 0; i < codigo.size(); i++) {
            System.out.println((i + 1) + ": " + codigo.get(i));
        }
    }
}
