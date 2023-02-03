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
import java.util.concurrent.atomic.AtomicInteger;

import static Codegen.Assembly.ASMValue.Operand.Reg.getReg;
import static Codegen.Assembly.ASMValue.Operand.Reg.zero;
import static java.lang.StrictMath.max;

public class ASMBuilder extends IRVisitor  { // Pass
    public ASMRoot root;

    public Module module;
    public HashMap<Function, ASMFunc> FuncRelation = new HashMap<>();
    public HashMap<BasicBlock, ASMBlock> BlockRelation = new HashMap<>();
    public HashMap<Value, Reg> ValueRelation = new HashMap<>();
    public HashMap<Reg, AtomicInteger> UseCnt = new HashMap<>();

    public ASMFunc curFunc = null;
    public ASMBlock curBlock = null;
    public ASMBlock funcEndBlock = null;


    public ASMBuilder(Module module) {
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

    void delRegUse(Reg reg) {
        AtomicInteger tmp = UseCnt.get(reg);
        tmp.decrementAndGet();
    }

    boolean Notused(Reg reg) {
        AtomicInteger here = UseCnt.get(reg);
        return reg instanceof VirtualReg && (here == null || here.get() == 0);
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
        });

        root.Allfunc.forEach(i -> {
            int regCnt = 0;
            int blockCnt = 0;
            for(ASMBlock j : i.Allblock) {
                j.id = j.id + (blockCnt ++);
                for(ASMInst k : j.AllInst) {
                    // Must Figure it out !!!!!!!!!!!!!!!!!!!!!!
                    if(k.rd instanceof VirtualReg && k.rd.id.equals("tmp")) {
                        k.rd.id = k.rd.id + (regCnt ++);
                    }
                    if(k.rs1 instanceof VirtualReg && k.rs1.id.equals("tmp")) {
                        k.rs1.id = k.rs1.id + (regCnt ++);
                    }
                    if(k.rs2 instanceof VirtualReg && k.rs2.id.equals("tmp")) {
                        k.rs2.id = k.rs2.id + (regCnt ++);
                    }
                }
            }
        });
    }

    public void visit(Function it) {
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

        Reg.CalleeSavedReg.forEach(i -> {
            VirtualReg vReg = new VirtualReg();
            curBlock.addInst(new Mv(vReg, i));
            asmFunc.savedRegs.add(new Pair<>(i, vReg));
        });

        VirtualReg vRa = new VirtualReg();
        curBlock.addInst(new Mv(vRa, Reg.ra));

        //Callee finished

        int offset = 0;

        for(int i = 0; i < (int) it.AllVar.size(); ++ i) {
            VirtualReg virtualReg = new VirtualReg();
            if(i < 8) {
                curBlock.addInst(new Mv(virtualReg, Reg.getReg("a" + i)));
            } else {
                curBlock.addInst(new Lw(virtualReg, Reg.sp, new Immediate(offset)));
                // push it to the stack.
                offset += 4;
            }
            ValueRelation.put(it.AllVar.get(i), virtualReg);
        }

        funcEndBlock = new ASMBlock(it.id, "end");

        it.AllBlock.forEach(i -> i.accept(this));

        asmFunc.Allblock.add(funcEndBlock);
        curBlock = funcEndBlock;

        // restore saved reg

        asmFunc.savedRegs.forEach(i -> {
            curBlock.addInst(new Mv(i.a, i.b));
        });
        curBlock.addInst(new Arith("add", Reg.sp, Reg.sp, new Immediate()));
        curBlock.addInst(new Ret());
    }

    public Reg getVal(Value val) {
        // 搞清楚是存在哪里的
        Reg reg = null;
        if(val instanceof Constant) {
            if(val instanceof StrConstant) {
                reg = new VirtualReg();
                curBlock.addInst(new La(reg, new Symbol(val.id)));
            } else if(val instanceof NullConstant) {
                reg = Reg.zero;
            } else if(val instanceof IntConstant){
                int v = ((IntConstant) val).val;
                Immediate imm = new Immediate(v);
                reg = new VirtualReg();
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

    public void visit(Inst it) {
        if(it instanceof AllocaInst) {
            VirtualReg vreg = new VirtualReg();
            ValueRelation.put(it, vreg);
        } else if(it instanceof BinaryExprInst) {
            Reg rs1 = getVal(it.getUse(0));
            Reg rs2 = getVal(it.getUse(1));
            Reg rd = getVal(it);
            curBlock.addInst(new Arith(getRISCVwhi(((BinaryExprInst) it).whi), rd, rs1, rs2));
        } else if(it instanceof IcmpInst) {
            Reg rs1 = getVal(it.getUse(0));
            Reg rs2 = getVal(it.getUse(1));
            Reg rd = getVal(it);
            switch(((IcmpInst) it).whi) {
                case eq -> {
                    curBlock.addInst(new Arith("xor", rd, rs1, rs2));
                    curBlock.addInst(new Arith("seqz", rd, rd));
                    // seqz : SLTIU rd, rs1, 1 两边无符号,就是看是不是0
                }

                case ne -> {
                    curBlock.addInst(new Arith("xor", rd, rs1, rs2));
                    curBlock.addInst(new Arith("snez", rd, rd));
                    // snez : seqz的反面
                }

                case sgt -> {
                    curBlock.addInst(new Arith("slt", rd, rs2, rs1));
                    // slt <
                }

                case sge -> {
                    curBlock.addInst(new Arith("slt", rd, rs1, rs2));
                    curBlock.addInst(new Arith("xor", rd, rd, new Immediate(1)));
                }

                case slt -> {
                    curBlock.addInst(new Arith("slt", rd, rs1, rs2));
                }

                case sle -> {
                    curBlock.addInst(new Arith("slt", rd, rs2, rs1));
                    curBlock.addInst(new Arith("xor", rd, rd, new Immediate(1)));
                }
            }
        } else if(it instanceof BitCastInst) {
            curBlock.addInst(new Mv(getVal(it), getVal(it.getUse(0))));
        } else if(it instanceof BrInst) {
            curBlock.addInst(new Beqz(getVal(it.getUse(0)), BlockRelation.get((BasicBlock)it.getUse(2))));
            curBlock.addInst(new J(BlockRelation.get((BasicBlock) it.getUse(1))));
        } else if(it instanceof BrLabelInst) {
            curBlock.addInst(new J(BlockRelation.get((BasicBlock) it.getUse(0))));
        } else if(it instanceof GetElementPtrInst) {
            Reg lastPlace = getVal((it.OperandList.get(0)).v);
            Type lastType = (it.OperandList.get(0)).v.type;
            for(int i = 1; i < it.OperandList.size(); ++ i) {
                Use cur = it.OperandList.get(i);
                if(lastType instanceof PtrType) {
                    lastType = ((PtrType) lastType).getLast();
                    VirtualReg vreg = new VirtualReg();
                    curBlock.addInst(new Arith("mul", vreg, getVal(cur.v), getVal(new IntConstant(lastType.getSize()))));
                    curBlock.addInst(new Arith("add", vreg, lastPlace, vreg));
                    lastPlace = vreg;
                } else if(lastType instanceof StructType) {
                    VirtualReg vreg = new VirtualReg();
                    curBlock.addInst(new Arith("add", vreg, lastPlace, new Immediate(((StructType) lastType).getOffset(((IntConstant)cur.v).val))));
                    lastPlace = vreg;
                }
            }
            ValueRelation.put(it, lastPlace);
        } else if(it instanceof CallInst) {
            ASMFunc toFunc = FuncRelation.get(it.OperandList.get(0).v);
            for(int i = 1; i < it.OperandList.size(); ++ i) {
                Use cur = it.OperandList.get(i);
                if(i <= 8) {
                    curBlock.addInst(new Mv(Reg.getReg("a" + (i - 1)), getVal(cur.v)));
                } else {
                    curBlock.addInst(new Sw(getVal(cur.v), Reg.sp, new Immediate((9 - i) * 4)));
                }
            }
            curBlock.addInst(new Call(toFunc));
            if(!it.type.isVoid()) {
                VirtualReg ret = new VirtualReg();
                curBlock.addInst(new Mv(ret, Reg.a0));
                // 一般使用a0来存储返回值
                ValueRelation.put(it, ret);
            }
            root.StackSize = max(root.StackSize, (it.OperandList.size() - 8) * 4);
        } else if(it instanceof LoadInst) {
            Value ptr = (it.getUse(0));
            Reg rd = getVal(it);

            // global
            if(ptr instanceof Variable) {
                Symbol imm = new Symbol(ptr.id);
                curBlock.addInst(new Lw(rd, imm));
            } else if(ptr instanceof AllocaInst) {
                Reg rs1 = getVal(ptr);
                curBlock.addInst(new Mv(rd, rs1));
                //寄存器里
            } else {
                Reg rs1 = getVal(ptr);
                curBlock.addInst(new Lw(rd, rs1, new Immediate(0)));
                //内存
            }
        } else if(it instanceof StoreInst) {
            Value ptr = it.getUse(1);
            Reg rs2 = getVal(it.getUse(0));

            if(ptr instanceof Variable) {
                Symbol symbol = new Symbol(ptr.id);
                Reg vreg = new VirtualReg();
                curBlock.addInst(new Sw(rs2, vreg, symbol));
            } else if (ptr instanceof AllocaInst) {
                Reg rs1 = getVal(it.getUse(1));
                curBlock.addInst(new Mv(rs1, rs2));
            } else {
                Reg rs1 = getVal(it.getUse(1));
                curBlock.addInst(new Sw(rs2, rs1, new Immediate(0)));
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
                    if(!(pre instanceof J || pre instanceof Beqz)) {
                        iter.next();
                        break;
                    }
                }
                ASMBlock.Iterator = iter;
                curBlock.addInst(new Mv(rd, getVal(i.a)));
                ASMBlock.Iterator = null;
            }

        } else if(it instanceof RetInst) {
            if(!it.OperandList.isEmpty()) {
                curBlock.addInst(new Mv(Reg.a0, getVal(it.getUse(0))));
            }
            curBlock.addInst(new J(funcEndBlock));
        } else if(it instanceof UnreachableInst) {
            curBlock.addInst(new Ret());
        }
    }

}
