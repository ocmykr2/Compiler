package Semantic.AST.Node;
import Utils.position;
import Semantic.AST.ASTVisitor;

abstract public class ASTNode {
    public position pos;

    public ASTNode(position pos) {
        this.pos = pos;
    }

    abstract public void accept(ASTVisitor visitor);
}
