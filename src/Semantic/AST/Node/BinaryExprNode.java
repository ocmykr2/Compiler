package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class BinaryExprNode extends ExprNode {

    public ExprNode src1, src2;
    public int whi;

    public BinaryExprNode(position pos, ExprNode src1, ExprNode src2, int whi) {
        super(pos);
        this.src1 = src1;
        this.src2 = src2;
        this.whi = whi;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
