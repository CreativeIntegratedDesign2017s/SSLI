lexer grammar SimpleLexer;

/* Symbols */
LRB :	'(' ;
RRB :	')' ;
LCB :	'{' ;
RCB :	'}' ;
LSB :	'[' ;
RSB :	']' ;
CM  :	',' ;
CL  :	':' ;
SC  :	';' ;
ASN :	'=' ;
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
AMP :	'&' ;

/* Keywords */
IF  :	'if' ;
THEN:	'then' ;
ELSE:	'else' ;
DO  :	'do' ;
WHILE:	'while' ;
END :	'end' ;
RETURN:	'return' ;
IMPORT:	'import' ;
VOID:	'void' ;

/* Default Mode */
BOOL:	'true' | 'false' ;
INT :	[0-9]+ ;
STR :	'"' ('\\"' | '\\\\' | ~('\\' | '\r' | '\n'))*? '"' ;
ID  :	[a-zA-Z] ([a-zA-Z0-9])* ;
WS  :	[ \t\r\n]+ -> skip ;
