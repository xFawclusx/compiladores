import java.io.FileReader;
import java.util.ArrayList;
import java_cup.runtime.Symbol;

public class Main {
    public static void main(String[] args) {
        String rutaArchivo = args.length > 0 ? args[0] : "ej.txt";
        String rutaAssembly = "salida.s";

        try {
            Lexer lexer = new Lexer(new FileReader(rutaArchivo));
            Parser parser = new Parser(lexer);
            Symbol resultado = parser.parse();
            NodoMain ast = (NodoMain) resultado.value;

            System.out.println("¡Analisis sintactico exitoso!");

            System.out.println("\n--- ARBOL DE SINTAXIS ABSTRACTA ---");
            ImpresorAST.imprimir(ast, "");

            AnalizadorSemantico semantico = new AnalizadorSemantico();
            if (!semantico.analizar(ast)) {
                System.out.println("\n--- ANALISIS SEMANTICO: SE ENCONTRARON ERRORES ---");
                semantico.imprimirErrores();
                return;
            }
            System.out.println("\n--- ANALISIS SEMANTICO: sin errores ---");

            GeneradorCodigoIntermedio gci = new GeneradorCodigoIntermedio();
            ArrayList<Instruccion> codigo = gci.generar(ast);
            gci.imprimir();

            GeneradorAssembly genAsm = new GeneradorAssembly();
            String assembly = genAsm.generar(ast, codigo);
            genAsm.guardar(rutaAssembly);
            System.out.println("\n--- ASSEMBLY x86-64 (guardado en " + rutaAssembly + ") ---");
            System.out.print(assembly);

            Interprete interprete = new Interprete();
            interprete.ejecutar(ast);

        } catch (java.io.FileNotFoundException e) {
            System.err.println("Error: No se encontro el archivo " + rutaArchivo);
        } catch (Exception e) {
            System.err.println("Error durante la compilacion:");
            e.printStackTrace();
        }
    }
}
