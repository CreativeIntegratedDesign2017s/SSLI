parser grammar SimpleParser;
options { tokenVocab=SimpleLexer; }
prgm:	unit* ;

/* Interpretation Unit */
unit:	stmt								# Statement
    |	rtype ID '(' para_list ')' block	# Procedure
    |	IMPORT STR ';'						# Import
    ;

block: '{' stmt_list '}' ;
stmt_list: stmt*;

/* Statements */
stmt:	expr? ';'							            # Evaluate
    |	type ID init ';'					            # Declare
    |	expr '=' expr ';'					            # Assign
    |	IF expr THEN stmt_list (ELSE stmt_list)? END	# IfElse
    |	DO stmt_list WHILE expr END				        # DoWhile
    |	WHILE expr DO stmt_list END				        # WhileDo
    |	RETURN expr? ';'					            # Return
    |   block                                           # Nested
    ;

/* Expressions */
expr:	ID									# Identifier
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
    |	expr '(' argu_list ')'				# ProcCall
    |	expr '[' expr ']'					# Subscript
    |	expr '[' expr ':' expr ']'			# Substring
    |	op=('+'|'-') expr					# UnaryPM
    |	<assoc=right> expr '^' expr			# Pow
    |	expr op=('*'|'/') expr				# MulDiv
    |	expr op=('+'|'-') expr				# AddSub
    |	expr op=(LT|LE|EQ|NEQ|GE|GT) expr	# Compare
    |	NOT expr							# Not
    |	expr AND expr						# And
    |	expr OR expr						# Or
    |	'(' expr ')'						# Bracket
    ;

// type declaration subrules

// variable declararation subrules
type:	ID ('[' INT ']')* ;
init:	('=' expr)? ;

// procedure subrules
para_list: VOID? | ptype ID (',' ptype ID)* ;
argu_list: VOID? | expr (',' expr)* ;
rtype:	VOID | ID ;
ptype:	ID | ID '&' | ID ('[' ']')+ ;
