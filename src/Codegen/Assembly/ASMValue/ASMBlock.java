package Codegen.Assembly.ASMValue;


import Codegen.Assembly.ASMInst.*;
import Codegen.Assembly.ASMValue.Operand.Symbol;

import java.util.*;

public class ASMBlock {
    public String id;

    public ArrayList <ASMBlock> pre = new ArrayList<>(),
            nxt = new ArrayList<>();
    // 请改成单个,如果个数为1的话

    public LinkedList <ASMInst>  AllInst  = new LinkedList<>();

    public static ListIterator < ASMInst > Iterator = null;

    public ASMBlock(String funcId, String id) {
        this.id = "." + funcId + "_" + id;
    }

    public ASMBlock(String funcId) {
        this.id = "." + funcId + "_" + "tmp";
    }

    public static void addLink(ASMBlock from, ASMBlock to) {
        from.nxt.add(to);
        to.pre.add(from);
    }

    public String toString() {
        return id;
    }

    public void addInst(ASMInst inst) {
        if(inst instanceof J) {
            addLink(this, ((J) inst).toBlock);
        } else if(inst instanceof Beqz) {
            addLink(this, ((Beqz) inst).toBlock);
        } else if(inst instanceof Sw && inst.imm instanceof Symbol) {
            // sw rs2, symbol, rs1
            // auipc rs1, %hi(symbol)
            // sw rs2, %lo(symbol)(rs1)
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
