package Codegen.Assembly.ASMValue;


import Codegen.Assembly.ASMInst.*;
import Codegen.Assembly.ASMValue.Operand.Symbol;

import java.util.*;

public class ASMBlock {
    public String id;

    public LinkedList <ASMInst>  AllInst  = new LinkedList<>();

    public static ListIterator < ASMInst > Iterator = null;

    public ASMBlock(String funcId, String id) {
        this.id = "." + funcId + "_" + id;
    }

    public String toString() {
        return id;
    }

    public void addInst(ASMInst inst) {
        if(inst instanceof Sw && inst.imm instanceof Symbol) {
            Lui inst1 = new Lui(inst.rs1, inst.imm);
            if(Iterator == null) {
                AllInst.add(inst1);
            } else {
                Iterator.add(inst1);
            }
        }
        if(Iterator == null) {
            AllInst.add(inst);
        } else {
            Iterator.add(inst);
        }
    }
}
