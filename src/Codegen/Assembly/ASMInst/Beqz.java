package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.ASMBlock;
import Codegen.Assembly.ASMValue.Operand.Reg;

public class Beqz extends ASMInst {
    public ASMBlock toBlock;
    public Beqz(Reg rs1, ASMBlock toBlock) {
        this.rs1 = rs1;
        this.toBlock = toBlock;
    }

    @Override
    public String toString() {
        // beqz rs1, block rs1=0就跳
        return String.format("beqz %s, %s", rs1, toBlock);
    }
}
