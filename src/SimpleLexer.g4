lexer grammar SimpleLexer;

/* Comments */
SLC :	'//' .*? '\n' -> skip ;

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
NEQ :	'!=' ;
LE  :	'<=' ;
GE  :	'>=' ;
NOT :	'!' ;
AND :	'&&' ;
OR  :	'||' ;
AMP :	'&' ;

/* Keywords */
PROC:   'proc';
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
STR :	'"' ('\\"' | '\\\\' | '\\n' | '\\t' | '\\r' | ~('\\' | '\r' | '\n'))*? '"' ;
ID  :	[a-zA-Z] ([a-zA-Z0-9])* ;
WS  :	[ \t\r\n]+ -> skip ;
