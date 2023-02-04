package Codegen.IR;
import Codegen.IR.Constant.EmptyConstant;
import Codegen.IR.Constant.IntConstant;
import Codegen.IR.Constant.NullConstant;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Global.BasicBlock;
import Codegen.IR.Value.Global.Function;
import Codegen.IR.Value.Global.Module;
import Codegen.IR.Value.Global.Variable;
import Codegen.IR.Value.Inst.*;
import Semantic.AST.*;
import Codegen.IR.Value.*;
import Codegen.IR.Type.*;
import Semantic.AST.Node.*;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static Codegen.IR.Type.IntType.INT1;
import static Codegen.IR.Type.IntType.INT32;
import static Codegen.IR.Type.PtrType.I32STAR;
import static Codegen.IR.Type.PtrType.I8STAR;
import static Codegen.IR.Type.Type.VOID;
import static Codegen.IR.Value.Inst.BinaryExprInst.OptTable.*;
import static Codegen.IR.Value.Inst.IcmpInst.OptTable.*;

public class IRBuilder extends ASTVisitor {
    Module module;
    ProgramNode programNode;
    BasicBlock curBlock = null;
    Function initFunc = null;
    boolean global = false;

    // Expr.val 对于Func是全局的记录(因为可能有很多),其他的就是这个东西出来的值
    // classFunc type -> FuncType

    public IRBuilder(String id, ProgramNode programNode) {
        module = new Module(id);
        this.programNode = programNode;
        curScope = new IRScope(null, false);
    }

    public Module work() {
        visit(programNode);
        return module;
    }

    void putFunc(FuncDefNode it, StructType curClass) {
        String id =
                (curClass != null) ? (curClass.id + "." + it.id) : (it.id);
        System.out.println("??????????????????????????????????????????????????????????????????????");
        System.out.println(it.return_type);
        System.out.println("??????????????????????????????????????????????????????????????????????");
        Type retType = module.AllClass.get(it.return_type);

        if (it.dimension > 0)
            retType = new PtrType(retType, it.dimension);

        FuncType itFunc = new FuncType(retType);
        if (curClass != null)
            itFunc.belong = (new PtrType(curClass));
        else itFunc.belong = null;

        it.allVar.forEach(i -> {
            Type curType = module.AllClass.get(i.type);
            if (i.dimension > 0)
                curType = new PtrType(curType, i.dimension);
            itFunc.AllPara.add(curType);
        });
        module.AllFunc.put(id, new Function(itFunc, id));
    }

    public Inst putInst(Inst inst, String id) {
        curBlock.addInst(inst);
        inst.id = id;
        return inst;
    }

    public Inst putInst(Inst inst) {
        return putInst(inst, "");
    }

