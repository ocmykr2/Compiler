package Codegen.IR.Constant;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.IntType;

public class IntConstant extends Constant {
    public int val;
    public static IntConstant TRUE = new IntConstant(1, 1);
    public static IntConstant FALSE = new IntConstant(0, 1);
    public static IntConstant BOOL_TRUE = new IntConstant(1, 8);
    public static IntConstant BOOL_FALSE = new IntConstant(0, 8);

    public static IntConstant ZERO = new IntConstant(0);
    public static IntConstant ONE = new IntConstant(1);
    public static IntConstant FOUR = new IntConstant(4);
    public static IntConstant NEG_ONE = new IntConstant(-1);
    public IntConstant(int val, int len) {
        super(new IntType(len));
        this.val = val;
    }

    public IntConstant(int value) {
        this(value, 32);
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
