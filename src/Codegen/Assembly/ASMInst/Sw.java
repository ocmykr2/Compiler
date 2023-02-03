package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Immediate;
import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.Assembly.ASMValue.Operand.Symbol;

public class Sw extends ASMInst {
    public Sw(Reg rs2, Reg rs1, Immediate imm) {
        this.rs2 = rs2;
        this.rs1 = rs1;
        this.imm = imm;
    }

    public Sw(Reg rs2, Reg rs1, Symbol imm) {
        this.rs2 = rs2;
        this.rs1 = rs1;
        this.imm = imm;
    }

    public String toString() {
        if(imm instanceof Immediate) {
            return String.format("sw %s, %s(%s)", rs2, imm, rs1);
        } else  {
            return String.format("sw %s, %s(%s)", rs2, ((Symbol)imm).lowbit(), rs1);
        }
    }
}
