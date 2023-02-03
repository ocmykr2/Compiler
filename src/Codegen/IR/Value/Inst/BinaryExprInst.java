package Codegen.IR.Value.Inst;

import Codegen.IR.Value.Value;

public class BinaryExprInst extends Inst {
    public enum OptTable {
        add,
        sub,
        mul,
        sdiv, // divide
        srem, // remainder

        shl, // (x << y)
        ashr, // (x >> y)

        and,
        or,
        xor
    }

    public OptTable whi;

    public BinaryExprInst(OptTable whi, Value src1, Value src2) {
        super(src1.type);
        assert src1.type.equals(src2.type);
        this.whi = whi;
        addUse(src1);
        addUse(src2);
    }

}
