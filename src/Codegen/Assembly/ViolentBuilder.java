package Codegen.Assembly;

import Codegen.Assembly.ASMInst.*;
import Codegen.Assembly.ASMValue.ASMBlock;
import Codegen.Assembly.ASMValue.ASMFunc;
import Codegen.Assembly.ASMValue.ASMRoot;
import Codegen.Assembly.ASMValue.Operand.*;
import Codegen.IR.Constant.Constant;
import Codegen.IR.Constant.IntConstant;
import Codegen.IR.Constant.NullConstant;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.IRVisitor;
import Codegen.IR.Type.PtrType;
import Codegen.IR.Type.StructType;
import Codegen.IR.Type.Type;
import Codegen.IR.Value.Global.BasicBlock;
import Codegen.IR.Value.Global.Function;
import Codegen.IR.Value.Global.Module;
import Codegen.IR.Value.Global.Variable;
import Codegen.IR.Value.Inst.*;
import Codegen.IR.Value.Use;
import Codegen.IR.Value.Value;
import org.antlr.v4.runtime.misc.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import static Codegen.Assembly.ASMValue.Operand.Reg.*;
import static java.lang.StrictMath.max;

public class ViolentBuilder extends IRVisitor  { // Pass
    public ASMRoot root;

    public int OFFSET = 32;

    public Module module;
    public HashMap<Function, ASMFunc> FuncRelation = new HashMap<>();
    public HashMap<BasicBlock, ASMBlock> BlockRelation = new HashMap<>();
    public HashMap<Value, Reg> ValueRelation = new HashMap<>();
    public HashMap<Reg, AtomicInteger> UseCnt = new HashMap<>();

    public int StackSize = 0;

    public ASMFunc curFunc = null;
    public ASMBlock curBlock = null;
    public ASMBlock funcEndBlock = null;


    public ViolentBuilder(Module module) {
        this.module = module;
        root = new ASMRoot(module.id);
    }

    public ASMRoot doit() {
        visit(module);
        return root;
    }

    void addRegUse(Reg reg) {
        AtomicInteger here = UseCnt.get(reg);
        if(here == null) {
            UseCnt.put(reg, new AtomicInteger(1));
        } else {
            here.incrementAndGet();
        }
    }

    public void visit(Module it) {
        it.AllFunc.forEach((a, b) -> {
            ASMFunc asmFunc = new ASMFunc(b.id, b.AllVar.size());
            FuncRelation.put(b, asmFunc);
            if(!b.AllBlock.isEmpty()) {
                root.Allfunc.add(asmFunc);
            }
        });

        root.AllConstant = it.AllConstant;
        root.AllVar = it.AllVar;
        it.AllFunc.forEach((a, b) -> {
            if(!b.AllBlock.isEmpty()) {
                b.accept(this);
            }
        });

        root.Allfunc.forEach(i -> {
            i.Allblock.forEach(j -> {
                j.AllInst.forEach(k -> {
                    if(k.imm != null) {
                        if(k.imm instanceof Immediate kimm) {
                            if(kimm.whom != null && !kimm.flag) {
                                ((Immediate) k.imm).val -= kimm.whom.StackSize;
                                ((Immediate) k.imm).flag = true;
//                                System.out.println("HERE");
                            }
                        }
                    }
                });
            });
        });

/*        root.Allfunc.forEach(i -> {
            UseCnt.clear();
            i.Allblock.forEach(j -> j.AllInst.forEach(k -> {
                if(k.rs1 != null) addRegUse(k.rs1);
                if(k.rs2 != null) addRegUse(k.rs2);
            }));
            for(Iterator<ASMBlock> iter = i.Allblock.descendingIterator(); iter.hasNext();) {
                ASMBlock j = iter.next();
                for(Iterator<ASMInst> Institer = j.AllInst.descendingIterator(); Institer.hasNext();) {
                    ASMInst k = Institer.next();
                    if(k.rd != null && Notused(k.rd)) {
                        Institer.remove();
                        if(k.rs1 != null) delRegUse(k.rs1);
                        if(k.rs2 != null) delRegUse(k.rs2);
                    }
                }
            }
        });*/

        root.Allfunc.forEach(i -> {
            int regCnt = 0;
            int blockCnt = 0;
            for(ASMBlock j : i.Allblock) {
                j.id = j.id + (blockCnt ++);
                for(ASMInst k : j.AllInst) {
                    // Must Figure it out !!!!!!!!!!!!!!!!!!!!!!
                    if(k.rd instanceof VirtualReg && k.rd.id.equals("tmp")) {
//                        k.rd.id = k.rd.id + (regCnt ++);
                        System.out.println("A A A");
                    }
                    if(k.rs1 instanceof VirtualReg && k.rs1.id.equals("tmp")) {
                        //k.rs1.id = k.rs1.id + (regCnt ++);
                        System.out.println("B B B");
                    }
                    if(k.rs2 instanceof VirtualReg && k.rs2.id.equals("tmp")) {
//                        k.rs2.id = k.rs2.id + (regCnt ++);
                        System.out.println("C C C");
                    }
                }
            }
        });
    }

