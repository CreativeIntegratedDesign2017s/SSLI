parser grammar SimpleParser;

options { tokenVocab=SimpleLexer; }

/* Program */
prgm:	EOF									# EndPrgm
    |	stmt prgm							# Statement
    |	ENUM ID '{' ID (',' ID)* '}' prgm	# EnumDef
    |	TYPE ID ID ';' prgm					# TypeDef
    |	PROC rt ID '(' pr ')' stmt prgm		# ProcDef
    |	IMPORT STR ';' prgm					# Import
//  |	TYPE ID '{' (ID ID ';')+ '}' prgm	# Composite
    ;

/* Statement */
stmt:	';'									# Blank
    |	expr ';'							# Check
    |	'{' stmt+ '}'						# Nested
    /* Declarations && Assignments */
    |	tid vid init ';'					# Declare
    |	lval ':=' rval ';'					# Assign
    /* Selection Statements */
    |	IF expr THEN stmt END				# IfThen
    |	IF expr THEN stmt ELSE stmt END		# IfElse
    /* Iteration Statements */
    |	DO stmt WHILE expr END				# DoWhile
    |	WHILE expr DO stmt END				# WhileDo
    /* Jump Statements */
    |	CONTINUE ';'						# Continue
    |	BREAK ';'							# Break
    |	RETURN expr? ';'					# Return
    ;

// for Declare Statements
tid :	ID									# NormType
//  |	ID '@'								# RefType
    ;
vid :	ID									# VarDecl
    |	ID '[' INT ']'						# ArrDecl
    ;
init: /* epsilon */							# ByDefault
    |	':=' expr							# ByValue
    ;

// for Assign Statements
lval:	ID									# VarAsgn
    |	ID '[' expr ']'						# ArrAsgn
//  |	'#' ID								# RefAsgn
    ;
rval:	expr ;

/* Expression */
expr:	ID									# Identifier
    |	BOOL								# Boolean
    |	INT									# Integer
    |	STR									# String
//  |	expr '.' ID							# Member
    |	expr '(' st ')'						# ProcCall
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
    |	'{' expr (',' expr)* '}'			# Compound
    ;

// for Function Define & Call
rt  :	VOID | ID ;
pr  :	/* epsilon */ | VOID | ID ID (',' ID ID)* ;
st  :	/* epsilon */ | VOID | expr (',' expr)* ;
