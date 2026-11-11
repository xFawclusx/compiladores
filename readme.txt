==========================================================
 COMPILADOR JAVA - PASOS PARA COMPILAR Y EJECUTAR
==========================================================

El compilador lee un programa (por ejemplo ej.txt), hace el analisis
lexico, sintactico y semantico, genera codigo de tres direcciones y
assembly x86-64 (salida.s), y despues lo ejecuta con el interprete.

Soporta: int, bool, asignaciones, + y *, return, if, if/else y while.
La condicion del if/while va entre parentesis y tiene que ser una
expresion bool (true, false o una variable bool). El cuerpo va entre
llaves y es una lista de sentencias.

Todos los comandos se corren desde la carpeta raiz del proyecto
(la que tiene src/, lib/ y ej.txt).


----------------------------------------------------------
 0) REQUISITOS
----------------------------------------------------------
- Java JDK 17 o superior (java y javac).
    java -version
    javac -version
- (Opcional) gcc, solo para ensamblar y correr salida.s en Linux.


----------------------------------------------------------
 1) REGENERAR LEXER Y PARSER (solo si se cambia lex.flex o sintactico.cup)
----------------------------------------------------------
Los archivos Lexer.java, Parser.java y Sym.java ya estan generados en src/.
Si se modifica lex.flex o sintactico.cup hay que volver a generarlos:

  Linux:
    java -jar lib/jflex-full-1.9.1.jar -d src src/lex.flex
    java -jar lib/java-cup-11b-2015.03.26.jar -parser Parser -symbols Sym -destdir src src/sintactico.cup

  Windows (PowerShell):
    java -jar lib\jflex-full-1.9.1.jar -d src src\lex.flex
    java -jar lib\java-cup-11b-2015.03.26.jar -parser Parser -symbols Sym -destdir src src\sintactico.cup

(JFlex deja una copia vieja src/Lexer.java~ que se puede borrar.)


----------------------------------------------------------
 2) COMPILAR
----------------------------------------------------------
  Linux:
    mkdir -p out
    javac -encoding UTF-8 -cp lib/java-cup-11b-2015.03.26.jar -d out src/*.java

  Windows (PowerShell):
    New-Item -ItemType Directory -Force -Path out | Out-Null
    javac -encoding UTF-8 -cp "lib\java-cup-11b-2015.03.26.jar" -d out src\*.java

Los avisos "uses unchecked or unsafe operations" de Parser.java son normales.


----------------------------------------------------------
 3) EJECUTAR
----------------------------------------------------------
  Linux (ojo: el separador del classpath es ":"):
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main ej.txt

  Windows (el separador es ";"):
    java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main ej.txt

Si no se pasa ningun archivo usa ej.txt. Para otro programa:
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main ej_control.txt
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main pruebas/ok_02_while.txt

La salida muestra en orden:
  1. "¡Analisis sintactico exitoso!"
  2. El arbol de sintaxis abstracta (AST)
  3. El resultado del analisis semantico
  4. El codigo intermedio de tres direcciones
  5. El assembly x86-64 (tambien se guarda en salida.s)
  6. La ejecucion con el interprete (valor de retorno y memoria final)


----------------------------------------------------------
 4) (OPCIONAL) CORRER EL ASSEMBLY NATIVO EN LINUX
----------------------------------------------------------
    gcc salida.s -o programa
    ./programa
    echo $?

"echo $?" muestra el valor que retorno main (true = 1, false = 0, void = 0).
Tiene que coincidir con lo que imprime el interprete
(">> EL PROGRAMA RETORNO: ..."), salvo que el codigo de salida
de Linux va de 0 a 255.


----------------------------------------------------------
 5) CASOS DE PRUEBA (carpeta pruebas/)
----------------------------------------------------------
Para correrlos todos juntos (Linux):
    bash pruebas/correr_pruebas.sh

O uno por uno:
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main pruebas/<archivo>.txt

--- Casos que funcionan ---------------------------------------------
Archivo                                   Que prueba                          Resultado esperado
ok_01_if_else.txt                         if/else (entra al else)             RETORNO: 10
ok_02_while.txt                           while que da 2 vueltas con un if    RETORNO: 204
ok_03_anidados.txt                        while dentro de while, if anidado   RETORNO: 11

--- Casos que fallan ------------------------------------------------
error_01_semantico_condicion_int.txt      while (x) con x int
    -> Error Semántico: La condicion del while debe ser bool pero es int.
error_02_sintactico_sin_llaves.txt        if (b) x = 2;   (faltan las llaves)
    -> Syntax error ... Error durante la compilacion
error_03_lexico_operador_relacional.txt   while (x < 5)   (no hay operadores relacionales)
    -> Error léxico: < en línea 3, columna 11  (despues tambien da Syntax error)

Nota: las lineas y columnas que informan los errores empiezan a contar desde 0.
