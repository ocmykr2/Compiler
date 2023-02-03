// Without IR
// Bracket?
package Checker;

import Semantic.AST.ASTVisitor;
import Utils.position;
import Utils.Error_.SemanticError;
import  Semantic.AST.Node.*;

import java.util.ArrayList;

public class SemanticChecker extends ASTVisitor {
    public Symbols symbols;
    public Scope curScope;
    public ClassDefNode curClass = null;
    public ArrayList<FuncDefNode> curFunc = new ArrayList<>();
    public int loops = 0;

    public SemanticChecker(Symbols symbols) {
        this.symbols = symbols;
        this.curScope = new Scope(null);
    }

    public FuncDefNode last_func() {
        return curFunc.get(curFunc.size() - 1);
    }

    public void visit(ProgramNode it) {
        it.allVar.forEach(i -> i.accept(this));
        it.allClass.forEach(i -> i.accept(this));
        Boolean have_main = false;
        for (FuncDefNode func : it.allFunc) {
            if(!func.id.equals("main"))
                continue;
            if (have_main == true)
                throw new SemanticError(func.pos, "There are multiple mains!");
            have_main = true;
            if (!func.return_type.equals("int")) {
                throw new SemanticError(func.pos, "The main function should have the type of int");
            }
            if (func.allVar.size() != 0) {
                throw new SemanticError(func.pos, "The main function should have no parameters");
            }
        }
        if (have_main == false) {
            throw new SemanticError(it.pos, "There is no main function");
        }
        it.allFunc.forEach(i -> i.accept(this));
    }

    public void visit(ClassDefNode it) {
        curClass = it;
        curScope = new Scope(curScope);
        it.allVar.forEach(i -> i.accept(this));
        it.allFunc.forEach(i -> i.accept(this));
//        System.out.println(it.allFunc.get(0).return_type + " asdasdasdasdasddddddddddddddddddddddddddddddddddddasdasdasdasd");
        curScope = curScope.fa;
        curClass = null;
    }

    public void visit(FuncDefNode it) {
        System.out.println("FuncDef!!!");
        System.out.println(it.id);
        System.out.println(it.return_type);
        if(symbols.getClass(it.return_type) == null) {
            throw new SemanticError(it.pos, "There is no Class defined named " + it.return_type);
        }

        curScope = new Scope(curScope);
        curFunc.add(it);
        //System.out.println("SDASDASD");
        //it.allVar.forEach(x -> System.out.println(x.VarSub.id));
        it.allVar.forEach(x -> x.accept(this));

        if(it.stmt != null) {
            //System.out.println("STMT!!!");
            it.stmt.accept(this);
        }

        if(!it.return_type.equals("void") && !it.id.equals("main") && it.returned == false) {
            if(curClass == null || !curClass.id.equals(last_func().id)) {
                throw new SemanticError(it.pos, "function " + it.id + " has no return");
            }
        }

        it.returned = false;
        it.belong = curClass;
        if(curClass != null && curClass.id.equals(curFunc.get(curFunc.size() - 1).id))
            curClass.Construct = it;

        curFunc.remove(curFunc.size() - 1);
        curScope = curScope.fa;
    }

    public void visit(ReturnStmtNode it) {
//        System.out.println("WHy HERE!!!!");
        if(it.expr != null) {
            it.expr.accept(this);
            if(last_func().return_type != null) {

                /*System.out.println("Impbegin");
                System.out.println(last_func().id);
                System.out.println(last_func().return_type);
                System.out.println(it.expr.type);
                System.out.println(it.expr.pos.toString());
                System.out.println("Impend");*/


                if(curClass != null && last_func().id.equals(curClass.id)) {
                    throw new SemanticError(it.pos, "The constructor function should have no return statement.");
                }
                if(!it.expr.type.equals("null") && !it.expr.type.equals(last_func().return_type))
                    throw new SemanticError(it.pos, "The return types do not match!");
                if(!it.expr.type.equals("null") && it.expr.dimension != last_func().dimension) {
                    throw new SemanticError(it.pos, "The return dimensions do not match!");
                }
            } else {
//                System.out.println("Last Func" + last_func().id + " " + last_func().return_type);
                last_func().return_type = it.expr.type;
                last_func().dimension = it.expr.dimension;
            }
        } else {
            if(last_func().return_type != null) {
                if(!last_func().return_type.equals("void")) {
                    if(curClass == null || !curClass.id.equals(last_func().id)) {
                        throw new SemanticError(it.pos, "There is no return expression.");
                    }
                }
            } else {
//                System.out.println("Last Func" + last_func().id + " " + last_func().return_type);
                last_func().return_type = "null";
            }
        }
        last_func().returned = true;
    }

