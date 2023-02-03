package Codegen.IR;
import Codegen.IR.Type.FuncType;
import Codegen.IR.Type.PtrType;
import Codegen.IR.Type.StructType;
import Codegen.IR.Constant.EmptyConstant;
import Codegen.IR.Constant.IntConstant;
import Codegen.IR.Constant.NullConstant;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Global.BasicBlock;
import Codegen.IR.Value.Global.Function;
import Codegen.IR.Value.Global.Module;
import Codegen.IR.Value.Global.Variable;
import Codegen.IR.Value.Inst.*;
import Codegen.IR.Value.Value;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IRPrinter extends IRVisitor {
    public PrintStream out;

    HashMap < String, AtomicInteger> globalID = new HashMap<>();
    HashMap < String, AtomicInteger> localID = new HashMap<>();

    boolean inFunc = false;

    public IRPrinter(PrintStream out) {
        this.out = out;
    }

    public int getNameId(String s, HashMap < String, AtomicInteger> map) {
        var id = map.get(s);
        if(id != null) {
            return id.getAndIncrement();
        } else {
            map.put(s, new AtomicInteger(1));
            return 0;
        }
    }

    public String getId(String val, boolean global) {
        var nameId = getNameId(val, global ? globalID : localID);
        return (global ? "@" : "%") + (val.isEmpty() ? "tmp." : val) +
                (nameId == 0 ? "" : String.valueOf(nameId));
    }

    public void getId(Value val, boolean global) {
        val.pid = getId(val.id, global);
    }

    public void getId(StructType val, boolean global) {
        val.pid = getId(val.id, global);
    }

    public void giveName(Module it) {
        it.AllConstant.forEach(i -> getId(i, true));
        it.AllClass.forEach((a, b) -> {
            if(b instanceof PtrType) {
                if(((PtrType)b).oriType instanceof StructType)
                getId((StructType) ((PtrType)b).oriType, false);
            }
        });
        // must be % and we have guarantee the function name have class. so no contradiction.
        localID.clear();
        it.AllVar.forEach((a, b) -> getId(b, true));
        it.AllFunc.forEach((a, b) -> {
            getId(b, true);
            b.AllVar.forEach(i -> getId(i, false));
            b.AllBlock.forEach(i -> {
                getId(i, false);
                i.Allinst.forEach(j -> {
                    if(!j.UserList.isEmpty())
                        getId(j, false);
                });
            });
            localID.clear();
        });
    }

    public void visit(Module it) {
        out.printf("source_filename = \"%s\"\n\n", it.id);
        giveName(it);

        it.AllConstant.forEach(i -> i.accept(this));
        out.println("\n");

        it.AllClass.forEach((a, b) -> {
            if(b instanceof PtrType) {
                if(((PtrType)b).oriType instanceof StructType)
                    visit((StructType) ((PtrType)b).oriType);
            }
        });
        out.println("\n");

        it.AllVar.forEach((a, b) -> visit(b));
        out.println("\n");

        it.AllFunc.forEach((a, b) -> {
            if(b.AllBlock.isEmpty())
                b.accept(this);
        });
        out.println("\n");

        inFunc = true;

        it.AllFunc.forEach((a, b) -> {
            if(!b.AllBlock.isEmpty())
                b.accept(this);
        });
    }

    public void visit(StructType it) {
        out.printf("%s = type { ", it.pid);
        for (int i = 0; i < it.AllVar.size(); ++ i) {
            out.printf("%s", it.AllVar.get(i).toString());
            out.print(i == it.AllVar.size() - 1 ? "" : ",");
        }
        out.printf(" }\n");
    }

    public void visit(IntConstant it) {
        out.printf("%s %d", it.type.toString(), it.val);
    }

    public void visit(StrConstant it) {
        out.printf("%s = private unnamed_addr constant %s c\"%s\"\n", it.pid,
                ((PtrType)it.type).getLast().toString(), it.LLVMString());
    }

    public void visit(NullConstant it) {
        out.printf("%s null", it.type.toString());
    }

    public void visit(EmptyConstant it) {
        out.printf("%s zeroinitializer", it.type.toString());
    }

    public void visit(Variable it) {
        if(!inFunc) {
//            out.println("???????????????????????????????????????????????????????????????????????????????");
            out.printf("%s = global ", it.pid);
            it.initVal.accept(this);
            out.printf("\n");
        } else {
            out.printf("%s %s", it.type.toString(), it.pid);
        }
    }

    public void visit(Function it) {
        if(it.AllBlock.isEmpty()) {
            FuncType funcType = (FuncType) it.type;
            out.printf("declare %s %s(", funcType.retType.toString(), it.pid);
            for(int i = 0; i < funcType.AllPara.size(); ++ i) {
                out.printf("%s", funcType.AllPara.get(i).toString());
                out.printf((i == funcType.AllPara.size() - 1) ? "" : ", ");
            }
            out.printf(")\n");
        } else {
            out.printf("define %s %s(", ((FuncType) it.type).retType.toString(), it.pid);
            for (int i = 0; i < it.AllVar.size(); ++i) {
                it.AllVar.get(i).accept(this);
                out.print(i == it.AllVar.size() - 1 ? "" : ", ");
            }
            out.print(") {\n");
            for (int i = 0; i < it.AllBlock.size(); ++i) {
                it.AllBlock.get(i).accept(this);
                out.print(i == it.AllBlock.size() - 1 ? "" : "\n");
            }
            out.print("}\n\n");
        }
    }

    public void visit(BasicBlock it) {
        out.printf("%s:\n", it.pid.substring(1)); // remove @/%
        it.Allinst.forEach(i -> {
            out.printf("  ");
            i.accept(this);
            out.printf("\n");
        });
    }

    public String getValue(Value val) {
        return val.type.toString() + " " + getobj(val);
    }

    public String getobj(Value val) {
        if(val instanceof EmptyConstant) {
            return "zeroinitializer";
        } else if(val instanceof NullConstant) {
            return "null";
        } else if(val instanceof IntConstant) {
            return String.valueOf(((IntConstant) val).val);
        } else return val.pid;
    }

    public void visit(Inst it) {
        if(!it.UserList.isEmpty()) {
            out.printf("%s = ", it.pid);
        }
        if(it instanceof AllocaInst) {
            out.printf("alloca %s", ((PtrType)it.type).getLast().toString());
        } else if(it instanceof BinaryExprInst tmp) {
            out.printf("%s %s, %s",
                    tmp.whi.toString(), getValue(tmp.getUse(0)), getobj(tmp.getUse(1)));
        } else if(it instanceof IcmpInst tmp) {
            out.printf("icmp %s %s, %s",
                    tmp.whi.toString(), getValue(tmp.getUse(0)), getobj(tmp.getUse(1)));
        } else if(it instanceof BitCastInst tmp) {
            out.printf("bitcast %s to %s", getValue(tmp.getUse(0)), tmp.type.toString());
        } else if(it instanceof BrInst tmp) {
            out.printf("br %s, %s, %s", getValue(tmp.getUse(0)), getValue(tmp.getUse(1)), getValue(tmp.getUse(2)));
        } else if(it instanceof BrLabelInst tmp) {
            out.printf("br %s", getValue(tmp.getUse(0)));
        } else if(it instanceof CallInst tmp) {
            out.printf("call %s %s(", tmp.type.toString(), getobj(tmp.getUse(0)));
            // The first one name of function
            for(int i = 1; i < tmp.OperandList.size(); ++ i) {
                out.printf("%s%s", getValue(tmp.OperandList.get(i).v), (i == tmp.OperandList.size() - 1) ? "" : ",");
            }
            out.printf(")");
        } else if(it instanceof GetElementPtrInst tmp) {
            out.printf("getelementptr %s, %s, ", ((PtrType)tmp.getUse(0).type).getLast().toString(),
                    getValue(tmp.getUse(0)));
            for(int i = 1; i < tmp.OperandList.size(); ++ i) {
                out.printf("%s%s", getValue(tmp.OperandList.get(i).v), (i == tmp.OperandList.size() - 1) ? "" : ",");
            }
        } else if(it instanceof LoadInst tmp) {
            out.printf("load %s, %s", ((PtrType)tmp.getUse(0).type).getLast().toString(), getValue(tmp.getUse(0)));
        } else if(it instanceof StoreInst tmp) {
            out.printf("store %s, %s", getValue(tmp.getUse(0)), getValue(tmp.getUse(1)));
        } else if(it instanceof RetInst tmp) {
            if(tmp.OperandList.isEmpty()) {
                out.printf("ret void");
            } else {
                out.printf("ret %s", getValue(tmp.getUse(0)));
            }
        } else if(it instanceof PhiInst tmp) {
            out.printf("phi %s ", tmp.type.toString());
            for(int i = 0; i < tmp.OperandList.size(); i += 2) {
                out.printf("[ %s, ", getobj(tmp.OperandList.get(i).v));
                // no type!!!
                out.printf("%s ]", getobj(tmp.OperandList.get(i + 1).v));
                out.printf("%s", (i == tmp.OperandList.size() - 2) ? "" : ",");
            }
        } else if(it instanceof  UnreachableInst tmp) {
            out.printf("unreachable");
        }
    }
}