    @Override
    public void visit(ProgramNode it) {
        List<Function> Allbuiltin = Arrays.asList(
                new Function(new FuncType(VOID, I8STAR), "print"),
                new Function(new FuncType(VOID, I8STAR), "println"),
                new Function(new FuncType(VOID, INT32), "printInt"),
                new Function(new FuncType(VOID, INT32), "printlnInt"),
                new Function(new FuncType(I8STAR), "getString"),
                new Function(new FuncType(INT32), "getInt"),
                new Function(new FuncType(I8STAR, INT32), "toString"),
                new Function(new FuncType(I8STAR, INT32), "mx_malloc"),
                new Function(new FuncType(VOID, I8STAR, INT32, INT32), "mx_memset"),
                new Function(new FuncType(INT32, I8STAR, I8STAR), "mx_strcmp"),
                new Function(new FuncType(I8STAR, I8STAR, I8STAR), "mx_strcat"),
                new Function(new FuncType(INT32, I8STAR), "mx_str_length"),
                new Function(new FuncType(I8STAR, I8STAR, INT32, INT32), "mx_str_substring"),
                new Function(new FuncType(INT32, I8STAR), "mx_str_parseInt"),
                new Function(new FuncType(INT32, I8STAR, INT32), "mx_str_ord")
        );

        Allbuiltin.forEach(x -> module.AllFunc.put(x.id, x));

        initFunc = new Function(new FuncType(VOID), "mx.initfunc");
        initFunc.AllBlock.add(new BasicBlock("entry"));
        module.AllFunc.put(initFunc.id, initFunc);

        module.AllClass.put("void", VOID);
        module.AllClass.put("null", new PtrType(VOID));
        module.AllClass.put("bool", INT1);
        module.AllClass.put("int", INT32);
        module.AllClass.put("string", I8STAR);

        // init

        it.allClass.forEach(i -> {
            module.AllClass.put(i.id, new PtrType(new StructType(i.id)));
        });

        it.allClass.forEach(i -> {
            StructType cur = (StructType) ((PtrType)module.AllClass.get(i.id)).oriType;
            i.allVar.forEach(j -> {
                j.allVar.forEach(k -> {
                    Type now = module.AllClass.get(k.type);
                    if(k.dimension > 0)
                        now = new PtrType(now, k.dimension);
                    cur.AllVar.add(now);
                    cur.pos.put(k.id, cur.pos.size());
                });
            });
        });

        it.allClass.forEach(i -> {
            StructType curClass = (StructType) ((PtrType)module.AllClass.get(i.id)).oriType;
            i.allFunc.forEach(j -> {
                    putFunc(j, curClass);
            });
        });

        it.allFunc.forEach(i -> {
            putFunc(i, null);
        });

//        System.out.println(("asdssadasssasas"));

        global = true;
        it.allVar.forEach(i -> {i.accept(this); /*i.allVar.forEach(j -> System.out.println(j.id));*/});
        global = false;
        it.allClass.forEach(i -> i.accept(this));
        // 要么同层能找到,要么必然就是它
        it.allFunc.forEach(i -> i.accept(this));
        global = true;
        //init completed should Add to main
        System.out.println("NearEnd");

        curBlock = initFunc.AllBlock.get(initFunc.AllBlock.size() - 1);
        putInst(new RetInst());
        module.AllFunc.get("main").AllBlock.get(0).Allinst.addFirst((new CallInst(initFunc)));

        int tot = 0;
        for(StrConstant i : module.AllConstant) {
            i.id = i.id + (tot ++);
        }

        // define var.init function
        // define internal void @__cxx_global_var_init() section ".text.startup" {}

        // erase useless inst
        module.AllFunc.forEach((k, v) -> {
            v.AllBlock.forEach(x -> {
                x.Allinst.removeIf(x1 -> (x1.type.basicType != TypeTable.VOID &&
                        !(x1 instanceof CallInst) && x1.UserList.isEmpty()));
                // 就是要去掉我们访问primary时候多出来的冗余Load
            });
        });
    }

    IRScope curScope = null;

    StructType curClass = null;

    Function curFunc = null;

    @Override
    public void visit(ClassDefNode it) {
        curClass = (StructType) ((PtrType)(module.AllClass.get(it.id))).oriType;
        curScope = new IRScope(curScope, true);
        it.allFunc.forEach(i -> {
            i.accept(this);
        });
        curScope = curScope.fa;
        curClass = null;
    }


    public void visit(FuncDefNode it) {
        System.out.println("FuncDef " + it.id);
        String id = it.id;
        if(curClass != null)  {
            id = curClass.id + "." + id;
        }

        Function func = module.AllFunc.get(id);
        FuncType funcType = (FuncType) func.type;

        curFunc = func;

        curBlock = new BasicBlock(id + ".entry");
        func.AllBlock.add(curBlock);
        curScope = new IRScope(curScope, false);

        if(curClass != null) {
            // Store this to be used.
            PtrType belong = funcType.belong;
            Variable var = new Variable(belong, "this");
            func.AllVar.add(var);
            Inst allocathis =
                    putInst(new AllocaInst(belong), "this_ptr");
            putInst(new StoreInst(var, allocathis));
            curScope.add_para("this", allocathis);
        }

        it.allVar.forEach(i -> {
            Type curType = module.AllClass.get(i.type);
            if(i.dimension > 0) {
                curType = new PtrType(curType, i.dimension);
            }
            Variable var = new Variable(curType, i.VarSub.id);
            func.AllVar.add(var);

            // Store the var

            Value allocaVar = putInst(new AllocaInst(curType), i.VarSub.id + "_ptr");
            putInst(new StoreInst(var, allocaVar));
            curScope.add_para(i.VarSub.id, allocaVar);
        });

        System.out.printf("it.stmt == null %s\n", it.id);
        System.out.println(((BlockStmtNode)it.stmt).stmt.size());
        if(it.stmt != null)
        ((BlockStmtNode)it.stmt).stmt.forEach(i -> i.accept(this));

        if(curBlock.Allinst.isEmpty() || !(curBlock.Allinst.getLast() instanceof  RetInst)) {
            if(Objects.equals(it.id, "main")) {
                    putInst(new RetInst(IntConstant.ZERO));
            } else if(it.return_type.equals("void")){
                putInst(new RetInst());
            } else {
                putInst(new UnreachableInst());
            }
        }

        curScope = curScope.fa;
        //System.out.println(curFunc.id);
        curFunc = null; // IMPORTANT!!!!
    }

