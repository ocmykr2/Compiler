package Semantic.AST;

import Parser.MxstarBaseVisitor;
import Parser.MxstarParser;
import Semantic.AST.Node.*;
import Utils.position;

public class ASTBuilder extends MxstarBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(MxstarParser.ProgramContext ctx) {
        ProgramNode pro = new ProgramNode(new position(ctx));

        if (ctx.funcDef() != null) {
            ctx.funcDef().forEach(x -> pro.allFunc.add((FuncDefNode) visit(x)));
        }
        if(ctx.classDef() != null) {
            ctx.classDef().forEach(x -> pro.allClass.add((ClassDefNode) visit(x)));
        }

        if(ctx.varDefStmt() != null) {
            ctx.varDefStmt().forEach(x -> pro.allVar.add((VarDefStmtNode) visit(x)));
        }

        return pro;
    }

    @Override
    public ASTNode visitClassDef(MxstarParser.ClassDefContext ctx) {
        //System.out.println("Boom!");
        ClassDefNode cls = new ClassDefNode(new position(ctx), ctx.Identifier().getText());

        if(ctx.constructFunc() != null) {
            ctx.constructFunc().forEach(x -> cls.allFunc.add((FuncDefNode) visit(x)));
        } else {
            BlockStmtNode tmp = new BlockStmtNode(new position(ctx));
            cls.allFunc.add(new FuncDefNode(new position(ctx), 0, tmp, ctx.Identifier().getText(),ctx.Identifier().getText()));
        }

        if (ctx.funcDef() != null) {
            ctx.funcDef().forEach(x -> cls.allFunc.add((FuncDefNode) visit(x)));
        }

        if(ctx.varDefStmt() != null) {
            ctx.varDefStmt().forEach(x -> cls.allVar.add((VarDefStmtNode) visit(x)));
        }

        return cls;
    }

    @Override
    public ASTNode visitFuncDef(MxstarParser.FuncDefContext ctx) {
        StmtNode stmt = (StmtNode) visit(ctx.block());
        if(ctx.block() != null)
            System.out.println("WHAT ARE YOU DOING");
        String return_type = ctx.returnType().getText();
        FuncDefNode funcdef = new FuncDefNode(new position(ctx), count_dim(return_type), stmt, get_type(return_type), ctx.Identifier().getText());
        if(ctx.funcVarDefList() != null) {
            ctx.funcVarDefList().funcVarDef().forEach(x -> funcdef.allVar.add((FuncVarDefNode) visit(x)));
        }
        return funcdef;
    }

    @Override
    public ASTNode visitConstructFunc(MxstarParser.ConstructFuncContext ctx) {
        StmtNode stmt = (StmtNode) visit(ctx.block());
        FuncDefNode consfuncdef = new FuncDefNode(new position(ctx), 0, stmt, null, ctx.Identifier().getText());
        if(ctx.funcVarDefList() != null) {
            ctx.funcVarDefList().funcVarDef().forEach(x -> consfuncdef.allVar.add((FuncVarDefNode) visit(x)));
        }
        return consfuncdef;
    }

    @Override
    public ASTNode visitFuncVarDef(MxstarParser.FuncVarDefContext ctx) {
        String str = ctx.type().getText();
//        System.out.println(str);
        VarSubDefNode VarSub = (VarSubDefNode)visit(ctx.varDefSub());
        return new FuncVarDefNode(new position(ctx), count_dim(str), get_type(str), VarSub);
    }

    @Override
    public ASTNode visitVarDef(MxstarParser.VarDefContext ctx) {
        String str = ctx.type().getText();
//        System.out.println("BEGIN");
//        System.out.println(get_type(str));
//        System.out.println(count_dim(str));
//        System.out.println(ctx.varDefSub(0).Identifier());
//        System.out.println("END");
        VarDefStmtNode var = new VarDefStmtNode(new position(ctx), get_type(str), count_dim(str));
//        System.out.println("BEGIN=============");
//        System.out.println(var.type);
//        System.out.println(var.dimension);
//        System.out.println(ctx.varDefSub(0).Identifier());
//        System.out.println("END============");
        if(ctx.varDefSub() != null) {
            //ctx.varDefSub().forEach(x -> System.out.println(x.Identifier().getText()));
            ctx.varDefSub().forEach(x -> var.allVar.add((VarSubDefNode) visit(x)));
//            ctx.varDefSub()
        }
        return var;
    }

    @Override
    public ASTNode visitVarDefSub(MxstarParser.VarDefSubContext ctx) {
        ExprNode expr = null;
        if(ctx.expression() != null)
            expr = (ExprNode) visit(ctx.expression());
        return new VarSubDefNode(new position(ctx), ctx.Identifier().getText(), expr);
    }

    @Override
    public ASTNode visitVarDefStmt(MxstarParser.VarDefStmtContext ctx) {
//        System.out.println("sadsadasadadasdassdasdasdasdasd");
        return (VarDefStmtNode)visit(ctx.varDef());
    }

    @Override
    public ASTNode visitPrimaryExpr(MxstarParser.PrimaryExprContext ctx) {
        int whi = 0;
        if(ctx.primary().const_() != null) {
            if(ctx.primary().const_() != null) {
                System.out.println("Joke!");
                System.out.println((new position(ctx)).toString());
            }
            if(ctx.primary().const_().NumberConst() != null) {
                whi = 1;
            }

            if(ctx.primary().const_().StringConst() != null) {
                whi = 2;
            }

            if(ctx.primary().const_().BoolConst() != null) {
                String str = ctx.primary().const_().BoolConst().toString();
                // Why there are no true/false branch
                whi = 3;
                for(int i = 0; i < str.length(); ++ i) {
                    if(str.charAt(i) == 'f') {
                        whi = 4; break;
                    }
                }
            }

            if(ctx.primary().const_().NullConst() != null) {
                whi = 5;
            }

        } else if(ctx.primary().This() != null) {
            whi = 6;
        };

        return new PrimaryExprNode(new position((ctx)), whi, ctx.primary().getText());
    }

    @Override
    public ASTNode visitLambdaExpr(MxstarParser.LambdaExprContext ctx) {
        LambdaExprNode lambda = new LambdaExprNode(new position(ctx), (StmtNode)visit(ctx.lambda().block()));
        if(ctx.lambda().funcVarDefList() != null)
            ctx.lambda().funcVarDefList().funcVarDef().forEach(x -> lambda.allVar.add((FuncVarDefNode)visit(x)));
        if(ctx.lambda().expressionList() != null)
            ctx.lambda().expressionList().expression().forEach(x -> lambda.allExpr.add((ExprNode) visit(x)));
        return lambda;
    }

    @Override
    public ASTNode visitLambda(MxstarParser.LambdaContext ctx) {
        LambdaExprNode lambda = new LambdaExprNode(new position(ctx), (StmtNode)visit(ctx.block()));
        if(ctx.funcVarDefList() != null)
            ctx.funcVarDefList().funcVarDef().forEach(x -> lambda.allVar.add((FuncVarDefNode)visit(x)));
        if(ctx.expressionList() != null)
            ctx.expressionList().expression().forEach(x -> lambda.allExpr.add((ExprNode) visit(x)));
        return lambda;
    }

    @Override
    public ASTNode visitNvarExpr(MxstarParser.NvarExprContext ctx) {
        String id = ctx.newType().getText();
        //System.out.println(id);
        NvarExprNode nvar = new NvarExprNode(new position(ctx), get_type(id), count_dim(id));
        if(ctx.newType().expression() != null) {
            //ctx.newType().expression().forEach(x -> System.out.println(x.toString()));
            ctx.newType().expression().forEach(x -> nvar.allExpr.add((ExprNode)visit(x)));
        }
        return nvar;
    }
    @Override
    public ASTNode visitSuffixExpr(MxstarParser.SuffixExprContext ctx) {
        String op = ctx.op.getText();
        int whi = 0;
        if(op.charAt(0) == '-') whi = 1;
        ExprNode obj = (ExprNode) visit(ctx.expression());
        return new SuffExprNode(new position(ctx), whi, obj);
    }

    @Override
    public ASTNode visitBracketExpr(MxstarParser.BracketExprContext ctx) {
        return new BracketExprNode(new position(ctx), (ExprNode)visit(ctx.expression()));
    }

    @Override
    public ASTNode visitMemberExpr(MxstarParser.MemberExprContext ctx) {
        String id = ctx.Identifier().getText();
        ExprNode obj = (ExprNode) visit(ctx.expression());
        return new MemberNode(new position(ctx), obj, id);
    }

    @Override
    public ASTNode visitCallExpr(MxstarParser.CallExprContext ctx) {
        ExprNode obj = (ExprNode) visit(ctx.expression());
        CallExprNode call = new CallExprNode(new position(ctx), obj);
        if(ctx.expressionList() != null) {
            ctx.expressionList().expression().forEach(x -> call.allExpr.add((ExprNode) visit(x)));
        }
        return call;
    }

    @Override
    public ASTNode visitSubscriptExpr(MxstarParser.SubscriptExprContext ctx) {
        ExprNode obj = (ExprNode) visit(ctx.base);
        ExprNode offset = (ExprNode) visit(ctx.index);
        return new SubscriptExprNode(new position(ctx), obj, offset);
    }

    @Override
    public ASTNode visitPrefixExpr(MxstarParser.PrefixExprContext ctx) {
        String str = ctx.op.getText();
        int whi = 0;
        if(str.length() == 2) {
            if(str.charAt(0) == '+') // ++
                whi = 1;
            else whi = 2; // --
        } else {
            if(str.charAt(0) == '+') whi = 3;
            else if (str.charAt(0) == '-') whi = 4;
            else if (str.charAt(0) == '!') whi = 5;
            else whi = 6;// '~'
        }
        ExprNode obj = (ExprNode) visit(ctx.expression());
        return new PrefExprNode(new position(ctx), whi, obj);
    }

    @Override
    public ASTNode visitBinaryExpr(MxstarParser.BinaryExprContext ctx) {
        ExprNode src1 = (ExprNode) visit(ctx.src1),
                src2 = (ExprNode) visit(ctx.src2);
        String str = ctx.op.getText();
        int whi = 0;
        if(str.length() == 1) {
            if(str.charAt(0) == '*') {
                whi = 1;
            } else if(str.charAt(0) == '/') {
                whi = 2;
            } else if(str.charAt(0) == '%') {
                whi = 3;
            } else if(str.charAt(0) == '+') {
                whi = 4;
            } else if(str.charAt(0) == '-') {
                whi = 5;
            } else if(str.charAt(0) == '<') {
                whi = 14;
            } else if(str.charAt(0) == '>') {
                whi = 15;
            } else if(str.charAt(0) == '&') {
                whi = 16;
            } else if(str.charAt(0) == '^') {
                whi = 17;
            } else if(str.charAt(0) == '|') {
                whi = 18;
            }
        } else {
            if(str.charAt(1) == '<') {        // <<
                whi = 6;
            } else if(str.charAt(1) == '>') { // >>
                whi = 7;
            } else if(str.charAt(0) == '=') { // ==
                whi = 8;
            } else if(str.charAt(0) == '!') { // !=
                whi = 9;
            } else if(str.charAt(0) == '<') { // <=
                whi = 10;
            } else if(str.charAt(0) == '>') { // >=
                whi = 11;
            } else if(str.charAt(0) == '&') { // &&
                whi = 12;
            } else if(str.charAt(0) == '|') { // ||
                whi = 13;
            }
        }
        if(whi == 0) {
            //System.out.println("No No No!!!!!");
            //System.out.println(str);
        }
        return new BinaryExprNode(new position(ctx), src1, src2, whi);
    }

    @Override
    public ASTNode visitAssignExpr(MxstarParser.AssignExprContext ctx) {
        ExprNode src1 = (ExprNode) visit(ctx.src1);
        ExprNode src2 = (ExprNode) visit(ctx.src2);
        return new AssignExprNode(new position(ctx), src1, src2);
    }

    @Override
    public ASTNode visitBlockStatement(MxstarParser.BlockStatementContext ctx) {
        //System.out.println("asdasdasd!!!!!!!!");
        return (BlockStmtNode)visit(ctx.block());
    }

    @Override
    public ASTNode visitBlock(MxstarParser.BlockContext ctx) {
        BlockStmtNode it = new BlockStmtNode(new position(ctx));
        if(ctx.statement() != null) {
            ctx.statement().forEach(x -> it.stmt.add((StmtNode) visit(x)));
        }
        return it;
    }

    @Override
    public ASTNode visitVarDefStatement(MxstarParser.VarDefStatementContext ctx) {
        return (VarDefStmtNode)visit(ctx.varDefStmt());
    }

    @Override
    public ASTNode visitIfStatement(MxstarParser.IfStatementContext ctx) {
        return (IfStmtNode)visit(ctx.ifStmt());
    }

    @Override
    public ASTNode visitIfStmt(MxstarParser.IfStmtContext ctx) {
        ExprNode cond = (ExprNode) visit(ctx.expression());
        StmtNode TrueStmt = (StmtNode) visit(ctx.trueStmt);
        StmtNode FalseStmt = null;
        if(ctx.falseStmt != null) {
            FalseStmt = (StmtNode) visit(ctx.falseStmt);
        }
        return new IfStmtNode(new position(ctx), cond, TrueStmt, FalseStmt);
//        position pos, ExprNode cond, StmtNode If_Stmt, StmtNode Else_Stmt
    }

    @Override
    public ASTNode visitWhileStatement(MxstarParser.WhileStatementContext ctx) {
        return (WhileStmtNode)visit(ctx.whileStmt());
    }

    @Override
    public ASTNode visitWhileStmt(MxstarParser.WhileStmtContext ctx) {
        ExprNode cond = (ExprNode) visit(ctx.expression());
        StmtNode stmt = (StmtNode) visit(ctx.statement());
        return new WhileStmtNode(new position(ctx), cond, stmt);
    }

    @Override
    public ASTNode visitForStatement(MxstarParser.ForStatementContext ctx) {
        return (ForStmtNode)visit(ctx.forStmt());
    }

    @Override
    public ASTNode visitForStmt(MxstarParser.ForStmtContext ctx) {
        //position pos, ExprNode init, ExprNode cond, ExprNode chg, VarDefStmtNode VarDefStmt, StmtNode stmt
        ExprNode init = null, cond = null, chg = null;
        if(ctx.init != null)
            init = (ExprNode) visit(ctx.init);
        if(ctx.cond != null)
            cond = (ExprNode) visit(ctx.cond);
        if(ctx.chg != null)
            chg = (ExprNode) visit(ctx.chg);
        VarDefStmtNode vardefstmt = null;
        if(ctx.varDef() != null)
        vardefstmt = (VarDefStmtNode) visit(ctx.varDef());
        StmtNode stmt = (StmtNode) visit(ctx.statement());
        return new ForStmtNode(new position(ctx),init, cond, chg, vardefstmt, stmt);
    }

    @Override
    public ASTNode visitCtrlStatement(MxstarParser.CtrlStatementContext ctx) {
        String str = ctx.getText();
        Boolean whi = false;
        for(int i = 0; i < str.length(); ++ i) {
            if(str.charAt(i) == 'c') {
                whi = true;
                break;
            }
        }
        return new CtrlStmtNode(new position(ctx), whi);
    }

    @Override
    public ASTNode visitReturnStatement(MxstarParser.ReturnStatementContext ctx) {
        ExprNode expr = null;
        if(ctx.expression() != null) {
            expr = (ExprNode)visit(ctx.expression());
        }
        return new ReturnStmtNode(new position(ctx), expr);
    }

    @Override
    public ASTNode visitExprStatement(MxstarParser.ExprStatementContext ctx) {
        ExprNode expr = null;
        if(ctx.expression() != null) {
            expr = (ExprNode)visit(ctx.expression());
        }
        return new ExprStmtNode(new position(ctx), expr);
    }
    @Override
    public ASTNode visitEmptyStatement(MxstarParser.EmptyStatementContext ctx) {
        return new EmptyStmtNode(new position(ctx));
    }

    public String get_type(String str) {
        String type = "";
        for(int i = 0; i < str.length(); ++ i) {
            if(str.charAt(i) == '[') break;
            type = type + str.charAt(i);
        }
        return type;
    }

    public int count_dim(String str) {
        // just count the top ceng
        int dim = 0, tmp = 0;
        for(int i = 0; i < str.length(); ++ i) {
            if(str.charAt(i) != '[' && str.charAt(i) != ']') continue;
            if(str.charAt(i) == '[') ++ tmp;
            if(str.charAt(i) == ']') {
                -- tmp;
                if(tmp == 0)
                    ++ dim;
            }
        }
        return dim;
    }

}
