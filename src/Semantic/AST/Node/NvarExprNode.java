package Semantic.AST.Node;

import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;

public class NvarExprNode extends ExprNode {
    public ArrayList< ExprNode > allExpr = new ArrayList<>();
    public ClassDefNode _Class = null;

    public NvarExprNode(position pos, String type, int dimension) {
        super(pos);
        this.type = type;
        this.dimension = dimension;
    }

    public void accept(ASTVisitor Visitor) {
        Visitor.visit(this);
    }
}