    @Override
    public void visit(VarDefStmtNode it) {
        it.allVar.forEach(i -> i.accept(this));
    }

    @Override
    public void visit(VarSubDefNode it) {
        System.out.println(it.id);
        System.out.println("-----------------------------------------------------------");
        Type curType = module.AllClass.get(it.type);
        if(it.dimension > 0) {
            curType = new PtrType(curType, it.dimension);
        }
        if(global) {
            Variable ptr = new Variable(new PtrType(curType), it.id, new EmptyConstant(curType));
            module.AllVar.put(it.id, ptr);
            curScope.add_para(it.id, ptr);
            curFunc = initFunc;
            curBlock = initFunc.AllBlock.getLast();
        } else {
            //System.out.println("HERERERE");
            Inst ptr = putInst(new AllocaInst(curType), it.id);
            curScope.add_para(it.id, ptr);
        }

        // deal with init_expr, just add a '='

        if(it.init_expr != null) {
            PrimaryExprNode primaryExprNode = new PrimaryExprNode(null, 0, it.id);
            primaryExprNode.if_func = null;
            AssignExprNode tmp = new AssignExprNode(null, primaryExprNode, it.init_expr);
            visit(tmp);
        }
    }

    public void visit(PrimaryExprNode it) {
        // ???
        System.out.println("Prim : " + it.s + " " + it.whi);
        if(it.whi == 0) {
            if(it.if_func != null) {
                if(curClass != null) {
                    System.out.println("WTF?????");
                    // classFunc : in class choice
                    it.classFunc = module.AllFunc.get
                            (curClass.id + "." + it.s);
                }
                it.val = module.AllFunc.get(it.s);
                return;
            }
            //System.out.println("in0");
            var para = curScope.get_para(it.s, true);
            //System.out.println("out0");
            //System.out.println("in1");
            var flag = curScope.if_class_get(it.s, true);
            if(para == null) {
                System.out.println("FUCK1");
            } else System.out.println(para.id + " asdasd");

            System.out.println("flag " + flag);

            //para.id

            Integer pos = (curClass != null) ? curClass.pos.get(it.s) : null;
            // pos : 有可能在里面
            // 在Class中找到
            if(pos != null && (para == null || flag)) {
                //System.out.println("in2");
                var loadInst =
                        putInst(new LoadInst(curScope.get_para("this", true)), "this.self");
                //System.out.println("out2");
                Inst getInst = putInst(new GetElementPtrInst(loadInst, IntConstant.ZERO, new IntConstant(pos)));
                it.val = putInst(new LoadInst(getInst), "this." + it.s + ".val");
                it.ptr = getInst;
            } else {
                //在全局或者某个函数里
                it.ptr = para;
                //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                System.out.println(it.s + " " + it.whi);
                it.val = putInst(new LoadInst(para), it.s + ".val");
            }
        } else if(it.whi == 1){
            it.val = new IntConstant(Integer.parseInt(it.s));
        } else if(it.whi == 2) {
            StrConstant str = new StrConstant(it.s.substring(1, it.s.length() - 1));
            str.id = ".strconst";
            module.AllConstant.add(str);
            it.val =
                    putInst(new GetElementPtrInst(I8STAR, str, IntConstant.ZERO, IntConstant.ZERO));  // Can be less???
        } else if(it.whi == 3) {
            it.val = IntConstant.TRUE;
        } else if(it.whi == 4) {
            it.val = IntConstant.FALSE;
        } else if(it.whi == 5) {
            it.val = new NullConstant();
        } else {
            System.out.println("in3");
            Value ptr = curScope.get_para("this", true);
            System.out.println("out3");
            it.val = putInst(new LoadInst(ptr), "this.val");
            // why 分开两个,我猜想是两个东西用途不同,要SSA
            it.ptr = putInst(new LoadInst(ptr), "this.self");
        }
    }

