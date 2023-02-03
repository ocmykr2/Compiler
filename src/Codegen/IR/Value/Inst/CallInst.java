package Codegen.IR.Value.Inst;

import Codegen.IR.Type.FuncType;
import Codegen.IR.Value.Value;

public class CallInst extends  Inst{
    public CallInst(Value Func) {
        super(((FuncType)Func.type).retType);
        this.addUse(Func);
    }

    public CallInst(Value Func, Value ... parameters) {
        this(Func);
        for(Value v : parameters) {
            this.addUse(v);
        }
    }
}
