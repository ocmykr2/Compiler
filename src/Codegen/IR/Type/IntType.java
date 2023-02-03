package Codegen.IR.Type;

public class IntType extends Type {
    public int len;

    public static final IntType INT1 = new IntType(1);
    public static final IntType INT8 = new IntType(8);
    public static final IntType INT32 = new IntType(32);

    public IntType(int len) {
        super(TypeTable.INT);
        this.len = len;
    }

    @Override
    public boolean equals(Object rhs) {
        if(this == rhs) return true;
        if(rhs == null || getClass() != rhs.getClass()) return false;
        if(!super.equals(rhs)) return false;
        return len == ((IntType) rhs).len;
    }

    @Override
    public String toString() {
        return "i" + len;
    }

    public int getSize() {
        return (size == -1) ? (size = 4) : size;
    }

}
