package Codegen.IR.Value.Global;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;
import Codegen.IR.Constant.Constant;
import Codegen.IR.Value.Value;

public class Variable extends Value {
    public Constant initVal = null;

    public Variable(Type type, String id) {
        super(type);
        this.id = id;
    }

    public Variable(Type type, String id, Constant initVal) {
        super(type);
        this.id = id;
        this.initVal = initVal;
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
