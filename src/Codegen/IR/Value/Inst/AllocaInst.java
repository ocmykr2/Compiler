package Codegen.IR.Value.Inst;

import Codegen.IR.Type.PtrType;
import Codegen.IR.Type.Type;

public class AllocaInst extends Inst {
    public AllocaInst(Type type) {
        super(new PtrType(type));
    }
}
