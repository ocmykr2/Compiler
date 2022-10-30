package Semantic.AST.Node;

import java.util.ArrayList;
import Semantic.AST.ASTVisitor;
import Utils.position;

public class LambdaExprNode extends ExprNode {
    public ArrayList<FuncVarDefNode> allVar = new ArrayList<>();
    public ArrayList<ExprNode> allExpr = new ArrayList<>();
    public StmtNode stmt;

    public LambdaExprNode(position pos, StmtNode stmt) {
        super(pos);
        this.stmt = stmt;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
