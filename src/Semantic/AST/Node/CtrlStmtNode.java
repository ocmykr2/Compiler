package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

public class CtrlStmtNode extends StmtNode {
    public Boolean whi;
    // 0 for break, 1 for continue;

    public CtrlStmtNode(position pos, Boolean whi) {
        super(pos);
        this.whi = whi;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
