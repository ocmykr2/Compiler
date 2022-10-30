package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassDefNode extends ASTNode {
    public String id;
    public FuncDefNode Construct = null;
    public ArrayList < VarDefStmtNode > allVar = new ArrayList<>();
    public ArrayList <FuncDefNode> allFunc = new ArrayList<>();
    // Can add a new allConstruct for Construct Function
    public HashMap <String, FuncDefNode> FuncMap = new HashMap<>();
    public HashMap <String, VarSubDefNode> VarMap = new HashMap<>();

    public ClassDefNode(position pos, String id) {
        super(pos);
        this.id = id;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
