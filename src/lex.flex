// --- SECCIÓN 1: Código de Usuario ---
import java_cup.runtime.Symbol;

%%
// --- SECCIÓN 2: Opciones y Declaraciones ---
%class Lexer
%cupsym Sym
%cup
%line
%column
%public

// Macros de expresiones regulares
Letra = [a-zA-Z]
Digito = [0-9]
Identificador = {Letra} ({Letra} | {Digito})*
Numero = {Digito}+
Espacio = [ \t\r\n]+

%%
// --- SECCIÓN 3: Reglas Léxicas ---

// Ignorar espacios
{Espacio} { /* Ignorar */ }

// Palabras reservadas y constantes lógicas
"int"     { return new Symbol(Sym.INT, yyline, yycolumn); }
"bool"    { return new Symbol(Sym.BOOL, yyline, yycolumn); }
"void"    { return new Symbol(Sym.VOID, yyline, yycolumn); }
"main"    { return new Symbol(Sym.MAIN, yyline, yycolumn); }
"return"  { return new Symbol(Sym.RETURN, yyline, yycolumn); }
"true"    { return new Symbol(Sym.TRUE, yyline, yycolumn); }
"false"   { return new Symbol(Sym.FALSE, yyline, yycolumn); }
"if"      { return new Symbol(Sym.IF, yyline, yycolumn); }
"else"    { return new Symbol(Sym.ELSE, yyline, yycolumn); }
"while"   { return new Symbol(Sym.WHILE, yyline, yycolumn); }

// Operadores y delimitadores
"+"       { return new Symbol(Sym.MAS, yyline, yycolumn); }
"*"       { return new Symbol(Sym.POR, yyline, yycolumn); }
"="       { return new Symbol(Sym.IGUAL, yyline, yycolumn); }
";"       { return new Symbol(Sym.PYC, yyline, yycolumn); }
"("       { return new Symbol(Sym.PAR_IZQ, yyline, yycolumn); }
")"       { return new Symbol(Sym.PAR_DER, yyline, yycolumn); }
"{"       { return new Symbol(Sym.LLA_IZQ, yyline, yycolumn); }
"}"       { return new Symbol(Sym.LLA_DER, yyline, yycolumn); }

// Variables y constantes numéricas (envían su valor al parser)
{Identificador} { return new Symbol(Sym.ID, yyline, yycolumn, yytext()); }
{Numero}        { return new Symbol(Sym.NRO, yyline, yycolumn, Integer.parseInt(yytext())); }

// Manejo de errores léxicos
. { System.err.println("Error léxico: " + yytext() + " en línea " + yyline + ", columna " + yycolumn); }
