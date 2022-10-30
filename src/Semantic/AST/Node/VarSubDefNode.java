package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class VarSubDefNode extends ASTNode {

    public String id, type;
    public int dimension;
    public ExprNode init_expr;

    public VarSubDefNode(position pos, String id, ExprNode init_expr) {
        super(pos);
        this.id = id;
        this.init_expr = init_expr;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
