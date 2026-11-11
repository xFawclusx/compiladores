import java.util.HashMap;

public class TablaDeSimbolos {
    // Guarda el nombre de la variable y su valor actual
    private HashMap<String, Object> memoria = new HashMap<>();

    public void declarar(String nombre, String tipo) {
        if (memoria.containsKey(nombre)) {
            throw new RuntimeException("Error Semantico: Variable '" + nombre + "' ya fue declarada.");
        }
        // Inicializamos: los int empiezan en 0, los bool en false
        memoria.put(nombre, tipo.equals("int") ? 0 : false);
    }

    public void asignar(String nombre, Object valor) {
        if (!memoria.containsKey(nombre)) {
            throw new RuntimeException("Error Semantico: Intentas asignar valor a '" + nombre + "' pero no esta declarada.");
        }
        memoria.put(nombre, valor);
    }

    public Object obtener(String nombre) {
        if (!memoria.containsKey(nombre)) {
            throw new RuntimeException("Error Semantico: Intentas usar la variable '" + nombre + "' pero no esta declarada.");
        }
        return memoria.get(nombre);
    }

    public void imprimirMemoria() {
        System.out.println("\n--- MEMORIA AL FINALIZAR ---");
        for (String var : memoria.keySet()) {
            System.out.println(var + " = " + memoria.get(var));
        }
    }
}