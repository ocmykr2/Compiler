package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class EmptyStmtNode extends StmtNode {
    public EmptyStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
