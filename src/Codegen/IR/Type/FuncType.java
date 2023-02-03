package Codegen.IR.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class FuncType extends Type{
    public Type retType = null;
    public PtrType belong = null;
    public ArrayList <Type> AllPara = new ArrayList<>();

    @Override
    public int getSize() {
        return (size == -1) ? (size = 8) : size;
    }

    public FuncType(){
        super(TypeTable.FUNCTION);
    }

    public FuncType(Type retType, Type... Parameters) {
        super(TypeTable.FUNCTION);
        this.retType = retType;
        AllPara = new ArrayList<> (Arrays.asList(Parameters));
    }
}
