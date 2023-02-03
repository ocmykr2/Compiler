package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Immediate;
import Codegen.Assembly.ASMValue.Operand.Operand;
import Codegen.Assembly.ASMValue.Operand.Reg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ASMInst {
    public Reg rd = null;
    public Reg rs1 = null, rs2 = null;
    public Operand imm = null; // imm or symbol

    public abstract String toString();

    public ArrayList< Reg > getDefList() {
        if(rd == null)
            return new ArrayList<>();
        else {
            return new ArrayList<>(List.of(rd));
        }
    }

    public ArrayList < Reg > getUseList() {
        if(rs1 == null) {
            return new ArrayList<>();
        } else if(rs2 == null) {
            return new ArrayList<>(List.of(rs1));
        } else return new ArrayList<>(List.of(rs1, rs2));
    }

    public boolean Undetermined() {
        if(imm instanceof Immediate) {
            if(!((Immediate)imm).flag) return true;
        }
        return false;
    }

    public int getImmVal() {
        return ((Immediate) imm).val;
    }

    public void setImmVal(int immval) {
        ((Immediate)imm).val = immval;
        ((Immediate)imm).flag = true;
    }
}
