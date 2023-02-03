package Semantic.AST.Node;

import Codegen.IR.Type.PtrType;
import Codegen.IR.Value.Value;
import Utils.position;

// use abstract
public abstract class ExprNode extends ASTNode {
    public int dimension;
    public String type;
    public boolean is_left_val, tobe_left_val;
    public FuncDefNode if_func;
    public ClassDefNode if_class;
    public VarSubDefNode Node_In_Def;

    public Value val = null, ptr = null;

    public ExprNode(position pos) {
        super(pos);
        this.dimension = 0;
        this.type = null;
        this.is_left_val = false;
        this.tobe_left_val = false;
        this.if_func = null;
        this.if_class = null;
        this.Node_In_Def = null;
    }

}
