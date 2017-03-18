parser grammar SimpleParser;

options { tokenVocab=SimpleLexer; }

/* Main Program */
prgm:	unit* ;

/* Interpretation Unit */
unit:	IMPORT STR ';'						# Import
    |	rtype ID '(' para ')' stmt			# Procedure
    |	stmt								# Statement
    ;

/* Statements */
stmt:	';'									# Blank
    |	expr ';'							# Check
    |	'{' stmt+ '}'						# Nested
    |	type ID init ';'					# Declare
    |	dest '=' expr ';'					# Assign
    |	IF expr THEN stmt END				# IfThen
    |	IF expr THEN stmt ELSE stmt END		# IfElse
    |	DO stmt WHILE expr END				# DoWhile
    |	WHILE expr DO stmt END				# WhileDo
    |	RETURN expr? ';'					# Return
    ;

/* Expressions */
expr:	ID									# Identifier
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
    |	expr '(' argu ')'					# ProcCall
    |	expr '[' expr ']'					# Subscript
    |	expr '[' expr ':' expr ']'			# Substring
    |	op=('+'|'-') expr					# UnaryPM
    |	<assoc=right> expr '^' expr			# Pow
    |	expr op=('*'|'/') expr				# MulDiv
    |	expr op=('+'|'-') expr				# AddSub
    |	expr op=(LT|LE|EQ|GE|GT) expr		# Compare
    |	NOT expr							# Not
    |	expr AND expr						# And
    |	expr OR expr						# Or
    |	'(' expr ')'						# Bracket
    ;

// declare & assign subrules
type:	ID									# VarType
    |	ID '[' INT ']'						# ArrType
    ;
init:	/* epsilon */						# DefInit
    |	'=' expr							# ValInit
    ;
dest:	ID									# VarDest
    |	ID '[' expr ']'						# ArrDest
    ;

// procedure subrules
para:	/* epsilon */ | VOID | ptype ID (',' ptype ID)* ;
argu:	/* epsilon */ | VOID | expr (',' expr)* ;
rtype:	VOID | ID ;
ptype:	ID									# VarPass
    |	ID '[' ']'							# ArrPass
    ;
