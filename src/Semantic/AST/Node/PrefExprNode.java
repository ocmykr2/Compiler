package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class PrefExprNode extends ExprNode {
    public int whi;
    public ExprNode obj;

    public PrefExprNode(position pos, int whi, ExprNode obj) {
        super(pos);
        this.whi = whi;
        this.obj = obj;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
