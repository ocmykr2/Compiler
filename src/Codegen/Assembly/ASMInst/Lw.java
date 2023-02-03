package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Immediate;
import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.Assembly.ASMValue.Operand.Symbol;

public class Lw extends ASMInst {
    public Lw(Reg rd, Reg rs1, Immediate imm) {
        this.rd = rd;
        this.rs1 = rs1;
        this.imm = imm;
    }
    // lw rd, offset(rs1)

    public Lw(Reg rd, Symbol imm) {
        this.rd = rd;
        this.imm = imm;
    }
    // lw rd, symbol

    @Override
    public String toString() {
        if(imm instanceof Symbol) {
            return String.format("lw %s, %s", rd, imm);
        } else {
            return String.format("lw %s, %s(%s)", rd, imm, rs1);
        }
    }
}
