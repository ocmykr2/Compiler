package Codegen.IR.Value;

import java.util.LinkedList;
import Codegen.IR.Type.Type;
abstract public class User extends Value {
    public LinkedList<Use> OperandList = new LinkedList<>();

    public User(Type type) {
        super(type);
    }

    // Only run in User

    public void addUse(Value v) {
        Use.getUseLink(this, v);
    }

    public Value getUse(int pos) {
        return OperandList.get(pos).v;
    }

    public int getOperandCnt() {
        return OperandList.size();
    }
}
