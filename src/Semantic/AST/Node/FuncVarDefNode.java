package Semantic.AST.Node;

import Codegen.IR.Value.Value;
import Semantic.AST.ASTVisitor;
import Utils.position;
public class FuncVarDefNode extends ASTNode{
    public int dimension;
    public String type;
    public VarSubDefNode VarSub;

    public Value val;

    public FuncVarDefNode(position pos, int dimension, String type, VarSubDefNode VarSub) {
        super(pos);
        this.dimension = dimension;
        this.type = type;
        this.VarSub = VarSub;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
