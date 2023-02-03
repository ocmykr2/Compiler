package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Reg;

// mv rd, rs = addi rd, rs, 0

public class Mv extends ASMInst {
    public Mv(Reg rd, Reg rs1) {
        this.rd = rd;
        this.rs1 = rs1;
    }

    // mv rd, rs1

    public String toString() {
        return String.format("mv %s, %s", rd, rs1);
    }
}
