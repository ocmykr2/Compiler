package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Immediate;
import Codegen.Assembly.ASMValue.Operand.Reg;

// rd = imm (32bit) 直接加载32位立即数

public class Li extends ASMInst {
    public Li(Reg rd, Immediate imm) {
        this.rd = rd;
        this.imm = imm;
    }

    public String toString() {
        return String.format("li %s, %s", rd, imm);
    }
}
