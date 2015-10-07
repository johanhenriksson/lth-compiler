package lang.ast; // The generated scanner will belong to the package lang.ast

import lang.ast.LangParser.Terminals; // The terminals are implicitly defined in the parser
import lang.ast.LangParser.SyntaxError;

%%

// define the signature for the generated scanner
%public
%final
%class LangScanner
%extends beaver.Scanner

// the interface between the scanner and the parser is the nextToken() method
%type beaver.Symbol 
%function nextToken 

// store line and column information in the tokens
%line
%column

// this code will be inlined in the body of the generated scanner class
%{
  private beaver.Symbol sym(short id) {
    return new beaver.Symbol(id, yyline + 1, yycolumn + 1, yylength(), yytext());
  }
%}

// macros
WhiteSpace = [ ] | \t | \f | \n | \r
ID         = [a-zA-Z][_a-zA-Z0-9]*
Int        = "-"? [0-9]+
Float      = "-"? [0-9]+ \. [0-9]+
String     = \"[^\"]*\"
Comment    = "//" [^\n\r]* ([\n\r])? | "/*"((\*+[^/*])|([^*]))*\**"*/"

%%

// discard whitespace information
{WhiteSpace}    { }

// discard comments
{Comment}       { }

// token definitions
"if"       { return sym(Terminals.IF); }
"else"     { return sym(Terminals.ELSE); }
"while"    { return sym(Terminals.WHILE); }
"true"     { return sym(Terminals.TRUE); }
"false"    { return sym(Terminals.FALSE); }
"return"   { return sym(Terminals.RETURN); }
"break"    { return sym(Terminals.BREAK); }
"continue" { return sym(Terminals.CONTINUE); }
","        { return sym(Terminals.COMMA); }
";"        { return sym(Terminals.SEMI); }
"("        { return sym(Terminals.LPARAM); }
")"        { return sym(Terminals.RPARAM); }
"{"        { return sym(Terminals.LBRACE); }
"}"        { return sym(Terminals.RBRACE); }
"="        { return sym(Terminals.ASSIGN); }
"=="       { return sym(Terminals.EQUALS); }
"!="       { return sym(Terminals.NOTEQ); }
"<="       { return sym(Terminals.LEQUAL); }
">="       { return sym(Terminals.GEQUAL); }
"<"        { return sym(Terminals.LESS); }
">"        { return sym(Terminals.GREATER); }
"+"        { return sym(Terminals.PLUS); }
"-"        { return sym(Terminals.MINUS); }
"*"        { return sym(Terminals.MULT); }
"/"        { return sym(Terminals.DIV); }
"%"        { return sym(Terminals.MOD); }
"!"        { return sym(Terminals.NOT); }
"&&"       { return sym(Terminals.AND); }
"||"       { return sym(Terminals.OR); }

{Int}      { return sym(Terminals.INT); }
{Float}    { return sym(Terminals.FLOAT); }
{String}   { return sym(Terminals.STRING); }
{ID}       { return sym(Terminals.ID); }
<<EOF>>    { return sym(Terminals.EOF); }

/* error fallback */
[^]        { throw new SyntaxError("Illegal character <"+yytext()+">"); }
