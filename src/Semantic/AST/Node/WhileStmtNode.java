package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.security.PublicKey;

public class WhileStmtNode extends StmtNode {
    public ExprNode cond;
    public StmtNode Stmt;

    public WhileStmtNode(position pos, ExprNode cond, StmtNode Stmt) {
        super(pos);
        this.cond = cond;
        this.Stmt = Stmt;
    }

    @Override
    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