    public void visit(FuncVarDefNode it) {
        if(symbols.getClass(it.type) == null) {
            throw new SemanticError(it.pos, "There is no type named " + it.type);
        }
        it.VarSub.accept(this);
        it.VarSub.type = it.type;
        it.VarSub.dimension = it.dimension;
    }

    public void visit(VarDefStmtNode it) {
//        System.out.println("HERE AT VarDefStmt");
        if(symbols.getClass(it.type) == null) {
            throw new SemanticError(it.pos, "There is no type named " + it.type);
        }

        for(VarSubDefNode i : it.allVar) {
            i.type = it.type;
            i.dimension = it.dimension;
            i.accept(this);
//            System.out.println(i.pos.toString());
//            System.out.println(i.type);
//            System.out.println(i.id);
//            System.out.println("asdasdasdasd");
        }
    }

    public void visit(VarSubDefNode it) {
        //System.out.println("WHY!!!");
        //if(!symbols.classDefined())
        if(symbols.classDefined(it.id)) {
            throw new SemanticError(it.pos, it.id + " has already been defined.");
        }

        if(curScope.get_var(it.id, false) != null) {
            throw new SemanticError(it.pos, it.id + " has already been defined.");
        }

        if(it.init_expr !=  null) {
            it.init_expr.accept(this);
        }

        System.out.println("BEGIN");
        System.out.println(it.type);
        System.out.println(it.id);
        System.out.println(it.pos.toString());
        System.out.println("END");

        curScope.add(it.id, it);
    }

    public void visit(BlockStmtNode it) {
        curScope = new Scope(curScope);
        it.stmt.forEach(x -> x.accept(this));
        curScope = curScope.fa;
    }

    public void visit(SubscriptExprNode it) {
        it.obj.accept(this);
        if(it.obj.is_left_val == false)
            if(it.obj.if_class != null || (it.obj.is_left_val == false && it.obj.if_func != null)) {
                throw new SemanticError(it.obj.pos, "[] is not supposed to appear after functions or classes.");
            }

        if(it.obj.tobe_left_val == true) {
            throw new SemanticError(it.obj.pos, "[] is not supposed to use in new grammar.");
        }

        if(it.obj.dimension == 0)
            throw new SemanticError(it.obj.pos, "There are no dimensions.");

        it.offset.accept(this);

        if(!it.offset.type.equals("int") || it.offset.dimension != 0) {
            throw new SemanticError(it.obj.pos, "Wrong offset type!");
        }

        it.type = it.obj.type;
        it.dimension = it.obj.dimension - 1;
        it.is_left_val = it.obj.is_left_val;
    }

