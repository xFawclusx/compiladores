import java.util.ArrayList;

// Clase base para todos los nodos
abstract class NodoAST {}
abstract class Expresion extends NodoAST {}
abstract class Sentencia extends NodoAST {}

// --- Main ---
class NodoMain extends NodoAST {
    String tipoRetorno;
    ArrayList<NodoDeclaracion> declaraciones;
    ArrayList<Sentencia> sentencias;

    public NodoMain(String t, ArrayList<NodoDeclaracion> d, ArrayList<Sentencia> s) {
        this.tipoRetorno = t; this.declaraciones = d; this.sentencias = s;
    }
}

// --- DECLARACIONES ---
class NodoDeclaracion extends NodoAST {
    String tipo, id;
    public NodoDeclaracion(String t, String i) { this.tipo = t; this.id = i; }
}

// --- SENTENCIAS ---
class NodoAsignacion extends Sentencia {
    String id; Expresion expr;
    public NodoAsignacion(String i, Expresion e) { this.id = i; this.expr = e; }
}

class NodoReturn extends Sentencia {
    Expresion expr; // Permite null para la regla "return;"
    public NodoReturn(Expresion e) { this.expr = e; }
}

class NodoIf extends Sentencia {
    Expresion condicion;
    ArrayList<Sentencia> entonces;
    ArrayList<Sentencia> sino; // null si no tiene else
    public NodoIf(Expresion c, ArrayList<Sentencia> e, ArrayList<Sentencia> s) {
        this.condicion = c; this.entonces = e; this.sino = s;
    }
}

class NodoWhile extends Sentencia {
    Expresion condicion;
    ArrayList<Sentencia> cuerpo;
    public NodoWhile(Expresion c, ArrayList<Sentencia> b) { this.condicion = c; this.cuerpo = b; }
}

// --- EXPRESIONES ---
class NodoOperacion extends Expresion {
    Expresion izq, der; String operador;
    public NodoOperacion(Expresion i, String op, Expresion d) {
        this.izq = i; this.operador = op; this.der = d;
    }
}

class NodoIdentificador extends Expresion {
    String id;
    public NodoIdentificador(String i) { this.id = i; }
}

class NodoNumero extends Expresion {
    Integer valor;
    public NodoNumero(Integer v) { this.valor = v; }
}

class NodoBooleano extends Expresion {
    Boolean valor;
    public NodoBooleano(Boolean v) { this.valor = v; }
}