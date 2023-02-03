package Codegen.IR.Value;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.Type;

import java.util.LinkedList;

public abstract class Value {
    public Type type;
    public String id = "";
    public String pid = "";
    public LinkedList <Use> UserList = new LinkedList<>();

    public abstract void accept(IRVisitor irVisitor);

    public Value(Type type) {
        this.type = type;
    }
}
