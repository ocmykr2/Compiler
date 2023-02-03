package Codegen.IR.Type;

public class PtrType extends Type {
    public Type oriType;
    public int dim;

    //char* and int*

    public PtrType(Type oriType) {
        this(oriType, 1);
    }

    public PtrType(Type oriType, int dim) {
        super(TypeTable.POINTER);

        if(oriType.basicType == TypeTable.POINTER) {
            this.oriType = ((PtrType) oriType).oriType;
            this.dim = ((PtrType) oriType).dim + dim;
        } else {
            this.oriType = oriType;
            this.dim = dim;
        }
    }

    public static PtrType I8STAR = new PtrType(IntType.INT8);
    public static PtrType I32STAR = new PtrType(IntType.INT32);

    @Override
    public int getSize() {
        return (size == -1) ? (size = 8) : size;
    }

    @Override
    public boolean equals(Object rhs) {
        if(this == rhs) return true;
        // If instanceof Type -> tranverse to type
        if(rhs instanceof Type type && type.basicType == TypeTable.NULL)
            return true;
        if(rhs == null || getClass() != rhs.getClass())
            return false;
        return dim == ((PtrType)rhs).dim
                && oriType.equals(((PtrType)rhs).oriType);
    }

    public Type getLast() {
        if(dim == 1) {
            return oriType;
        } else
            return new PtrType(oriType, dim - 1);
    }

    public String toString() {
        String s = oriType.toString()
                + "*".repeat(dim);
        return s;
    }
}