    public void visit(AssignExprNode it) {
        it.src1.accept(this);
        if(it.src1.is_left_val == false)
            throw new SemanticError(it.src1.pos, "Src1 is not a left val");
        it.src2.accept(this);
        if (it.src2.if_class != null)
            throw new SemanticError(it.src2.pos, "Src2 is a class");
        if(it.src2.is_left_val == false && it.src2.if_func != null)
            throw new SemanticError(it.src2.pos, "Src2 is a function");
        if(!it.src2.type.equals("null") && !it.src1.type.equals(it.src2.type)) {
            throw new SemanticError(it.pos, "Src1's type and src2's type don't match");
        }

        System.out.println(it.src1.type);
        System.out.println(it.src1.pos.toString());
        System.out.println(it.src1.dimension);
        System.out.println(it.src2.type);
        System.out.println(it.src2.pos.toString());
        System.out.println(it.src2.dimension);

        if(!it.src2.type.equals("null") && it.src1.dimension != it.src2.dimension) {
            throw new SemanticError(it.pos, "Src1's dimension and src2's dimension don't match");
        }
        if(it.src1.dimension == 0) {
            if(it.src1.type.equals("int") && it.src2.type.equals("null")) {
                throw new SemanticError(it.pos, "The type of src1 is int while src2 is null.");
            }
            if(it.src1.type.equals("bool") && it.src2.type.equals("null")) {
                throw new SemanticError(it.pos, "The type of src1 is bool while src2 is null.");
            }
            if(it.src1.type.equals("string") && it.src2.type.equals("null")) {
                throw new SemanticError(it.pos, "The type of src1 is string while src2 is null.");
            }
            if(it.src1.type.equals("void") && it.src2.type.equals("null")) {
                throw new SemanticError(it.pos, "The type of src1 is void while src2 is null.");
            }
        }
        it.type = it.src1.type;
        it.dimension = it.src1.dimension;
        it.is_left_val = true;
    }

    public String Number_to_Op(int t) {
        if(t == 1) return "*";
        else if(t == 2) return "/";
        else if(t == 3) return "%";
        else if(t == 4) return "+";
        else if(t == 5) return "-";
        else if(t == 14) return "<";
        else if(t == 15) return ">";
        else if(t == 16) return "&";
        else if(t == 17) return "^";
        else if(t == 18) return "|";
        else if(t == 6) return "<<";
        else if(t == 7) return ">>";
        else if(t == 8) return "==";
        else if(t == 9) return "!=";
        else if(t == 10) return "<=";
        else if(t == 11) return ">=";
        else if(t == 12) return "&&";
        else if(t == 13) return "||";
        return null;
    }

    public int Op_to_Number(String t) {
        if(t == "x") return 1;
        else if(t == "/") return 2;
        else if(t == "%") return 3;
        else if(t == "+") return 4;
        else if(t == "-") return 5;
        else if(t == "<") return 14;
        else if(t == ">") return 15;
        else if(t == "&") return 16;
        else if(t == "^") return 17;
        else if(t == "|") return 18;
        else if(t == "<<") return 6;
        else if(t == ">>") return 7;
        else if(t == "==") return 8;
        else if(t == "!=") return 9;
        else if(t == "<=") return 10;
        else if(t == ">=") return 11;
        else if(t == "&&") return 12;
        else if(t == "||") return 13;
        return -1;
    }

    public void visit(BinaryExprNode it) {
        it.src1.accept(this);
        if(it.src1.if_class != null)
            throw new SemanticError(it.src1.pos, "Src1 is a class");
        if(it.src1.is_left_val == false && it.src1.if_func != null){
            throw new SemanticError(it.src1.pos, "Src1 is a function");
        }

        it.src2.accept(this);
        if(it.src2.if_class != null)
            throw new SemanticError(it.src2.pos, "Src1 is a class");
        if(it.src2.is_left_val == false && it.src2.if_func != null) {
            throw new SemanticError(it.src2.pos, "Src2 is a function");
        }

        if(it.whi == 8 || it.whi == 9) {
            if(!it.src1.type.equals(it.src2.type) && !it.src2.type.equals("null"))
                throw new SemanticError(it.pos, "Src1's type and src2's type don't match");
            if(it.src1.dimension != it.src2.dimension && !it.src2.type.equals("null"))
                throw new SemanticError(it.pos, "Src1's dimension and src2's dimension don't match");
            it.type = "bool";
        } else {
            if(!it.src1.type.equals(it.src2.type))
                throw new SemanticError(it.pos, "Src1's type and src2's type don't match");
            if(it.src1.dimension != it.src2.dimension && !it.src2.type.equals("null"))
                throw new SemanticError(it.pos, "Src1's dimension and src2's dimension don't match");
            if(it.src1.type.equals("bool")) {
                if((it.whi == 14) || (it.whi == 15) || (it.whi == 10) || (it.whi == 11)) {
                    if(it.whi == 14) throw new SemanticError(it.pos,"Bool can't be used in <");
                    else if(it.whi == 15) throw new SemanticError(it.pos,"Bool can't be used in >");
                    else if(it.whi == 10) throw new SemanticError(it.pos,"Bool can't be used in <=");
                    else if(it.whi == 11) throw new SemanticError(it.pos,"Bool can't be used in >=");
                }
            } else if(it.src1.type.equals("string")) {
//                System.out.println(it.whi);
                if(it.whi != 4 && it.whi != 14 && it.whi != 15 && it.whi != 10 && it.whi != 11 && it.whi != 8 && it.whi != 9) {
                    throw new SemanticError(it.pos, "String can't be used in " + Number_to_Op(it.whi));
                }
            } else if(!it.src1.type.equals("int")) {
                throw new SemanticError(it.pos, it.src1.type + " can't be used in " + Number_to_Op(it.whi));
            }

            if((it.whi == 14) || (it.whi == 15) || (it.whi == 10) || (it.whi == 11))
                it.type = "bool";
            else it.type = it.src1.type;
            it.dimension = it.src1.dimension;
        }
    }

