package Codegen.IR.Value.Inst;

import Codegen.IR.Type.IntType;
import Codegen.IR.Value.Value;

public class IcmpInst extends Inst {
    public enum OptTable {
        eq, // =
        ne, // !=
        sgt,// >
        sge,// >=
        slt,// <
        sle // <=
    }

    public OptTable whi;

    public IcmpInst(OptTable whi, Value src1, Value src2) {
        super(IntType.INT1);
        // boolean
        this.whi = whi;
        addUse(src1);
        addUse(src2);
    }
}
