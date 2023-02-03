package Codegen.IR.Value.Inst;

import Codegen.IR.Type.PtrType;
import Codegen.IR.Value.Value;

public class LoadInst extends Inst {
    public LoadInst(Value ptr) {
        super(((PtrType)ptr.type).getLast());
        addUse(ptr);
    }
}