    public void visit(BracketExprNode it) {
        it.expr.accept(this);

        it.type = it.expr.type;
        it.dimension = it.expr.dimension;
        it.is_left_val = it.expr.is_left_val;
        if(it.expr.tobe_left_val == true)
            it.is_left_val = true;
        else {
            if(it.expr.if_class != null) {
                throw new SemanticError(it.pos, "The Class is lack of constructor");
            }
            if(it.expr.is_left_val == false && it.expr.if_func != null) {
                throw new SemanticError(it.pos, "The Function is lack of call");
            }
        }
    }

    public void visit(CallExprNode it) {
           it.obj.accept(this);
           if(it.obj.if_class == null && it.obj.if_func == null)
               throw new SemanticError(it.pos, "There is no such named function or class");
           FuncDefNode f = null;
           if(it.obj.if_class != null)
               f = it.obj.if_class.allFunc.get(0);
           else
               f = it.obj.if_func;
           if(f.allVar.size() != it.allExpr.size()) {
               throw new SemanticError(it.pos, "Too many or too little parameters");
           }
           it.func = f;
           it.allExpr.forEach(i -> i.accept(this));
           for(int i = 0; i < f.allVar.size(); ++ i) {
               if(it.allExpr.get(i).if_class != null) {
                   throw new SemanticError(it.allExpr.get(i).pos, i + "th parameter"+" is a class");
               }

               if(it.allExpr.get(i).is_left_val == false && it.allExpr.get(i).if_func != null) {
                    throw new SemanticError(it.allExpr.get(i).pos, i + "th parameter"+" is a function");
               }
               if(!it.allExpr.get(i).type.equals("null") && !f.allVar.get(i).type.equals(it.allExpr.get(i).type)) {
                   throw new SemanticError(it.allExpr.get(i).pos, i + "th parameter"+" has a wrong type");
               }
               if(!it.allExpr.get(i).type.equals("null")&& f.allVar.get(i).dimension != it.allExpr.get(i).dimension) {
                   throw new SemanticError(it.allExpr.get(i).pos, i + "th parameter"+" has a wrong number of dimensions");
               }
           }

           if(it.obj.if_class != null) {
               it.type = it.obj.if_class.id;
               it.is_left_val  = true;
           } else {
               it.type = it.obj.if_func.return_type;
               it.dimension = it.obj.if_func.dimension;
           }
    }
    public void visit(NvarExprNode it) {
        System.out.println(it.type);
        System.out.println("Nvar");
        it._Class = symbols.getClass(it.type);
        if(it._Class == null)
            throw new SemanticError(it.pos, "no such type " + it.type);

        for(ExprNode x : it.allExpr) {
            x.accept(this);
            if((x.is_left_val == false && x.if_func != null) || x.if_class != null || x.dimension != 0 || !x.type.equals("int")) {
                throw new SemanticError(x.pos, "Wrong type for the size of the array");
            }
        }

        it.tobe_left_val = true;
    }

