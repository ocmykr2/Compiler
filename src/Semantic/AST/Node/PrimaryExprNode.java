package Semantic.AST.Node;

import Codegen.IR.Value.Global.Function;
import Semantic.AST.ASTVisitor;
import Utils.position;

public class PrimaryExprNode extends ExprNode{
    // 0 Identifier 1 number_const 2 string_const 3 true 4 false 5 null 6 this
    public int whi;
    public String s;
    public Function classFunc = null;

    public PrimaryExprNode(position pos, int whi, String s) {
        super(pos);
        this.whi = whi;
        this.s = s;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
