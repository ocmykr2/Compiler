package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;
public abstract class StmtNode extends ASTNode {
    public StmtNode(position pos) {
        super(pos);
    }
    /*public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }*/
}
