package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class SubscriptExprNode extends ExprNode {
    public ExprNode obj, offset;

    public SubscriptExprNode(position pos, ExprNode obj, ExprNode offset) {
        super(pos);
        this.obj = obj;
        this.offset = offset;
    }

    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
