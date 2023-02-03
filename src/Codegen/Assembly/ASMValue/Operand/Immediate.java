package Codegen.Assembly.ASMValue.Operand;

import Codegen.Assembly.ASMValue.ASMFunc;

public class Immediate extends Operand {
    public int val;

    public boolean flag;

    public ASMFunc whom = null;

    public Immediate(int val, boolean flag) {
        this.val = val;
        this.flag = flag;
    }

    public Immediate(int val, boolean flag, ASMFunc whom) {
        this.val = val;
        this.flag = flag;
        this.whom = whom;
    }

    public Immediate(int val) {
        this.val = val;
        this.flag = true;
    }

    public Immediate() {
        this(0, false);
    }

    public boolean Low() {
        return (val < 2048) && (val >= -2048);
    }

    public Immediate highbit() {
        return new Immediate(val >> 12);
    }

    public Immediate lowbit() {
        return new Immediate(val & (4095));
    }

    @Override
    public String toString() {
        return "" + val;
    }
}
