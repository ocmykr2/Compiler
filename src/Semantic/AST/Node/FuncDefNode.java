package Semantic.AST.Node;
import Semantic.AST.ASTVisitor;
import Utils.position;

import java.util.ArrayList;

public class FuncDefNode extends ASTNode {
    public int dimension;
    public StmtNode stmt;
    public String return_type, id;
    public ArrayList < FuncVarDefNode > allVar = new ArrayList<>();
    public Boolean returned = false;

    public ClassDefNode belong = null;
    public FuncDefNode(position pos, int dimension, StmtNode stmt, String return_type, String id) {
        super(pos);
        this.dimension  = dimension;
        this.stmt = stmt;
        this.return_type = return_type;
        this.id = id;
    }

    public FuncDefNode(position pos, int dimension, StmtNode stmt, String return_type, String id, Boolean returned) {
        super(pos);
        this.dimension  = dimension;
        this.stmt = stmt;
        this.return_type = return_type;
        this.id = id;
        this.returned = returned;
    }

    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
