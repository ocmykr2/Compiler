package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public class BitCastInst extends Inst {
    public BitCastInst(Type aimType, Value val) {
        super(aimType);
        addUse(val);
    }
}
