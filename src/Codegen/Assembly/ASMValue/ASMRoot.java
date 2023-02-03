package Codegen.Assembly.ASMValue;

import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Global.Variable;

import java.util.ArrayList;
import java.util.HashMap;

public class ASMRoot {
    public String id;
    public ArrayList < ASMFunc > Allfunc = new ArrayList<>();
    public HashMap< String, Variable > AllVar = null;
    public ArrayList < StrConstant > AllConstant = null;
    public int StackSize = 0;

    public ASMRoot(String id) {
        this.id = id;
    }
}