    void FirstIn() {
        for(int i = 0; i < 4; ++ i) {
            curBlock.addInst(new Sw(Reg.getReg("t" + i), Reg.sp, new Immediate(i * 4 + 4)));
        }
    }

    void LastOut() {
        for(int i = 0; i < 4; ++ i) {
            curBlock.addInst(new Lw(Reg.getReg("t" + i), Reg.sp, new Immediate(i * 4 + 4)));
        }
    }

    public void visit(Function it) {
        StackSize = 0;
        ASMFunc asmFunc = FuncRelation.get(it);
        curFunc = asmFunc;

        BlockRelation.clear();
        ValueRelation.clear();

        it.AllBlock.forEach(i -> {
            ASMBlock asmBlock = new ASMBlock(it.id, i.id);
            BlockRelation.put(i, asmBlock);
            asmFunc.Allblock.add(asmBlock);
        });

        curBlock = asmFunc.Allblock.get(0);

        curBlock.addInst(new Arith("add", Reg.sp, Reg.sp, new Immediate()));

        // Callee 在运行被调函数前会被保存，函数返回后这些寄存器与运行被调函数前相同。

        StackSize += 4;
        curBlock.addInst(new Sw(Reg.ra, Reg.sp, new Immediate(0)));

        for(int i = 0; i < 7; ++ i) {
            curBlock.addInst(new Sw(Reg.getReg("t" + i), Reg.sp, new Immediate(StackSize)));
            StackSize += 4;
        }

//        System.out.println(StackSize);


        //Callee finished

        for(int i = 0; i < (int) it.AllVar.size(); ++ i) {
            VirtualReg virtualReg = new VirtualReg();
            virtualReg.spimm = StackSize;
            if(i < 8) {
                Assign(virtualReg, Reg.getReg("a" + i));
            }
            ValueRelation.put(it.AllVar.get(i), virtualReg);
            StackSize += 4;
        }

        funcEndBlock = new ASMBlock(it.id, "end");

        it.AllBlock.forEach(i -> i.accept(this));

        asmFunc.Allblock.add(funcEndBlock);
        curBlock = funcEndBlock;

        // restore saved reg
        curBlock.addInst(new Lw(Reg.ra, Reg.sp, new Immediate(0)));
        for(int i = 0; i < 7; ++ i) {
            curBlock.addInst(new Lw(Reg.getReg("t" + i), Reg.sp, new Immediate(i * 4 + 4)));
        }

        curBlock.addInst(new Arith("add", Reg.sp, Reg.sp, new Immediate(StackSize)));
        curBlock.addInst(new Ret());
        asmFunc.StackSize = StackSize;

        asmFunc.Allblock.forEach(i -> {
            i.AllInst.forEach(j -> {
                if(j.rd == Reg.sp) {
                    if(j.rs1 == Reg.sp) {
                        if(j.Undetermined()) {
                            j.imm = new Immediate(-StackSize);
                        }
                    }
                }
            });
        });
    }
    public Reg getVal(Value val) {
        // 搞清楚是存在哪里的
        Reg reg = null;
        if(val instanceof Constant) {
            if(val instanceof StrConstant) {
                reg = Reg.getReg("t5");
                curBlock.addInst(new La(reg, new Symbol(val.id)));
            } else if(val instanceof NullConstant) {
                reg = Reg.zero;
            } else if(val instanceof IntConstant){
                int v = ((IntConstant) val).val;
                Immediate imm = new Immediate(v);
                reg = Reg.getReg("t" + 5);
                if(v >= -2048 && v < 2048) {
                    curBlock.addInst(new Li(reg, imm));
                } else {
                    curBlock.addInst(new Lui(reg, imm.highbit()));
                    curBlock.addInst(new Arith("add", reg, reg, imm.lowbit()));
                }
            }
        } else {
            reg = ValueRelation.get(val);
            if(reg == null) {
                reg = new VirtualReg();
                ((VirtualReg)reg).spimm = StackSize;
                StackSize += 4;
                ValueRelation.put(val, reg);
            }
        }
        return reg;
    }

    public void visit(BasicBlock it) {
        ASMBlock asmBlock = BlockRelation.get(it);
        curBlock = asmBlock;
        it.Allinst.forEach(i -> i.accept(this));
    }

