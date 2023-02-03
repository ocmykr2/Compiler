package Codegen.Assembly.ASMValue;

import Codegen.Assembly.ASMValue.Operand.PhyReg;
import Codegen.Assembly.ASMValue.Operand.VirtualReg;
import org.antlr.v4.runtime.misc.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

public class ASMFunc {
    public String id;
    public int size;
    public int StackSize = 0;
    public LinkedList < ASMBlock > Allblock = new LinkedList<>();

    public ArrayList<Pair<PhyReg, VirtualReg>> savedRegs = new ArrayList<>();

    public ASMFunc(String id, int size) {
        this.id = id;
        this.size = size;
    }

    @Override
    public String toString() {
        return id;
    }
}
