package Codegen.IR.Value.Inst;

import Codegen.IR.Type.Type;

public class UnreachableInst extends Inst {

    public UnreachableInst() {
        super(Type.VOID);
    }

}
