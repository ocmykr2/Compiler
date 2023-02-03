package Checker;

import Semantic.AST.Node.VarSubDefNode;

import java.util.HashMap;

public class Scope {
    public HashMap < String, VarSubDefNode > allVar = new HashMap<>();
    public Scope fa;
    public Scope(Scope fa) {
        this.fa = fa;
    }

    public void add(String id, VarSubDefNode var) {
        //System.out.println("ADDDDDDDDDDDD");
        //System.out.println(id);
        //System.out.println(var.type);
        //System.out.println("BDDDDDDDDDDDD");
        allVar.put(id, var);
    }

    public VarSubDefNode get_var(String id, Boolean Searchup) {
        if(allVar.containsKey(id)) {
            return allVar.get(id);
        }
        if(fa != null && (Searchup == true))
            return fa.get_var(id, Searchup);
        return null;
    }
}