    public void visit(ReturnStmtNode it) {
        if(it.expr == null) {
            putInst(new RetInst());
            return;
        }

        it.expr.accept(this);

        //System.out.println(it.expr.type);
        //NullConstant must have type

        if(it.expr.val instanceof NullConstant) {
            it.expr.val.type = curFunc.type;
        }

        putInst(new RetInst(it.expr.val));
    }

    @Override
    public void visit(BlockStmtNode it) {
        curScope = new IRScope(curScope, false);
        //System.out.println(it.stmt.size());
        //System.out.println(it.stmt.get(0).pos);
        it.stmt.forEach(i -> i.accept(this));
        curScope = curScope.fa;
    }

    @Override
    public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }

    @Override
    public void visit(IfStmtNode it) {
        it.cond.accept(this);
        System.out.println("POINT7");
        if(it.Else_Stmt != null) {
            //System.out.println("POINT8");
            //it.Else_Stmt.accept(this);
            //System.out.println("POINT9");
            BasicBlock Ifblock = new BasicBlock("if.if.label");
            BasicBlock Elseblock = new BasicBlock("if.else.label");
            BasicBlock Endblock = new BasicBlock("if.end.label");

            putInst(new BrInst(it.cond.val, Ifblock, Elseblock));

            curFunc.AllBlock.add(Ifblock);
            curBlock = Ifblock;
            it.If_Stmt.accept(this);
            putInst(new BrLabelInst(Endblock));

            curFunc.AllBlock.add(Elseblock);
            curBlock = Elseblock;
            it.Else_Stmt.accept(this);
            putInst(new BrLabelInst(Endblock));

            curFunc.AllBlock.add(Endblock);
            curBlock = Endblock;
        } else {
            BasicBlock Ifblock = new BasicBlock("if.if.label");
            BasicBlock Endblock = new BasicBlock("if.end.label");

            putInst(new BrInst(it.cond.val, Ifblock, Endblock));

            curFunc.AllBlock.add(Ifblock);
            curBlock = Ifblock;
            it.If_Stmt.accept(this);
            putInst(new BrLabelInst(Endblock));

            curFunc.AllBlock.add(Endblock);
            curBlock = Endblock;
        }
    }

    BasicBlock curContdest = null, curBreakdest = null;

    public void visit(CtrlStmtNode it) {
        if(!it.whi) {
            // break;
            putInst(new BrLabelInst(curBreakdest));
        } else {
            putInst(new BrLabelInst(curContdest));
        }
    }

    @Override
    public void visit(WhileStmtNode it) {
        BasicBlock whileCond = new BasicBlock("while.cond.label");
        BasicBlock whileStmt = new BasicBlock("while.stmt.label");
        BasicBlock whileEnd = new BasicBlock("while.end.label");

        putInst(new BrLabelInst(whileCond));
        curBlock = whileCond;
        curFunc.AllBlock.add(whileCond);
        it.cond.accept(this);

        putInst(new BrInst(it.cond.val, whileStmt, whileEnd));

        curBlock = whileStmt;
        curFunc.AllBlock.add(curBlock);
        BasicBlock oldBreakdest = curBreakdest, oldContdest = curContdest;
        curBreakdest = whileEnd;
        curContdest = whileCond;

        if(it.Stmt != null)
            it.Stmt.accept(this);

        curBreakdest = oldBreakdest;
        curContdest = oldContdest;

        putInst(new BrLabelInst(whileCond));

        curBlock = whileEnd;
        curFunc.AllBlock.add(curBlock);
    }

    public void visit(ForStmtNode it) {
        // null !!!
        BasicBlock forCond = new BasicBlock("for.cond.label");
        BasicBlock forStmt = new BasicBlock("for.stmt.label");
        BasicBlock forChg = new BasicBlock("for.chg.label");
        BasicBlock forEnd = new BasicBlock("for.end.label");

        if(it.init != null) it.init.accept(this);
        putInst(new BrLabelInst(forCond));

        curBlock = forCond;
        curFunc.AllBlock.add(forCond);
        if(it.cond != null) {
            it.cond.accept(this);
            putInst(new BrInst(it.cond.val, forStmt, forEnd));
        } else {
            putInst(new BrLabelInst(forStmt));
        }

        curBlock = forStmt;
        curFunc.AllBlock.add(curBlock);
        BasicBlock oldBreakdest = curBreakdest, oldContdest = curContdest;
        curBreakdest = forEnd;
        curContdest = forCond;
        if(it.stmt != null)
            it.stmt.accept(this);
        curBreakdest = oldBreakdest;
        curContdest = oldContdest;

        putInst(new BrLabelInst(forChg));

        curBlock = forChg;
        curFunc.AllBlock.add(curBlock);
        if(it.chg != null)
            it.chg.accept(this);
        putInst(new BrLabelInst(forCond));

        curBlock = forEnd;
        curFunc.AllBlock.add(curBlock);
    }

    @Override
    public void visit(MemberNode it) {
        it.obj.accept(this);

        // Should Determine HERE!!!!

        if(it.if_func != null) {
            if(it.obj.dimension > 0) { // size
                it.val = null;
                it.Begptr = it.obj.val;
            } else if(it.obj.type.equals("string")) {
                String id = "mx_str_" + it.id;
                it.val = module.AllFunc.get(id);
                it.Begptr = it.obj.val;
            } else {
                String id = ((StructType)((PtrType)it.obj.val.type).oriType).id + "." + it.id;
                it.val = module.AllFunc.get(id);
                it.Begptr = it.obj.val;
            }
            return;
        }
        Integer pos = ((StructType)((PtrType)it.obj.val.type).oriType).pos.get(it.id);
        Inst getInst = putInst(new GetElementPtrInst(it.obj.val, IntConstant.ZERO, new IntConstant(pos)));
        it.ptr = getInst;
//        System.out.println("sus");
        it.val = putInst(new LoadInst(getInst), "load." + it.id);
    }

    @Override
    public void visit(CallExprNode it) {
        it.obj.accept(this);

        if(it.obj instanceof MemberNode) {
            if(((MemberNode)it.obj).val == null) {
                Value ptr1 = putInst(new BitCastInst(I32STAR, ((MemberNode)it.obj).Begptr), "ptr_1");
                Value ptr2 = putInst(new GetElementPtrInst(ptr1, IntConstant.NEG_ONE), "ptr_2");
                it.val = putInst(new LoadInst(ptr2), ((MemberNode)it.obj).id + ".size");
                return;
            }
        }

        ArrayList <Value> AllPara = new ArrayList<>();
        it.allExpr.forEach(i -> {
            i.accept(this);
            AllPara.add(i.val);
        });

        Inst finalInst = null;

        if(it.obj instanceof MemberNode tmp) {
            System.out.println("GCDHERE!!!");
            finalInst = new CallInst(it.obj.val);
            finalInst.addUse(tmp.Begptr);
        } else {
//            System.out.println("GCDHERE!!!");
            PrimaryExprNode primaryExprNode = (PrimaryExprNode) it.obj;
            /*if(((PrimaryExprNode) it.obj).classFunc == null) {
                //((PrimaryExprNode) it.obj).classFunc = module.AllFunc.get(((PrimaryExprNode) it.obj).s);
                System.out.println("!!!!!!!!!!!!!!!!!!FUCK!!!!!!!!!!!!!!!!!!!!" + ((PrimaryExprNode) it.obj).s);
            }*/
            FuncType InClass = null;
            if(((PrimaryExprNode) it.obj).classFunc != null)
                    InClass = (FuncType) primaryExprNode.classFunc.type;
            boolean flag = true;
            if(InClass != null) {
                if(InClass.AllPara.size() == AllPara.size()) {
                    for(int i = 0; i < (int) AllPara.size(); ++ i) {
                        flag &= AllPara.get(i).type.equals(InClass.AllPara.get(i));
                    }
                    if(flag) {
                        finalInst = new CallInst(((PrimaryExprNode) it.obj).classFunc);
                        System.out.println("in4");
                        Value ptr = curScope.get_para("this", true);
                        System.out.println("out4");
                        Value loadInst = putInst(new LoadInst(ptr), "this.self");
                        finalInst.addUse(loadInst);
                    }
                } else flag = false;
            } else flag = false;
            if(!flag) {
                finalInst = new CallInst(primaryExprNode.val);
            }
        }

        //System.out.println("HERE");

        for(int i = 0; i < (int) AllPara.size(); ++ i) {
            // FUCK
            if(AllPara.get(i) instanceof NullConstant) {
                AllPara.get(i).type =
                        ((FuncType)it.obj.val.type).AllPara.get(i);
            }
            finalInst.addUse(AllPara.get(i));
        }

        it.val = putInst(finalInst, "__call");
    }

    private static class ForToNClass {
        public Value clk, EndVal;
        public BasicBlock ForCond, ForEnd;

        public ForToNClass(Value clk, Value EndVal, BasicBlock ForCond, BasicBlock ForEnd) {
            this.clk = clk;
            this.EndVal = EndVal;
            this.ForCond = ForCond;
            this.ForEnd = ForEnd;
        }
    }

    private ForToNClass FirstPart(Value N) {
        Inst clk = putInst(new AllocaInst(IntType.INT32), "clock");
        putInst(new StoreInst(IntConstant.ZERO, clk));

        BasicBlock ForCond = new BasicBlock("For.Cond.label"),
                ForStmt = new BasicBlock("For.Stmt.label"),
                ForEnd = new BasicBlock("For.End.label");

        putInst(new BrLabelInst(ForCond));

        curFunc.AllBlock.add(ForCond);
        curBlock = ForCond;
        Inst EndVal = putInst(new LoadInst(clk), "clk.val");
        Inst cmp = putInst(new IcmpInst(IcmpInst.OptTable.slt, EndVal, N), "cmp");
        putInst(new BrInst(cmp, ForStmt, ForEnd));

        curFunc.AllBlock.add(ForStmt);
        curBlock = ForStmt;

        return new ForToNClass(clk, EndVal, ForCond, ForEnd);
    }

    private void ForEnd(ForToNClass param) {
        var clockInc = putInst(new BinaryExprInst(add
                , param.EndVal, IntConstant.ONE), "IncClock");
        putInst(new StoreInst(clockInc, param.clk));
        putInst(new BrLabelInst(param.ForCond));
        // while.end
        curFunc.AllBlock.add(param.ForEnd);
        curBlock = param.ForEnd;
    }

