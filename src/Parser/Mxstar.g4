grammar Mxstar;

program : (funcDef | (classDef) | (varDefStmt))*;

classDef
    : Class Identifier
        '{' (funcDef | constructFunc | varDefStmt)* '}' ';'
    ;

funcDef : returnType Identifier '(' funcVarDefList? ')' block;
constructFunc : Identifier '('funcVarDefList ')' block;
lambda : '[' '&'? ']' '(' funcVarDefList ')' '->' block '(' expressionList ')';

block : '{' statement* '}';

statement
    : block                                                    # blockStatement
    | ifStmt                                                   # ifStatement
    | whileStmt                                                # whileStatement
    | forStmt                                                  # forStatement
    | (Break | Continue) ';'                                   # ctrlStatement
    | Return expression? ';'                                   # returnStatement
    | varDefStmt                                               # varDefStatement
    | expression ';'                                           # exprStatement
    | ';'                                                      # emptyStatement
    ;
funcVarDef: type varDefSub;
varDefStmt : varDef ';';
varDef : type varDefSub (',' varDefSub)*;
varDefSub : Identifier ('=' expression)?;
funcVarDefList : (funcVarDef (',' funcVarDef)* )?;

expression :
    // highest-level-priority
    primary                                                                 # primaryExpr
    // priority level 2
    | '(' expression ')'                                                      # bracketExpr
    | New newType('(' expression? ')')?                                       # nvarExpr
    | base = expression '[' index = expression ']'                            # subscriptExpr
    | expression op = ('++' | '--')                                           # suffixExpr
    | expression '(' expressionList ')'                                       # callExpr
    | expression op = '.' Identifier                                          # memberExpr
    | lambda                                                                  # lambdaExpr
    // priority level 3
    | <assoc = right> op = ('++' | '--') expression                           # prefixExpr
    | <assoc = right> op = ('+' | '-' | '!' | '~') expression                 # prefixExpr
    // level 4 and more
    | src1 = expression op = ('*' | '/' | '%')       src2 = expression        # binaryExpr
    | src1 = expression op = ('+' | '-')           src2 = expression          # binaryExpr
    | src1 = expression op = ('<<' | '>>')         src2 = expression          # binaryExpr
    | src1 = expression op = ('==' | '!=')         src2 = expression          # binaryExpr
    | src1 = expression op = ('<' | '>' | '<=' | '>=') src2 = expression      # binaryExpr
    | src1 = expression op = '&'                 src2 = expression            # binaryExpr
    | src1 = expression op = '^'                 src2 = expression            # binaryExpr
    | src1 = expression op = '|'                 src2 = expression            # binaryExpr
    | src1 = expression op = '&&'                src2 = expression            # binaryExpr
    | src1 = expression op = '||'                src2 = expression            # binaryExpr
    | <assoc = right> src1 = expression op = '='   src2 = expression          # assignExpr
    ;
    expressionList : (expression (',' expression)*)? ;

ifStmt : If '(' expression ')' trueStmt = statement (Else falseStmt = statement)?;
whileStmt : While '(' expression ')' statement;
forStmt : For '('(varDef | init = expression)?';' cond = expression? ';' chg = expression? ')' statement;

returnType : type | Void;
type : basicType('[' ']')*;
newType : basicType ('[' expression ']')* ('[' ']')*;
basicType : Identifier | Bool | Int | String;

const : NumberConst | StringConst | BoolConst | NullConst;
primary : Identifier | This | const;

NumberConst : [1-9][0-9]* | '0';
StringConst : '"' (~[\\"] | '\\' ["\\tn]) * '"';
BoolConst : True | False;
NullConst : Null;

Void : 'void';
Bool : 'bool';
Int : 'int';
String : 'string';
New : 'new';
Class : 'class';
Null : 'null';
True : 'true';
False : 'false';
This : 'this';
If : 'if';
Else : 'else';
For : 'for';
While : 'while';
Break : 'break';
Continue : 'continue';
Return : 'return';
Identifier : [a-zA-Z] [a-zA-Z_0-9]* ;


Indent : [ \t]+ -> skip;
NewLine : '\r' ? '\n' -> skip;
Comment : ('//' ~[\r\n]* | '/*' .*? '*/') -> skip;