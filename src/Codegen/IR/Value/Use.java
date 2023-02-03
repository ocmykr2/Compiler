package Codegen.IR.Value;

public class Use {
    public User u;
    public Value v;

    public Use(User u, Value v) {
        this.u = u;
        this.v = v;
    }

    public static Use getUseLink(User u, Value v) {
        Use tmp = new Use(u, v);
        u.OperandList.add(tmp);
        v.UserList.add(tmp);
        return tmp;
    }
}
