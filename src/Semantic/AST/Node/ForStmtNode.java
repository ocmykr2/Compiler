package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class ForStmtNode extends StmtNode {
    public ExprNode init = null, cond = null, chg = null;
    public VarDefStmtNode VarDefStmt;
    public StmtNode stmt;

    public ForStmtNode(position pos, ExprNode init, ExprNode cond, ExprNode chg, VarDefStmtNode VarDefStmt
    , StmtNode stmt) {
        super(pos);
        this.init = init;
        this.cond = cond;
        this.chg = chg;
        this.VarDefStmt = VarDefStmt;
        this.stmt = stmt;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
