package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public class StoreInst extends Inst {

    public StoreInst(Value v, Value ptr) {
        super(Type.VOID);
        addUse(v);
        addUse(ptr);
    }

}