package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;

public class VarDefStmtNode extends StmtNode {
    public String type;
    public int dimension;
    public ArrayList < VarSubDefNode > allVar = new ArrayList<>();

    public VarDefStmtNode(position pos, String type, int dimension) {
        super(pos);
        this.type = type;
        this.dimension = dimension;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
