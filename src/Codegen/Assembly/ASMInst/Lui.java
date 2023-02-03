package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Operand;
import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.Assembly.ASMValue.Operand.Symbol;

public class Lui extends ASMInst {
    public Lui(Reg rd, Operand imm) {
        this.rd = rd;
        this.imm = imm;
    }

    // lui rd, %hi(symbol)
    // lui rd, imm

    public String toString() {
        if(imm instanceof Symbol) {
            return String.format("lui %s, %s", rd, ((Symbol) imm).highbit());
        } else
            return String.format("lui %s, %s", rd, imm);
    }
}
