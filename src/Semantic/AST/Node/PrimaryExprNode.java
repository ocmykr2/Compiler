package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class PrimaryExprNode extends ExprNode{
    public int whi;
    public String s;

    public PrimaryExprNode(position pos, int whi, String s) {
        super(pos);
        this.whi = whi;
        this.s = s;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