    public String getRISCVwhi(BinaryExprInst.OptTable it) {
        return switch(it) {
            case add -> "add";
            case sub -> "sub";
            case mul -> "mul";
            case and -> "and";
            case or -> "or";
            case xor -> "xor";
            case sdiv -> "div"; // divide
            case srem -> "rem"; // remainder
            case shl -> "sll"; // (x << y)
            case ashr -> "sra"; // (x >> y)
            default -> null;
        };
    }

    public Immediate getImm(int val) {
        return new Immediate(val, true);
    }

    public void Get(Reg a, Reg b) {
        if(a instanceof VirtualReg) {
            curBlock.addInst(new Lw(b, sp, getImm(((VirtualReg) a).spimm)));
        } else {
            curBlock.addInst(new Mv(b, a));
        }
    }

    public void Assign(Reg a, Reg b) {
        if(a instanceof VirtualReg) {
            curBlock.addInst(new Sw(b, sp, getImm(((VirtualReg) a).spimm)));
        } else System.out.println("Oh my friends are heathens take it slow.");
    }

    public void visit(Inst it) {
        FirstIn();
        Reg t0 = Reg.getReg("t0");
        Reg t1 = Reg.getReg("t1");
        Reg t2 = Reg.getReg("t2");
        Reg t3 = Reg.getReg("t3");

        if(it instanceof AllocaInst) {
            VirtualReg vreg = new VirtualReg();
            vreg.spimm = StackSize; StackSize += 4;
            ValueRelation.put(it, vreg);
        } else if(it instanceof BinaryExprInst) {
            Reg rs1 = getVal(it.getUse(0));
            Get(rs1, t1);

            Reg rs2 = getVal(it.getUse(1));
            Get(rs2, t2);

            Reg rd = getVal(it);
            curBlock.addInst(new Arith(getRISCVwhi(((BinaryExprInst) it).whi), t0, t1, t2));
            System.out.println("Binary");
            Assign(rd, t0);
            System.out.println("Binary");
        } else if(it instanceof IcmpInst) {
            Reg rs1 = getVal(it.getUse(0));
            Get(rs1, t1);
            Reg rs2 = getVal(it.getUse(1));
            Get(rs2, t2);

            Reg rd = getVal(it);
            switch(((IcmpInst) it).whi) {
                case eq -> {
                    curBlock.addInst(new Arith("xor", t0, t1, t2));
                    curBlock.addInst(new Arith("seqz", t0, t0));
                    // seqz : SLTIU rd, rs1, 1 两边无符号,就是看是不是0
                }

                case ne -> {
                    curBlock.addInst(new Arith("xor", t0, t1, t2));
                    curBlock.addInst(new Arith("snez", t0, t0));
                    // snez : seqz的反面
                }

                case sgt -> {
                    curBlock.addInst(new Arith("slt", t0, t2, t1));
                    // slt <
                }

                case sge -> {
                    curBlock.addInst(new Arith("slt", t0, t1, t2));
                    curBlock.addInst(new Arith("xor", t0, t0, new Immediate(1)));
                }

                case slt -> {
                    curBlock.addInst(new Arith("slt", t0, t1, t2));
                }

                case sle -> {
                    curBlock.addInst(new Arith("slt", t0, t2, t1));
                    curBlock.addInst(new Arith("xor", t0, t0, new Immediate(1)));
                }

            }
            System.out.println("icmp");
            Assign(rd, t0);
            System.out.println("icmp");
        } else if(it instanceof BitCastInst) {
//            curBlock.addInst(new Mv(getVal(it), getVal(it.getUse(0))));
            Reg rd = getVal(it);
            Reg rs1 = getVal(it.getUse(0));
            Get(rs1, t1);
            System.out.println("Bitcast");
            curBlock.addInst(new Mv(t0, t1));
            Assign(rd, t0);
            System.out.println("Bitcast");
        } else if(it instanceof BrInst) {
            Reg rs1 = getVal(it.getUse(0));
            Get(rs1, t1);
            curBlock.addInst(new Beqz(t1, BlockRelation.get((BasicBlock)it.getUse(2))));
            curBlock.addInst(new J(BlockRelation.get((BasicBlock) it.getUse(1))));
        } else if(it instanceof BrLabelInst) {
            curBlock.addInst(new J(BlockRelation.get((BasicBlock) it.getUse(0))));
        } else if(it instanceof GetElementPtrInst) {
            Reg lastPlace = getVal((it.OperandList.get(0)).v);
            Get(lastPlace, t0);
            Type lastType = (it.OperandList.get(0)).v.type;
            for(int i = 1; i < it.OperandList.size(); ++ i) {
                Use cur = it.OperandList.get(i);
                if(lastType instanceof PtrType) {
                    lastType = ((PtrType) lastType).getLast();
                    Get(getVal(cur.v), t1);
                    Get(getVal(new IntConstant(lastType.getSize())), t2);
                    curBlock.addInst(new Arith("mul", t3, t1, t2));
                    curBlock.addInst(new Arith("add", t3, t0, t3));
                    curBlock.addInst(new Mv(t0, t3));
                } else if(lastType instanceof StructType) {
                    curBlock.addInst(new Arith("add", t3, t0, new Immediate(((StructType) lastType).getOffset(((IntConstant)cur.v).val))));
                    curBlock.addInst(new Mv(t0, t3));
                }
            }
//            Assign(lastPlace, t0);
            System.out.println("GEP");
            VirtualReg vreg = new VirtualReg();
            vreg.spimm = StackSize;
            StackSize += 4;
            Assign(vreg, t0);
            System.out.println("GEP");
            ValueRelation.put(it, vreg);
        } else if(it instanceof CallInst) {
            ASMFunc toFunc = FuncRelation.get(it.OperandList.get(0).v);
            System.out.println(toFunc.id + " Call");
            for(int i = 1; i < it.OperandList.size(); ++ i) {
                Use cur = it.OperandList.get(i);
                Get(getVal(cur.v), t1);
                if(i < 8) {
                    curBlock.addInst(new Mv(Reg.getReg("a" + (i - 1)), t1));
                    continue;
                }
                curBlock.addInst(new Sw(t1, Reg.sp, new Immediate(OFFSET + (i - 1) * 4, false, toFunc)));
            }
            curBlock.addInst(new Call(toFunc));
            if(!it.type.isVoid()) {
                VirtualReg ret = new VirtualReg();
                ret.spimm = StackSize;
                curBlock.addInst(new Sw(Reg.a0, Reg.sp, new Immediate(StackSize)));
                StackSize += 4;
                // 一般使用a0来存储返回值
                ValueRelation.put(it, ret);
            }
        } else if(it instanceof LoadInst) {
            Value ptr = (it.getUse(0));
            Reg rd = getVal(it);

            // global
            if(ptr instanceof Variable) {
                Symbol imm = new Symbol(ptr.id);
                curBlock.addInst(new Lw(t0, imm));
            } else if(ptr instanceof AllocaInst) {
                Reg rs1 = getVal(ptr);
                Get(rs1, t0);
                //寄存器里
            } else {
                Reg rs1 = getVal(ptr);
                Get(rs1, t1);
                curBlock.addInst(new Lw(t0, t1, new Immediate(0)));
                //内存
            }

            System.out.println("Load");
            Assign(rd, t0);
            System.out.println("Load");
        } else if(it instanceof StoreInst) {
            Value ptr = it.getUse(1);
            Reg rs2 = getVal(it.getUse(0));
            Get(rs2, t2); // value

            if(ptr instanceof Variable) {
                Reg t4 = Reg.getReg("t" + 4);
                Symbol symbol = new Symbol(ptr.id);
                curBlock.addInst(new Sw(t2, t4, symbol));
            } else if (ptr instanceof AllocaInst) {
                Reg rs1 = getVal(it.getUse(1));
                System.out.println("Store");
                Assign(rs1, t2);
                System.out.println("Store");
            } else {
                Reg rs1 = getVal(it.getUse(1));
                Get(rs1, t1);
                curBlock.addInst(new Sw(t2, t1, new Immediate(0)));
            }
        } else if(it instanceof PhiInst) {
            Reg rd = getVal(it);
            //设计中jump都在block的最后,所以可以假设所有inst已经搞过了
            for(Pair < Value, BasicBlock > i : ((PhiInst) it).AllBr) {
                ASMBlock asmBlock = BlockRelation.get(i.b);
                ListIterator <ASMInst> iter = asmBlock.AllInst.listIterator(asmBlock.AllInst.size());
                //先找到最后
                while(iter.hasPrevious()) {
                    ASMInst pre = iter.previous();
                    if((pre instanceof J || pre instanceof Beqz)) {
                        iter.previous();
                        break;
                    }
                }
                if(iter.hasPrevious()) {
                    if(iter.previous() instanceof Beqz) {
                        iter.previous();
                    }
                }

                ASMBlock.Iterator = iter;
                Reg t4 = Reg.getReg("t4");
                Get(getVal(i.a), t4);
                curBlock.addInst(new Mv(t0, t4));
                System.out.println("Phi");
                Assign(rd, t0);
                System.out.println("Phi");
                ASMBlock.Iterator = null;
            }

        } else if(it instanceof RetInst) {
            if(!it.OperandList.isEmpty()) {
//                System.out.println("AIYA");
                Get(getVal(it.getUse(0)), Reg.a0);
//                System.out.println("AIYA");
                //curBlock.addInst(new Mv(Reg.a0, getVal(it.getUse(0))));
            }
            curBlock.addInst(new J(funcEndBlock));
        } else if(it instanceof UnreachableInst) {
            curBlock.addInst(new Ret());
        }
        LastOut();
    }

}
