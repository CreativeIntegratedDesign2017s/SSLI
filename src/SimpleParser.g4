parser grammar SimpleParser;

options { tokenVocab=SimpleLexer; }

prgm:	EOF									# EndPrgm
    |	stmt prgm							# Statement
    |	ENUM ID '{' ID (CM ID)* '}' prgm	# EnumDef
    |	TYPE ID '{' (ID ID SC)+ '}' prgm	# TypeDef
    |	PROC ID '(' param ')' stmt prgm		# ProcDef
    |	IMPORT STR SC prgm					# Import
    ;

stmt:	SC									# Blank
    |	expr SC								# Evaluate
    |	ID ID (':=' expr)? ';'				# Define
    |	expr ':=' expr ';'					# Assign
    |	IF expr THEN stmt					# IfThen
    |	IF expr THEN stmt ELSE stmt			# IfElse
    |	DO stmt WHILE expr ';'				# DoWhile
    |	WHILE expr DO stmt					# WhileDo
    |	RETURN expr SC						# Return
    |	'{' stmt+ '}'						# Nested
    ;

expr:	ID									# Variable
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
    |	expr '.' expr						# Member
    |	expr '(' subst ')'					# ProcCall
    |	expr '[' expr (CL expr)? ']'		# Subscript
    |	op=('+'|'-') expr					# SignBit
    |	<assoc=right> expr '^' expr			# Pow
    |	expr op=('*'|'/') expr				# MulDiv
    |	expr op=('+'|'-') expr				# AddSub
    |	expr op=(LT|LE|EQ|GE|GT) expr		# Compare
    |	NOT expr							# Not
    |	expr AND expr						# And
    |	expr OR expr						# Or
    |	'(' expr ')'						# Bracket
    |	'{' expr (CM expr)* '}'				# Compound
    ;

param:	/* epsilon */ | VOID | ID ID (CM ID ID)* ;
subst:	/* epsilon */ | VOID | expr (CM expr)* ;
