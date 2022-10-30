package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;

public class ProgramNode extends ASTNode {
    public ArrayList <FuncDefNode> allFunc = new ArrayList<>();
    public ArrayList <VarDefStmtNode> allVar = new ArrayList<>();
    public ArrayList <ClassDefNode> allClass = new ArrayList<>();
    // use public

    public ProgramNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
