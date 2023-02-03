package Codegen.IR;

import Codegen.IR.Constant.EmptyConstant;
import Codegen.IR.Constant.IntConstant;
import Codegen.IR.Constant.NullConstant;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Global.BasicBlock;
import Codegen.IR.Value.Global.Function;
import Codegen.IR.Value.Global.Variable;
import Codegen.IR.Value.Global.Module;
import Codegen.IR.Value.Inst.Inst;

public class IRVisitor {
    public void visit(Inst it) { }
    public void visit(IntConstant it) { }
    public void visit(NullConstant it) { }
    public void visit(StrConstant it) { }
    public void visit(EmptyConstant it) { }
    public void visit(Module it) { }
    public void visit(Function it) { }
    public void visit(BasicBlock it) { }
    public void visit(Variable it) { }
}
