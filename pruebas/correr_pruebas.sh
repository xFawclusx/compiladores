#!/bin/bash
# Corre todos los casos de prueba de la carpeta pruebas/.
# Uso (desde la raiz del proyecto):  bash pruebas/correr_pruebas.sh
# Requiere haber compilado antes en out/ (ver readme.txt).

CP="out:lib/java-cup-11b-2015.03.26.jar"

for f in pruebas/ok_*.txt pruebas/error_*.txt; do
    echo "=================================================="
    echo "CASO: $f"
    echo "=================================================="
    java -cp "$CP" Main "$f" 2>&1 | grep -E "exitoso|SEMANTICO|Error|error|RETORNO"
    echo
done
