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
expr:	ID									                # Identifier
    |	BOOL								                # Boolean
    |	INT									                # Integer
    |	STR									                # String
    |	ID '(' argu_list ')'				                # ProcCall
    |	Container = expr '[' Indexer = expr ']'	            # Subscript
    |	Container = expr '[' From = expr ':' To = expr ']'  # Substring
    |	op=('+'|'-') expr					                # UnaryPM
    |	<assoc=right> Base = expr POW Exponent = expr       # Pow
    |	Oprnd1 = expr op=('*'|'/') Oprnd2 = expr            # MulDiv
    |	Oprnd1 = expr op=('+'|'-') Oprnd2 = expr		    # AddSub
    |	Oprnd1 = expr op=(LT|LE|EQ|NEQ|GE|GT) Oprnd2 = expr # Compare
    |	NOT expr							                # Not
    |	Oprnd1 = expr AND Oprnd2 = expr		                # And
    |	Oprnd1 = expr OR Oprnd2 = expr						# Or
    |	'(' expr ')'						                # Bracket
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
