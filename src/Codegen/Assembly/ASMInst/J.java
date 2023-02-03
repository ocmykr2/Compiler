package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.ASMBlock;

public class J extends ASMInst {
    public ASMBlock toBlock;
    public J(ASMBlock toBlock) {
        this.toBlock = toBlock;
    }

    public String toString() {
        return String.format("j %s", toBlock);
    }
}
