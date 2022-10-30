package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class MemberNode extends ExprNode {
    public ExprNode obj;
    public String id;

    public MemberNode(position pos, ExprNode obj, String id) {
        super(pos);
        this.obj = obj;
        this.id = id;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}