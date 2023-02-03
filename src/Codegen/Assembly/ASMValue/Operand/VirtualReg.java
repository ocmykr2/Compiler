package Codegen.Assembly.ASMValue.Operand;

public class VirtualReg extends Reg {
    public int spimm = -1;
    public VirtualReg(String id) {
        super(id);
    }

    public VirtualReg() {
        this("tmp");
    }
}
