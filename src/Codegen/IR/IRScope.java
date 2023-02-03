package Codegen.IR;


import Codegen.IR.Value.Value;
import Semantic.AST.Node.VarSubDefNode;

import java.util.HashMap;

public class IRScope {
    public HashMap < String, Value > Allpara = new HashMap<>();
    public IRScope fa;
    boolean if_class = false;

    public IRScope(IRScope fa, boolean if_class) {
        this.fa = fa; this.if_class = if_class;
    }

    public void add_para(String id, Value var) {
        Allpara.put(id, var);
    }

    public Value get_para(String id, Boolean Searchup) {
        //System.out.println(id);
        if(Allpara.containsKey(id)) {
            return Allpara.get(id);
        }
        if(fa != null && (Searchup == true))
            return fa.get_para(id, Searchup);
        return null;
    }

    public boolean if_class_get(String id, Boolean Searchup) {
        if(Allpara.containsKey(id)) {
            return if_class;
        }
        if(fa != null && (Searchup == true))
            return fa.if_class_get(id, true) | if_class;
        return false;
    }
}
