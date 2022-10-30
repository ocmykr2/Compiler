package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class IfStmtNode extends StmtNode {
    public ExprNode cond;
    public StmtNode If_Stmt, Else_Stmt;

    public IfStmtNode(position pos, ExprNode cond, StmtNode If_Stmt, StmtNode Else_Stmt) {
        super(pos);
        this.cond = cond;
        this.If_Stmt = If_Stmt;
        this.Else_Stmt = Else_Stmt;
    }

    @Override
    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
