package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;

public class BlockStmtNode extends StmtNode {

    public ArrayList < StmtNode > stmt = new ArrayList<>();

    public BlockStmtNode(position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
