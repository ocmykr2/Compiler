package Semantic.AST.Node;

import java.util.ArrayList;
import Semantic.AST.ASTVisitor;
import Utils.position;

public class CallExprNode extends ExprNode {
    public FuncDefNode func = null;
    public ExprNode obj;
    public ArrayList<ExprNode> allExpr = new ArrayList<>();

    public CallExprNode(position pos, ExprNode obj) {
        super(pos);
        this.obj = obj;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}