package Codegen.IR.Constant;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;

public class NullConstant extends Constant {
    public NullConstant(Type type) {
        super(type);
    }

    public NullConstant() {
        this(Type.NULL);
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
