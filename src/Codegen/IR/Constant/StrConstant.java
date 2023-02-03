package Codegen.IR.Constant;

import Codegen.IR.IRVisitor;
import Codegen.IR.Type.ArrayType;
import Codegen.IR.Type.IntType;
import Codegen.IR.Type.PtrType;

public class StrConstant extends Constant {
    public String s;

    public StrConstant(String s) {
        super(null);
        s = s.replace("\\\\", "\\")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\"", "\"");
        type = new PtrType(new ArrayType(IntType.INT8, s.length() + 1));
        this.s = s;
        //System.out.println(s);
    }

    public String LLVMString() {
        return s.replace("\\", "\\\\")
                .replace("\n", "\\0A")
                .replace("\r", "\\0D")
                .replace("\t", "\\09")
                .replace("\"", "\\22") + "\\00";
    }

    public String ASMString() {
        return s.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\"", "\\\"");
    }

    public void accept(IRVisitor irVisitor) {
        irVisitor.visit(this);
    }
}