    public void visit(MemberNode it) {
        it.obj.accept(this);
        if(it.obj.if_class != null || (it.obj.is_left_val == false && it.obj.if_func != null))
            throw new SemanticError(it.pos, "The left-side of . must not be class or function");
        //if(it.obj.type != null)
        //System.out.println(it.obj.type);
        ClassDefNode _Class = symbols.getClass(it.obj.type);
        if(it.obj.dimension == 0) {
            if(_Class.VarMap.containsKey(it.id)) {
                it.Node_In_Def = _Class.VarMap.get(it.id);
                it.type = it.Node_In_Def.type;
                it.dimension = it.Node_In_Def.dimension;
                it.is_left_val = true;
            } else if(_Class.FuncMap.containsKey(it.id)) {
                it.if_func = _Class.FuncMap.get(it.id);
                it.type = it.if_func.return_type;
                it.dimension = it.if_func.dimension;
            } else
                throw new SemanticError(it.pos, "No such member");
        } else if(it.id.equals("size")) {
            it.if_func = symbols.getFunc("__builtin_size");
            it.type = "int";
        } else
            throw new SemanticError(it.pos, "Array only has size() method");
    }

    public void visit(PrefExprNode it) {
        it.obj.accept(this);
        if(it.whi <= 4 && !it.obj.type.equals("int")) {
            new SemanticError(it.pos, "Wrong type, the correct type should be int");
        }
        if(it.whi >= 5 && !it.obj.type.equals("bool")) {
            new SemanticError(it.pos, "Wrong type, the correct type should be bool");
        }

        if(it.whi <= 2) {
            if(it.obj.is_left_val == false)
                throw new SemanticError(it.pos, "Can't perform because the object is not a left-val");
            it.is_left_val = true;
        }
        it.type = it.obj.type;
        it.dimension = it.obj.dimension;
    }

    public void visit(SuffExprNode it) {
        it.obj.accept(this);
        if(it.obj.is_left_val == false)
            throw new SemanticError(it.pos, "Can't perform because the object is not a left-val");
        if(!it.obj.type.equals("int"))
            throw new SemanticError(it.pos, "Wrong type, the correct type should be int");
        it.type = "int";
        it.dimension = it.obj.dimension;
    }

    Boolean cmp(position a, position b) {
        if(a.row == b.row)
            return a.col < b.col;
        else return a.row < b.row;
    }

    public void visit(PrimaryExprNode it) {
        // 0 Identifier 1 number_const 2 string_const 3 true 4 false 5 null 6 this
        if(it.whi == 0) {
            VarSubDefNode def = curScope.get_var(it.s, true);
            /*System.out.println("------------------------------------------------------------------------");
            System.out.println(def.type);
            System.out.println(it.pos.toString());
            System.out.println(def.pos.toString());
            System.out.println(cmp(it.pos, def.pos));
            System.out.println("------------------------------------------------------------------------");
           */


            if(def != null && cmp(it.pos, def.pos)) {
                def = null;
                //System.out.println("FUCK YOU!");
            }
            if(curClass != null) {
                VarSubDefNode def_class = curClass.VarMap.containsKey(it.s) ? curClass.VarMap.get(it.s) : null;
                if(def == null) def = def_class;
                else if(def_class != null && cmp(def.pos, curClass.pos))
                    def = def_class;
            }

            it.Node_In_Def = def;

        /*    System.out.println("------------------------------------------------------------------------");
            System.out.println(def.type);
            System.out.println(it.pos.toString());
            System.out.println(def.pos.toString());
            System.out.println(cmp(it.pos, def.pos));
            System.out.println("------------------------------------------------------------------------");
           */


            FuncDefNode f = symbols.getFunc(it.s);
            if(curClass != null && curClass.FuncMap.containsKey(it.s))
                f = curClass.FuncMap.get(it.s);

            ClassDefNode cls = symbols.getClass(it.s);
            if(def != null && f != null) {

                // there may be two both ok, to dertermine use the next one.

                it.type = def.type;
                it.dimension = def.dimension;
                it.is_left_val = true;
                it.if_func = f;
            } else if(def != null) {
                it.type = def.type;
                it.dimension = def.dimension;
                it.is_left_val = true;
            } else if(cls != null) {
                it.type = cls.id;
                it.tobe_left_val = true;
                it.if_class = cls;
            } else if(f != null) {
                it.type = f.return_type;
                it.if_func = f;
            } else
                throw new SemanticError(it.pos, "no Identifier defined as " + it.s);
            return;
        } else
        if(it.whi == 1) { // number
            it.type = "int";
        } else if(it.whi == 2) {
            it.type = "string";
        } else if(it.whi <= 4) {
            it.type = "bool";
        } else if(it.whi == 5) {
            it.type = "null";
        } else if(it.whi == 6) {
//            System.out.println(curClass.id);
            if(curClass == null)
                throw new SemanticError(it.pos, "this is a keyword");
            it.type = curClass.id;;
        }
    }

