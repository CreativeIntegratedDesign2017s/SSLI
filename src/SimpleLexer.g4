lexer grammar SimpleLexer;

/* Symbols */
LRB :	'(' ;
RRB :	')' ;
LCB :	'{' ;
RCB :	'}' ;
LSB :	'[' ;
RSB :	']' ;
DOT :	'.' ;
CM  :	',' ;
CL  :	':' ;
SC  :	';' ;
ASN :	':=' ;
ADD :	'+' ;
SUB :	'-' ;
MUL :	'*' ;
DIV :	'/' ;
POW :	'^' ;
LT  :	'<' ;
GT  :	'>' ;
EQ  :	'==' ;
LE  :	'<=' ;
GE  :	'>=' ;
NOT :	'!' ;
AND :	'&&' ;
OR  :	'||' ;

/* Keywords */
IF  :	'if' ;
THEN:	'then' ;
ELSE:	'else' ;
WHILE:	'while' ;
PRINT:	'print' ;
RETURN:	'return' ;
IMPORT:	'import' ;
ENUM:	'enum' ;
TYPE:	'type' ;
PROC:	'proc' ;
VOID:   'void' ;

/* Default Mode */
BOOL:	'true' | 'false' ;
INT :	[0-9]+ ;
STR :	'"' ('\\"' | '\\\\' | ~('\\' | '\r' | '\n'))*? '"' ;
ID  :	[a-zA-Z] ([a-zA-Z0-9])* ;
WS  :	[ \t\r\n]+ -> skip ;
