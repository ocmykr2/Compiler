package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Operand;
import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.Assembly.ASMValue.Operand.Symbol;

// x[rd] = pc + sext(immediate[31:12] << 12)

public class Auipc extends ASMInst {
    public Auipc(Reg rd, Operand imm) {
        this.rd = rd;
        this.imm = imm;
    }

    // auipc rd, %hi(Symbol)

    public String toString() {
        if(imm instanceof Symbol) {
            return String.format("auipc %s, %s", rd, ((Symbol) imm).highbit());
        } else return String.format("auipc %s, %s", rd, imm);
    }
}
