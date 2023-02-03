package Codegen.IR.Value.Global;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.FuncType;
import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

import java.util.ArrayList;
import java.util.LinkedList;

// must get an id

public class Function extends Value {
    public ArrayList < Variable > AllVar = new ArrayList<>();
    public LinkedList < BasicBlock > AllBlock  = new LinkedList<>();

    public int StackSize = 0;

    public Function(Type type, String id) {
        super(type);
        this.id = id;
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
