grammar Exp;

file
    :    block EOF
    ;

block
    :    (statement)*
    ;

statement
    :    function
    |    variable
    |    expression
    |    whileStatement
    |    ifStatement
    |    assignment
    |    returnStatement
    ;

function
    :    'fun' Identifier '(' parameterNames ')' '{' block '}'
    ;

variable
    :    'var' Identifier ('=' expression)?
    ;

parameterNames
    :    (Identifier (',' Identifier)*)?
    ;

whileStatement
    :    'while' '(' expression ')' '{' block '}'
    ;

ifStatement
    :    'if' '(' expression ')' '{' block '}' ('else' '{' block '}')?
    ;

assignment
    :    Identifier '=' expression
    ;

returnStatement
    :    'return' expression
    ;

expression
    :    functionCall
    |    expression Operation expression
    |    Identifier
    |    Literal
    |    '(' expression ')'
    ;

functionCall
    :    Identifier '(' arguments ')'
    ;

arguments
    :    (expression (',' expression)*)?
    ;

Identifier
    :    [A-Za-z_] [A-Za-z_0-9]*
    ;

Literal
    :    '-'? [1-9] [0-9]*
    |    '0'
    ;

Operation
    :    '+'
    |    '-'
    |    '*'
    |    '/'
    |    '%'
    |    '>'
    |    '<'
    |    '>='
    |    '<='
    |    '=='
    |    '!='
    |    '||'
    |    '&&'
    ;

WS : (' ' | '\t' | '\r'| '\n' | '//' .*? (('\r')?'\n' | EOF)) -> skip;