// 每次把char数组强转成别的类型

    public Value NewArray(Type type, int dimension, ArrayList<Value>AllExpr, int pos) {
        IntConstant ClassSize = new IntConstant(dimension > 1 ? 8 : type.getSize());
        Value TotalSize = putInst(new BinaryExprInst(BinaryExprInst.OptTable.mul, ClassSize, AllExpr.get(pos)), "total.size");
        Value ExtraSize = putInst(new BinaryExprInst(add, TotalSize, IntConstant.FOUR), "extra.size");
        Value MallocInst = putInst(new CallInst(module.AllFunc.get("mx_malloc"), ExtraSize), "malloc.ptr");
        Value I32Ptr = putInst(new BitCastInst(I32STAR, MallocInst), "malloc.i32ptr");

        // for size() function
        putInst(new StoreInst(AllExpr.get(pos), I32Ptr));
        Value toplacePtr = putInst(new GetElementPtrInst(I32Ptr, IntConstant.ONE), "toplace.ptr");
        Value retInst = putInst(new BitCastInst(new PtrType(type, dimension), toplacePtr), "final.ptr");

        if(pos < AllExpr.size() - 1) {
            ForToNClass forToNClass = FirstPart(AllExpr.get(pos));
            Value NextArray = NewArray(type, dimension - 1, AllExpr, pos + 1);
            Value placePtr =
                    putInst(new GetElementPtrInst(retInst, forToNClass.EndVal), "place.ptr");
            putInst(new StoreInst(NextArray, placePtr));
            ForEnd(forToNClass);
        }

        return retInst;
    }

    @Override
    public void visit(NvarExprNode it) {
        ArrayList < Value > AllExpr = new ArrayList<>();

        for(ExprNode i : it.allExpr) {
            i.accept(this);
            AllExpr.add(i.val);
        }

        Type curType = module.AllClass.get(it.type);

        if((it.dimension) > 0) {
            it.val = NewArray(curType, it.dimension, AllExpr, 0);
        } else {
            Type base = ((PtrType)curType).getLast();
            //System.out.println(base.basicType.toString());
            //System.out.println("why you are");
            var newSize = new IntConstant(base.getSize());
            var newPtr = putInst(new CallInst(module.AllFunc.get("mx_malloc"), newSize), "new.ptr");
            var bitcastPtr = putInst(new BitCastInst(curType, newPtr), "bitcast.ptr");

//            System.out.println(base.toString() + " Country Road");

            if (base instanceof StructType structType) {
                var cons = module.AllFunc.get(structType.id + "." + structType.id);
                if (cons != null) {
                    putInst(new CallInst(cons, bitcastPtr));
                }
            }
            it.val = bitcastPtr;
        }
    }

    /*
             ++ 1;
             -- 2;
             + 3;
             - 4;
             ! 5;
             ~ 6;
     */
    @Override
    public void visit(PrefExprNode it) {
        it.obj.accept(this);
        if(it.whi == 1) {
            it.val = putInst(new BinaryExprInst(add, it.obj.val, IntConstant.ONE), "res.val");
        } else if(it.whi == 2) {
            it.val = putInst(new BinaryExprInst(BinaryExprInst.OptTable.sub, it.obj.val, IntConstant.ONE), "res.val");
        } else if(it.whi == 3) {
            it.val = putInst(new BinaryExprInst(add, it.obj.val, IntConstant.ZERO), "res.val");
        } else if(it.whi == 4) {
            it.val = putInst(new BinaryExprInst(BinaryExprInst.OptTable.sub, IntConstant.ZERO, it.obj.val), "res.val");
        } else if(it.whi == 5) {
            it.val = putInst(new BinaryExprInst(BinaryExprInst.OptTable.xor, it.obj.val, IntConstant.TRUE), "res.val");
        } else if(it.whi == 6) {
            it.val = putInst(new BinaryExprInst(BinaryExprInst.OptTable.xor, it.obj.val, IntConstant.NEG_ONE), "res.val");
        }

        if(it.whi <= 2) {
            it.ptr = it.obj.ptr;
            putInst(new StoreInst(it.val, it.ptr));
        }
    }

    @Override
    public void visit(SuffExprNode it) {
        // ++ 0, -- 1
        it.obj.accept(this);
        Value tmp = null;
        if(it.whi == 0) {
            tmp = putInst(new BinaryExprInst(add, it.obj.val, IntConstant.ONE), "res.val");
        } else
            tmp = putInst(new BinaryExprInst(BinaryExprInst.OptTable.sub, it.obj.val, IntConstant.ONE), "res.val");
        putInst(new StoreInst(tmp, it.obj.ptr));
        it.val = it.obj.val;
    }

    @Override
    public void visit(EmptyStmtNode it) {
    }

    @Override
    public void visit(AssignExprNode it) {
        it.src2.accept(this);
        System.out.println("Assign src2 Completed");
//        System.out.println(((PrimaryExprNode)it.src1).dimension);
        it.src1.accept(this);
        System.out.println("Assign src1 Completed");

        if(it.src2.type.equals("null")) {
            it.src2.val.type = it.src1.val.type;
        }

        System.out.println("Assign Completed");

        putInst(new StoreInst(it.src2.val, it.src1.ptr));
        it.val = it.src2.val;
    }


    void Shortcircuitcase(BinaryExprNode it) {
        BasicBlock RightExpr = new BasicBlock("right.expr");
        BasicBlock ExprEnd = new BasicBlock("expr.end");
//        System.out.println("POINT");
        it.src1.accept(this);
//        System.out.println("POINT2");
        BasicBlock befBlock = curBlock;

        if(it.whi == 12) {
            putInst(new BrInst(it.src1.val, RightExpr, ExprEnd));
        } else {
            putInst(new BrInst(it.src1.val, ExprEnd, RightExpr));
        }
//        System.out.println("POINT3");
        curFunc.AllBlock.add(RightExpr);
        curBlock = RightExpr;
        it.src2.accept(this);
//        System.out.println("POINT4");
        putInst(new BrLabelInst(ExprEnd));

        curFunc.AllBlock.add(ExprEnd);
        curBlock = ExprEnd;
//        System.out.println("POINT5");

        if(it.whi == 12) {
            it.val = putInst(new PhiInst(new Pair<>(IntConstant.FALSE, befBlock),
                    new Pair<>(it.src2.val, RightExpr)));
//            System.out.println("POINT6");
        } else  {
            it.val = putInst(new PhiInst(new Pair<>(IntConstant.TRUE, befBlock),
                    new Pair<>(it.src2.val, RightExpr)));
        }
    }

    BinaryExprInst.OptTable getINTOp(int whi) {
        return switch(whi) {
            case 1 -> mul;
            case 2 -> sdiv;
            case 3 -> srem;
            case 4 -> add;
            case 5 -> sub;
            case 6 -> shl;
            case 7 -> ashr;
            case 16 -> and;
            case 17 -> xor;
            case 18 -> or;
            default -> null;
        };
    }

    IcmpInst.OptTable getIcmpOp(int whi) {
        return switch(whi) {
            case 8 -> eq;
            case 9 -> ne;
            case 10 -> sle;
            case 11 -> sge;
            case 14 -> slt;
            case 15 -> sgt;
            default -> null;
        };
    }

    boolean truelyString(String Type, int dim) {
        return Type.equals("string") && dim == 0;
    }

    @Override
    public void visit(BinaryExprNode it) {
        //1 *
        //2 /
        //3 %
        // 4 +
        // 5 -
        // 6 <<
        // 7 >>
        // 8 ==
        // 9 !=
        // 10 <=
        // 11 >=
        // 12 &&
        // 13 ||
        // 14 <
        // 15 >
        // 16 &
        // 17 ^
        // 18 |
        if(it.whi == 12 || it.whi == 13) {
            Shortcircuitcase(it);
            return;
        }
        it.src1.accept(this);
        it.src2.accept(this);
        if(getINTOp(it.whi) != null) {
            if(it.whi == 4) {
                if(it.src1.type.equals("string")) {
                    it.val =
                            new CallInst(module.AllFunc.get("mx_strcat"), it.src1.val, it.src2.val);
                } else {
                    it.val = new BinaryExprInst(getINTOp(it.whi), it.src1.val, it.src2.val);
                }
            } else it.val = new BinaryExprInst(getINTOp(it.whi), it.src1.val, it.src2.val);
        } else {
            if(it.whi == 8 || it.whi == 9) {
                boolean src1Null = it.src1.type.equals("null"), src2Null = it.src2.type.equals("null");
                if(src1Null && src2Null) {
                    it.val = (it.whi == 8) ? IntConstant.TRUE : IntConstant.FALSE;
                    return;
                } else if(src1Null) {
                    it.src1.val.type = it.src2.val.type;
                } else if(src2Null) {
                    it.src2.val.type = it.src1.val.type;
                }
                if(truelyString(it.type, it.dimension) || truelyString(it.type, it.dimension)) {
                    Inst callInst =
                            putInst(new CallInst(module.AllFunc.get("mx_strcmp"), it.src1.val, it.src2.val), "result.");
                    it.val = new IcmpInst(getIcmpOp(it.whi), callInst, IntConstant.ZERO);
                } else {
                    //System.out.println("LALALAL " + ((PrimaryExprNode)it.src1).s + " " + ((PrimaryExprNode)it.src2).s);

                    it.val = new IcmpInst(getIcmpOp(it.whi),
                            it.src1.val, it.src2.val);
                }
            } else {
                if(truelyString(it.src1.type, it.src1.dimension)) {
                    Inst callInst =
                            putInst(new CallInst(module.AllFunc.get("mx_strcmp"), it.src1.val, it.src2.val), "result.");
                    it.val = new IcmpInst(getIcmpOp(it.whi), callInst, IntConstant.ZERO);
                } else {
                    //System.out.println("ejghe;lkfjhvldsje;klrhheglearjkfjjfldfsa");
                    it.val = new IcmpInst(getIcmpOp(it.whi), it.src1.val, it.src2.val);
                }
            }
        }
        putInst((Inst)it.val);
    }

    @Override
    public void visit(FuncVarDefNode it) {
        it.VarSub.accept(this);
    }

    @Override
    public void visit(LambdaExprNode it) {
    }

    @Override
    public void visit(BracketExprNode it) {
        it.expr.accept(this);
        it.val = it.expr.val;
        it.ptr = it.expr.ptr;
    }

    @Override
    public void visit(SubscriptExprNode it) {
        it.obj.accept(this);
        it.offset.accept(this);
        it.ptr = putInst(new GetElementPtrInst(it.obj.val, it.offset.val));
//        System.out.println("Sus1");
        it.val = putInst(new LoadInst(it.ptr));
    }
}
