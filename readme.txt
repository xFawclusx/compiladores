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

Requisito: Java JDK 17 o superior (java y javac). Para verificarlo:
    java -version
    javac -version

IMPORTANTE:
- Todos los comandos se corren desde la carpeta raiz del proyecto
  (la que tiene src/, lib/ y ej.txt).
- Hay que compilar (paso 2) antes de ejecutar (paso 3). La carpeta out/
  no se sube al repositorio, asi que despues de clonar hay que compilar.
- Los comandos de Linux y de Windows son DISTINTOS. Usar solo los de
  tu sistema.


##########################################################
##                                                      ##
##                       LINUX                          ##
##                (terminal bash)                       ##
##                                                      ##
##########################################################

En Linux el classpath se separa con DOS PUNTOS ":" y las rutas usan "/".

----------------------------------------------------------
 1) (Solo si se cambio lex.flex o sintactico.cup) REGENERAR LEXER Y PARSER
----------------------------------------------------------
Lexer.java, Parser.java y Sym.java ya estan generados en src/.
Este paso solo hace falta si se modifica lex.flex o sintactico.cup.

    java -jar lib/jflex-full-1.9.1.jar -d src src/lex.flex
    java -jar lib/java-cup-11b-2015.03.26.jar -parser Parser -symbols Sym -destdir src src/sintactico.cup
    rm -f src/Lexer.java~

----------------------------------------------------------
 2) COMPILAR
----------------------------------------------------------
    mkdir -p out
    javac -encoding UTF-8 -cp lib/java-cup-11b-2015.03.26.jar -d out src/*.java

(El aviso "uses unchecked or unsafe operations" de Parser.java es normal.)

----------------------------------------------------------
 3) EJECUTAR
----------------------------------------------------------
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main ej.txt

Para otro programa se cambia el ultimo archivo:
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main ej_control.txt
    java -cp out:lib/java-cup-11b-2015.03.26.jar Main pruebas/ok_02_while.txt

----------------------------------------------------------
 4) CORRER TODOS LOS CASOS DE PRUEBA
----------------------------------------------------------
    bash pruebas/correr_pruebas.sh

----------------------------------------------------------
 5) (Opcional) EJECUTAR EL ASSEMBLY NATIVO
----------------------------------------------------------
Requiere gcc. Despues de ejecutar el paso 3 se genera salida.s:

    gcc salida.s -o programa
    ./programa
    echo $?

"echo $?" muestra el valor que retorno main (true = 1, false = 0, void = 0).
Tiene que coincidir con lo que imprime el interprete
(">> EL PROGRAMA RETORNO: ..."), salvo que en Linux el codigo de
salida va de 0 a 255.


##########################################################
##                                                      ##
##                      WINDOWS                         ##
##                    (PowerShell)                      ##
##                                                      ##
##########################################################

En Windows el classpath se separa con PUNTO Y COMA ";" y las rutas usan "\".

OJO: en PowerShell el ";" sin comillas corta el comando en dos.
Por eso el classpath va SIEMPRE ENTRE COMILLAS:
    BIEN:  java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main ej.txt
    MAL:   java -cp out;lib\java-cup-11b-2015.03.26.jar Main ej.txt
           (muestra "Usage: java [java options...]" y no ejecuta nada)

----------------------------------------------------------
 1) (Solo si se cambio lex.flex o sintactico.cup) REGENERAR LEXER Y PARSER
----------------------------------------------------------
Lexer.java, Parser.java y Sym.java ya estan generados en src\.
Este paso solo hace falta si se modifica lex.flex o sintactico.cup.

    java -jar lib\jflex-full-1.9.1.jar -d src src\lex.flex
    java -jar lib\java-cup-11b-2015.03.26.jar -parser Parser -symbols Sym -destdir src src\sintactico.cup
    Remove-Item -ErrorAction SilentlyContinue src\Lexer.java~

----------------------------------------------------------
 2) COMPILAR
----------------------------------------------------------
    New-Item -ItemType Directory -Force -Path out | Out-Null
    javac -encoding UTF-8 -cp "lib\java-cup-11b-2015.03.26.jar" -d out src\*.java

(El aviso "uses unchecked or unsafe operations" de Parser.java es normal.)

----------------------------------------------------------
 3) EJECUTAR
----------------------------------------------------------
    java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main ej.txt

Para otro programa se cambia el ultimo archivo:
    java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main ej_control.txt
    java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main pruebas\ok_02_while.txt

----------------------------------------------------------
 4) CORRER TODOS LOS CASOS DE PRUEBA
----------------------------------------------------------
    powershell -ExecutionPolicy Bypass -File pruebas\correr_pruebas.ps1

(El "-ExecutionPolicy Bypass" evita que Windows bloquee el script.)

----------------------------------------------------------
 5) ASSEMBLY NATIVO
----------------------------------------------------------
El salida.s se genera igual en Windows, pero es assembly para Linux
x86-64, asi que no se puede ensamblar ni ejecutar en Windows.
Para verlo funcionar hay que usar Linux (o WSL) con el paso 5 de Linux.


##########################################################
##               ERRORES COMUNES                        ##
##########################################################

"Error: no se ha encontrado o cargado la clase principal Main"
"ClassNotFoundException: Main"
  - No se compilo (falta el paso 2) o no existe la carpeta out/.
  - No estas parado en la carpeta raiz del proyecto.
  - En Linux se uso ";" en vez de ":" en el classpath.

"Usage: java [java options...] <application> ..."
  - En Windows/PowerShell se escribio el classpath sin comillas.
    Usar: java -cp "out;lib\java-cup-11b-2015.03.26.jar" Main ej.txt

"Error: No se encontro el archivo ..."
  - La ruta del programa a compilar esta mal escrita o no estas en la
    carpeta raiz del proyecto.


##########################################################
##               QUE MUESTRA LA SALIDA                  ##
##########################################################

  1. "¡Analisis sintactico exitoso!"
  2. El arbol de sintaxis abstracta (AST)
  3. El resultado del analisis semantico
  4. El codigo intermedio de tres direcciones
  5. El assembly x86-64 (tambien se guarda en salida.s)
  6. La ejecucion con el interprete (valor de retorno y memoria final)

Si hay un error, el compilador se detiene en la fase donde lo encontro.


##########################################################
##               CASOS DE PRUEBA (carpeta pruebas)      ##
##########################################################

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
