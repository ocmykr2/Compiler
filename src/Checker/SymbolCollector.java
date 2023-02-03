package Checker;

import Semantic.AST.ASTVisitor;
import Semantic.AST.Node.*;
import Utils.Error_.SemanticError;
import Utils.position;

public class SymbolCollector extends ASTVisitor {
    public Symbols symbols;
    public ClassDefNode curClass = null;

    public SymbolCollector(Symbols symbols) {
        this.symbols = symbols;

        ClassDefNode Int = new ClassDefNode(new position(), "int");
        curClass = Int;
        Int.accept(this);
        curClass = null;

        ClassDefNode Bool = new ClassDefNode(new position(), "bool");
        curClass = Bool;
        Bool.accept(this);
        curClass = null;

        ClassDefNode Str = new ClassDefNode(new position(), "string");
        Str.allFunc.add(new FuncDefNode(new position(), 0, null,"int", "length", true));

        FuncDefNode substring = new FuncDefNode(new position(), 0, null, "string", "substring", true);
        FuncVarDefNode left = new FuncVarDefNode(new position(),0, "int" , new VarSubDefNode(new position(), "left", null));
        FuncVarDefNode right = new FuncVarDefNode(new position(),0, "int" , new VarSubDefNode(new position(), "right", null));
        substring.allVar.add(left);
        substring.allVar.add(right);
        Str.allFunc.add(substring);

        Str.allFunc.add(new FuncDefNode(new position(), 0, null, "int", "parseInt" , true));

        FuncDefNode ord = new FuncDefNode(new position(), 0, null, "int", "ord", true);
        FuncVarDefNode pos = new FuncVarDefNode(new position(), 0, "int", new VarSubDefNode(new position(), "pos", null));
        ord.allVar.add(pos);
        Str.allFunc.add(ord);

        curClass = Str;
        Str.accept(this);
        curClass = null;

        ClassDefNode Void = new ClassDefNode(new position(), "void");
        curClass = Void;
        Void.accept(this);
        curClass = null;


        FuncVarDefNode printpara = new FuncVarDefNode(new position(), 0, "string", new VarSubDefNode(new position(), "str", null));
        FuncDefNode print = new FuncDefNode(new position(), 0, null, "void", "print");
        print.allVar.add(printpara);
        print.accept(this);

        FuncVarDefNode printlnpara = new FuncVarDefNode(new position(), 0, "string", new VarSubDefNode(new position(), "str", null));
        FuncDefNode println = new FuncDefNode(new position(), 0, null, "void", "println");
        println.allVar.add(printlnpara);
        println.accept(this);

        FuncVarDefNode printIntpara = new FuncVarDefNode(new position(), 0,"int", new VarSubDefNode(new position(), "n", null));
        FuncDefNode printInt = new FuncDefNode(new position(), 0, null,"void", "printInt");
        printInt.allVar.add(printIntpara);
        printInt.accept(this);

        FuncVarDefNode printlnIntpara = new FuncVarDefNode(new position(), 0,"int", new VarSubDefNode(new position(), "n", null));
        FuncDefNode printlnInt = new FuncDefNode(new position(), 0, null,"void", "printlnInt");
        printlnInt.allVar.add(printlnIntpara);
        printlnInt.accept(this);

        FuncDefNode getString = new FuncDefNode(new position(), 0,null, "string", "getString", true);
        getString.accept(this);

        FuncDefNode getInt = new FuncDefNode(new position(), 0, null, "int", "getInt", true);
        getInt.accept(this);

        FuncVarDefNode toStringpara = new FuncVarDefNode(new position(), 0, "int", new VarSubDefNode(new position(), "i", null));
        FuncDefNode toString = new FuncDefNode(new position(), 0, null, "string", "toString", true);
        toString.allVar.add(toStringpara);
        toString.accept(this);

        FuncDefNode size = new FuncDefNode(new position(), 0, null, "int", "__builtin_size");
        size.accept(this);
    }

     @Override
     public void visit(ProgramNode it) {
        it.allClass.forEach(i -> i.accept(this));
        it.allFunc.forEach(i -> i.accept(this));
     }

     @Override
     public void visit(FuncDefNode it) {
//        System.out.println(it.id + ";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
        if(curClass == null) {
            if(symbols.funcDefined(it.id))
                throw new SemanticError(it.pos, "symbol " + it.id + " has been defined");
            symbols.addFunc(it.id, it);
        } else {
            it.belong = curClass;
            if(curClass.FuncMap.containsKey(it.id)) {
                throw new SemanticError(it.pos, "member function" + it.id + "has been defined");
            }
            if(it.id.equals(curClass.id)) {
                if(it.return_type != null)
                throw new SemanticError(it.pos, "Constructor for " + it.id + " has a type");
                else it.return_type = "void";
            }
            curClass.FuncMap.put(it.id, it);
        }
     }

    @Override
    public void visit(ClassDefNode it) {
        System.out.println(it.id);
        if(it.id.equals("main")) {
            throw new SemanticError(it.pos, "Duplicated name for main");
        }
        if(symbols.classDefined(it.id)) {
            throw new SemanticError(it.pos, "symbol" + it.id + "has been defined");
        }
        symbols.addClass(it.id, it);
        curClass = it;
        it.allVar.forEach(i -> i.accept(this));
        it.allFunc.forEach(i -> i.accept(this));
        curClass = null;
    }

    @Override
    public void visit(VarDefStmtNode it) {
        for(VarSubDefNode i : it.allVar) {
            if(curClass.VarMap.containsKey(i.id)) {
                throw new SemanticError(it.pos, "member variable " + i.id + " has been defined");
            }
            i.type = it.type;
            i.dimension = it.dimension;
            curClass.VarMap.put(i.id, i);
        }
    }
}
