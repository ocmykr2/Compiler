package Codegen.IR.Type;

public enum TypeTable {
    VOID,
    NULL, // constant null
    INT,
    POINTER,
    LABEL, // basic_block's type
    STRUCT,
    ARRAY,
    FUNCTION
}
