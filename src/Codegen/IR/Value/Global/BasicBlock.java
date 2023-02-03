package Codegen.IR.Value.Global;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;
import Codegen.IR.Value.Inst.Inst;
import Codegen.IR.Value.Value;

import java.util.LinkedList;

public class BasicBlock extends Value {
    public LinkedList<Inst> Allinst = new LinkedList<>();

    public BasicBlock() {
        super(Type.LABEL);
    }

    public BasicBlock(String id) {
        super(Type.LABEL);
        this.id = id;
    }

    public void addInst(Inst inst) {
        Allinst.add(inst);
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
