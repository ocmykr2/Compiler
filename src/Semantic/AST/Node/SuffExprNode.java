package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class SuffExprNode extends ExprNode {
    public int whi;
    // 0 for ++, 1 for --
    public ExprNode obj;

    public SuffExprNode(position pos, int whi, ExprNode obj) {
        super(pos);
        this.whi = whi;
        this.obj = obj;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
