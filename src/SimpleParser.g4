parser grammar SimpleParser;

options { tokenVocab=SimpleLexer; }

prgm:	EOF									# EndPrgm
    |	stmt prgm							# Statement
    |	ENUM ID '{' ID (',' ID)* '}' prgm	# EnumDef
    |	TYPE ID '{' (ID ID ';')+ '}' prgm	# Composite
    |	TDEF ID ID ';' prgm					# Derived
    |	PROC ID ID '(' param ')' stmt prgm	# ProcDef
    |	IMPORT STR ';' prgm					# Import
    ;

stmt:	';'									# Blank
    |	expr ';'							# Check
    |	ID ID (':=' expr)? ';'				# Define
    |	expr ':=' expr ';'					# Assign
    |	IF expr THEN stmt (ELSE stmt)? END	# IfElse
    |	DO stmt WHILE expr END				# DoWhile
    |	WHILE expr DO stmt END				# WhileDo
    |	RETURN expr? ';'					# Return
    |	'{' stmt+ '}'						# Nested
    ;

expr:	ID									# Identifier
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
    |	expr '.' expr						# Member
    |	expr '(' subst ')'					# ProcCall
    |	expr '[' expr (':' expr)? ']'		# Subscript
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

param:	/* epsilon */ | VOID | ID ID (',' ID ID)* ;
subst:	/* epsilon */ | VOID | expr (',' expr)* ;
