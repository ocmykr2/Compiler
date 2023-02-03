package Codegen.IR.Value.Global;

import Codegen.IR.IRVisitor;

import Codegen.IR.Type.Type;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Value;

import java.util.ArrayList;
import java.util.HashMap;

// The most top structure

public class Module extends Value {
    public HashMap < String, Function > AllFunc = new HashMap<>();
    public HashMap < String, Variable > AllVar = new HashMap<>();
    public HashMap < String, Type> AllClass = new HashMap<>();
    public ArrayList <StrConstant > AllConstant = new ArrayList<>();

    public Module(String id) {
        super(Type.VOID);
        this.id = id;
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
