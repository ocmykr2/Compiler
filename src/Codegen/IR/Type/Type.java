package Codegen.IR.Type;

public class Type {
    public static final Type NULL = new Type(TypeTable.NULL);
    public static final Type VOID = new Type(TypeTable.VOID);
    public static final Type LABEL = new Type(TypeTable.LABEL);
    public TypeTable basicType;

    public int size = -1;

    public Type(TypeTable basicType) {
        this.basicType = basicType;
    }

    public boolean isVoid() {
        return basicType == TypeTable.VOID;
    }

    public int getSize() {
        return (size == -1) ? (size = 0) : size;
    }

    public String toString() {
        if(basicType == TypeTable.VOID) return "void";
        if(basicType ==  TypeTable.LABEL) return "label";
        return null;
    }

    @Override
    public boolean equals(Object rhs) {
        if(this == rhs) return true;
        if(rhs instanceof PtrType && this.basicType == TypeTable.NULL) {
            return true;
        }
        if(rhs == null || getClass() != rhs.getClass())
            return false;
        return basicType == ((Type)rhs).basicType;
    }
}
