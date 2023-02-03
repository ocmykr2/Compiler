package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public class RetInst extends Inst {
    public RetInst() {
        super(Type.VOID);
    }

    public RetInst(Value v) {
        super(Type.VOID);
        addUse(v);
    }
}
