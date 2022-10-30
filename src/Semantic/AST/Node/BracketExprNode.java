package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class BracketExprNode extends ExprNode {
    public ExprNode expr;

    public BracketExprNode(position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }
    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
