package Codegen.IR.Value.Inst;


import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;
import Codegen.IR.Value.User;

public class Inst extends User {
    public Inst(Type type) {
        super(type);
    }
    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
