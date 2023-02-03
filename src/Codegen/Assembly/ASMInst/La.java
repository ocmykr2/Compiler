package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.Assembly.ASMValue.Operand.Symbol;

public class La extends ASMInst {
    public La(Reg rd, Symbol symbol) {
        this.rd = rd;
        this.imm = symbol;
    }

    public String toString() {
        return String.format("la %s, %s", rd, imm);
    }
}