    public void visit(EmptyStmtNode it) {
    }

    public void visit(CtrlStmtNode it) {
        if(loops == 0)
            throw new SemanticError(it.pos, ((it.whi == false) ? "break" : "continue") + " outside a loop");
    }

    public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }

    public void visit(ForStmtNode it) {
        curScope = new Scope(curScope);
        ++ loops;
        if(it.VarDefStmt != null) {
            it.VarDefStmt.accept(this);
        }

        if(it.init != null)
            it.init.accept(this);

        if(it.cond != null) {
            it.cond.accept(this);
            if(!it.cond.type.equals("bool"))
                throw new SemanticError(it.pos, "Condition should be bool");
        }
        if(it.chg != null)
            it.chg.accept(this);

        it.stmt.accept(this);

        -- loops;
        curScope = curScope.fa;
    }

    public void visit(WhileStmtNode it) {
        curScope = new Scope(curScope);
        ++ loops;

        it.cond.accept(this);
        if(!it.cond.type.equals("bool"))
            throw new SemanticError(it.pos, "Condition Should be bool");
        it.Stmt.accept(this);

        loops --;
        curScope = curScope.fa;
    }

    public void visit(IfStmtNode it) {
        it.cond.accept(this);
        if(!it.cond.type.equals("bool"))
            throw new SemanticError(it.cond.pos, "Condition should be bool");
        curScope = new Scope(curScope);
        it.If_Stmt.accept(this);
        curScope = curScope.fa;

        if(it.Else_Stmt != null) {
            curScope = new Scope(curScope);
            it.Else_Stmt.accept(this);
            curScope = curScope.fa;
        }
    }

    public void visit(LambdaExprNode it) {
        if(it.allVar.size() != it.allExpr.size())
            throw new SemanticError(it.pos, "Function number and var number don't match");
        curScope = new Scope(curScope);

        it.allExpr.forEach(i -> i.accept(this));
        it.allVar.forEach(i -> i.accept(this));

        for(int i = 0; i < it.allVar.size(); ++ i) {
            if(it.allExpr.get(i).if_class != null)
                throw new SemanticError(it.allExpr.get(i).pos, "Expr " + i + " is class");
            if(it.allExpr.get(i).is_left_val == false && it.allExpr.get(i).if_func != null)
                throw new SemanticError(it.allExpr.get(i).pos, "Expr " + i + " is func");

            if(!it.allExpr.get(i).type.equals("null") && !it.allVar.get(i).type.equals(it.allExpr.get(i).type))
                throw new SemanticError(it.pos, "Expr " + i + "has wrong type!");

            if(!it.allExpr.get(i).type.equals("null") && it.allVar.get(i).dimension != it.allExpr.get(i).dimension)
                throw new SemanticError(it.pos, "Expr " + i + " number of dimension don't  match");
        }

        FuncDefNode f = new FuncDefNode(it.pos, 0, null, null,"Lambda " + it.pos.toString());
        curFunc.add(f);
        it.stmt.accept(this);

        if(f.returned == false)
            throw new SemanticError(it.pos, "No return!");

        it.type = f.return_type;
        it.dimension = f.dimension;
        curFunc.remove(curFunc.size() - 1);
        curScope = curScope.fa;
    }
}
