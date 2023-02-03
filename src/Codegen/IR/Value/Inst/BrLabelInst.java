package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public class BrLabelInst extends Inst {
    public BrLabelInst(Value dest) {
        super(Type.VOID);
        addUse(dest);
    }
}
