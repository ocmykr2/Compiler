package Codegen.Assembly.ASMValue.Operand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

// ra : 保存返回时的返回地址
// pc : 下一条指令的地址


public class Reg extends Operand {
    public String id;
    public PhyReg color = null;

    public Reg(String id) {
        this.id = id;
    }

    public String toString() {
        return color == null ? id : color.id;
    }

    public boolean equals(Reg another) {
        var c1 = color == null ? this : color;
        var c2 = another.color == null ? another : another.color;
        return c1 == c2;
    }

    public static ArrayList < PhyReg > Allreg = new ArrayList<>();
    public static ArrayList < PhyReg > CallerSavedReg = new ArrayList<>();
    public static ArrayList < PhyReg > CalleeSavedReg = new ArrayList<>();
    public static ArrayList < PhyReg > colorReg = new ArrayList<>();

    public static PhyReg zero, ra, sp, s0, a0;

    public static final List < String > AllregId = Arrays.asList(
            "zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0", "s1", "a0", "a1", "a2", "a3", "a4", "a5",
            "a6", "a7", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6");

    public static final List < String > CalleeSavedRegId = Arrays.asList(
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11");

    public static final List<String> CallerSavedRegId = Arrays.asList(
            "ra", "t0", "t1", "t2", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "t3", "t4", "t5", "t6");

    private static final List<String> colorRegId = Arrays.asList(
            "t0", "t1", "t2", "t3", "t4", "t5", "t6", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "ra");

    public static HashMap < String, PhyReg > IdtoReg = new HashMap<>();

    static {
        AllregId.forEach(i -> {
            PhyReg preg = new PhyReg(i);
            Allreg.add(preg);
            IdtoReg.put(i, preg);
        });
        CalleeSavedRegId.forEach(i -> CalleeSavedReg.add(getReg(i)));
        CallerSavedRegId.forEach(i -> CallerSavedReg.add(getReg(i)));
        colorRegId.forEach(i -> colorReg.add(getReg(i)));

        zero = getReg("zero");
        ra = getReg("ra");
        sp = getReg("sp");
        a0 = getReg("a0");
        s0 = getReg("s0");
    }

    public static PhyReg getReg(String id) {
        return IdtoReg.get(id);
    }
}
