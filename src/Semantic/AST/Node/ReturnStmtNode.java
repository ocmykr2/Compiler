package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class ReturnStmtNode extends StmtNode {
    public ExprNode expr;

    public ReturnStmtNode(position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
