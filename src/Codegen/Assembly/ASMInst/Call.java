package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.ASMFunc;
import Codegen.Assembly.ASMValue.Operand.Reg;
import Codegen.IR.Value.Use;

import java.util.ArrayList;

import static java.lang.StrictMath.min;

public class Call extends ASMInst {
    public ASMFunc func;

    public Call(ASMFunc func) {
        this.func = func;
    }

    @Override
    public ArrayList<Reg> getUseList() {
        ArrayList < Reg > UseList = new ArrayList<Reg>();
        for(int i = 0; i < min(8, func.size - 1); ++ i) {
            UseList.add(Reg.getReg("a" + i));
        }
        return UseList;
    }

    @Override
    public ArrayList<Reg> getDefList() {
        return new ArrayList<Reg>(Reg.CallerSavedReg);
    }

    public String toString() {
        return String.format("call %s", func);
    }
}
