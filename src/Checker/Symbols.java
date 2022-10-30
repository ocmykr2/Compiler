package Checker;


import Semantic.AST.Node.ClassDefNode;
import Semantic.AST.Node.FuncDefNode;

import java.util.HashMap;

public class Symbols {
    public HashMap<String, FuncDefNode> allFunc  = new HashMap<>();
    public HashMap<String, ClassDefNode> allClass = new HashMap<>();

    public Boolean defined(String id) {
        return allFunc.containsKey(id) || allClass.containsKey(id);
    }

    public Boolean funcDefined(String id) {
        return allFunc.containsKey(id);
    }

    public Boolean classDefined(String id) {
        return allClass.containsKey(id);
    }

    public void addFunc(String id, FuncDefNode func) {
        allFunc.put(id, func);
    }

    public void addClass(String id, ClassDefNode cls) {allClass.put(id, cls);}

    FuncDefNode getFunc(String id) {
        if(allFunc.containsKey(id)) {
            return allFunc.get(id);
        } else return null;
    }

    ClassDefNode getClass(String id) {
        if(allClass.containsKey(id)) {
            return allClass.get(id);
        } else return null;
    }
}
