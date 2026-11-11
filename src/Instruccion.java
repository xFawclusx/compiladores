// Instruccion de tres direcciones
public class Instruccion {
    enum Op { ASIGNACION, SUMA, MULTIPLICACION, RETURN,
              ETIQUETA, SALTO, SALTO_SI_FALSO } // control de flujo: resultado guarda la etiqueta

    final Op op;
    final String arg1, arg2, resultado;

    Instruccion(Op op, String arg1, String arg2, String resultado) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.resultado = resultado;
    }

    // true si la instruccion escribe en una variable/temporal (y no en una etiqueta)
    boolean escribeVariable() {
        return resultado != null && op != Op.ETIQUETA && op != Op.SALTO && op != Op.SALTO_SI_FALSO;
    }

    @Override
    public String toString() {
        switch (op) {
            case ASIGNACION:     return resultado + " = " + arg1;  //Resultado = arg1
            case SUMA:           return resultado + " = " + arg1 + " + " + arg2; //Resultado  = arg1 + arg2
            case MULTIPLICACION: return resultado + " = " + arg1 + " * " + arg2; //Resultado = arg1 * arg2
            case RETURN:         return arg1 == null ? "return" : "return " + arg1; // Return O Return arg1
            case ETIQUETA:       return resultado + ":"; // L1:
            case SALTO:          return "goto " + resultado; // goto L1
            case SALTO_SI_FALSO: return "ifFalse " + arg1 + " goto " + resultado; // Si arg1 es falso salta a L1
        }
        return "?";
    }
}
