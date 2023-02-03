package Codegen.IR.Constant;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;

// int a; -> empty init.

public class EmptyConstant extends Constant{
    public EmptyConstant(Type type) {
        super(type);
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
