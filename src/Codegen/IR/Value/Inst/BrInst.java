package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public class BrInst extends Inst {
    public BrInst(Value cond, Value If_val, Value Else_val) {
        super(Type.VOID);
        addUse(cond);
        addUse(If_val);
        addUse(Else_val);
    }
}
