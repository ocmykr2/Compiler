package Codegen.Assembly.ASMInst;

import Codegen.Assembly.ASMValue.Operand.Immediate;
import Codegen.Assembly.ASMValue.Operand.Reg;
public class Arith extends ASMInst {
    public String whi;

    public Arith(String whi, Reg rd, Reg rs1, Reg rs2) {
        this.whi = whi;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
    }

    public Arith(String whi, Reg rd, Reg rs1, Immediate imm) {
        this.whi = whi;
        this.rd = rd;
        this.rs1 = rs1;
        this.imm = imm;
    }

    public Arith(String whi, Reg rd, Reg rs1) {
        this.whi = whi;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = null;
    }

    public String toString() {
        if(rs2 != null) {
            return String.format("%s %s, %s, %s", whi, rd, rs1, rs2);
        } else if(imm != null) {
            return String.format("%si %s, %s, %s", whi, rd, rs1, imm);
        } else {
//            seqz rs, rs1
            return String.format("%s %s, %s", whi, rd, rs1);
        }
    }
}
