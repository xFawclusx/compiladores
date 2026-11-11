import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Tres direcciones -> assembly x86-64
public class GeneradorAssembly {
    private final StringBuilder asm = new StringBuilder();
    private final LinkedHashMap<String, Integer> pila = new LinkedHashMap<>();

    public String generar(NodoMain main, ArrayList<Instruccion> codigo) {
        for (NodoDeclaracion d : main.declaraciones) reservar(d.id);
        for (Instruccion ins : codigo) {
            if (ins.escribeVariable()) reservar(ins.resultado);
        }
        int tamanio = pila.size() * 8;
        if (tamanio % 16 != 0) tamanio += 8; //Si no es multiplo de 16 suma 8

        linea("    .text");
        linea("    .globl  main");
        linea("    .type   main, @function");
        linea("main:");
        linea("    pushq   %rbp"); //Guardo el basePointer del programa main
        linea("    movq    %rsp, %rbp");  // Defino mi nuevo basePointer
        if (tamanio > 0) linea("    subq    $" + tamanio + ", %rsp");  //Defino mi nuevo stackPointer
        for (NodoDeclaracion d : main.declaraciones) {
            linea("    movq    $0, " + dir(d.id));
        }

        for (Instruccion ins : codigo) {
            linea("");
            linea("    # " + ins);
            switch (ins.op) {
                case ASIGNACION:
                    linea("    movq    " + operando(ins.arg1) + ", %rax");
                    linea("    movq    %rax, " + dir(ins.resultado));
                    break;
                case SUMA:
                    linea("    movq    " + operando(ins.arg1) + ", %rax");
                    linea("    addq    " + operando(ins.arg2) + ", %rax");
                    linea("    movq    %rax, " + dir(ins.resultado));
                    break;
                case MULTIPLICACION:
                    linea("    movq    " + operando(ins.arg1) + ", %rax");
                    linea("    imulq   " + operando(ins.arg2) + ", %rax");
                    linea("    movq    %rax, " + dir(ins.resultado));
                    break;
                case ETIQUETA:
                    linea("." + ins.resultado + ":");
                    break;
                case SALTO:
                    linea("    jmp     ." + ins.resultado);
                    break;
                case SALTO_SI_FALSO: // Si la condicion vale 0 (false) salta
                    linea("    movq    " + operando(ins.arg1) + ", %rax");
                    linea("    cmpq    $0, %rax");
                    linea("    je      ." + ins.resultado);
                    break;
                case RETURN:
                    if (ins.arg1 != null) linea("    movq    " + operando(ins.arg1) + ", %rax");
                    else linea("    movq    $0, %rax");
                    linea("    leave");
                    linea("    ret");
                    break;
            }
        }

        linea("");
        linea("    # fin");
        linea("    movq    $0, %rax");
        linea("    leave");
        linea("    ret");
        linea("    .size   main, .-main");
        linea("    .section .note.GNU-stack,\"\",@progbits");
        return asm.toString();
    }

    private void reservar(String nombre) {
        if (!pila.containsKey(nombre)) pila.put(nombre, -8 * (pila.size() + 1));
    }

    private String dir(String nombre) {
        return pila.get(nombre) + "(%rbp)";
    }

    private String operando(String arg) { //Le pongo el sibolo de $ que significa que es constante
        if (arg.equals("true")) return "$1";
        if (arg.equals("false")) return "$0";
        if (Character.isDigit(arg.charAt(0))) return "$" + arg;
        return dir(arg);
    }

    private void linea(String s) { asm.append(s).append("\n"); }

    public void guardar(String ruta) throws IOException {
        try (FileWriter fw = new FileWriter(ruta)) { fw.write(asm.toString()); }
    }
}
