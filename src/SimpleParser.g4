parser grammar SimpleParser;

options { tokenVocab=SimpleLexer; }

prgm:	EOF									# End
    |	stmt prgm							# Statement
    |	ENUM ID '{' ID (CM ID)* '}' prgm	# EnumDef
    |	TYPE ID '{' (ID ID SC)+ '}' prgm	# TypeDef
    |	PROC ID '(' param ')' stmt prgm		# ProcDef
    |	IMPORT STR SC prgm					# Import
    ;

stmt:	SC									# Blank
    |	expr SC								# Evaluate
    |	PRINT expr SC						# Print
    |	RETURN expr SC						# Return
    |	'{' stmt+ '}'						# Nested
    |	ID ID SC							# Define
    |	ID ':=' expr SC						# Assign
    |	ID ID ':=' expr SC					# Initiate
    |	IF '(' expr ')' THEN '{' stmt+ '}'	# IfThen
    |	IF '(' expr ')' THEN '{' stmt+ '}' ELSE '{' stmt+ '}'	# IfElse
    |	WHILE '(' expr ')' '{' stmt+ '}'	# While
    ;

expr:	ID									# Variable
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
    |	'{' expr (CM expr)* '}'				# Compound
    |	ID '.' ID							# Member
    |	ID '(' subst ')'					# ProcCall
    |	ID '[' expr (CL expr)? ']'			# Substr
    |	op=('+'|'-') expr					# SignBit
    |	<assoc=right> expr '^' expr			# Pow
    |	expr op=('*'|'/') expr				# MulDiv
    |	expr op=('+'|'-') expr				# AddSub
    |	expr op=(LT|LE|EQ|GE|GT) expr		# Compare
    |	NOT expr							# Not
    |	expr AND expr						# And
    |	expr OR expr						# Or
    |	'(' expr ')'						# Bracket
    ;

param:	/* epsilon */ | VOID | ID ID (CM ID ID)* ;
subst:	/* epsilon */ | VOID | expr (CM expr)* ;
