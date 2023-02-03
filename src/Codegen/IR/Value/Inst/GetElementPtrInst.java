package Codegen.IR.Value.Inst;


// Exp: %tmp = getelementptr %struct.munger_struct* %P, i32 1, i32 0
// 计算指针位置,以及新的指针类型 第一个参数在这个struct中的第几个,第二个是struct内部的offset

import Codegen.IR.Type.PtrType;
import Codegen.IR.Type.StructType;
import Codegen.IR.Type.Type;
import Codegen.IR.Constant.IntConstant;
import Codegen.IR.Value.Value;

public class GetElementPtrInst extends Inst {
    public GetElementPtrInst(Type outType, Value ptr, Value ... pos) {
        super(outType);
        addUse(ptr);
        for(Value i : pos) {
            addUse(i);
        }
    }

    public GetElementPtrInst(Value ptr, Value pos) {
        this(ptr.type, ptr, pos);
    }

    public GetElementPtrInst(Value ptr, IntConstant pos1, IntConstant pos2) {
        this(new PtrType(((StructType)((PtrType)(ptr.type)).oriType).AllVar.get(pos2.val)), ptr, pos1, pos2);
    }
}
