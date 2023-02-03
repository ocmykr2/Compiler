package Codegen.IR.Type;

import java.util.ArrayList;

public class ArrayType extends Type {
    public Type oriType;
    public ArrayList< Integer > Alldim = new ArrayList<>();

    @Override
    public String toString() {
        if(Alldim.isEmpty())
            return oriType.toString();
        ArrayType tmp = new ArrayType(oriType);
        for(int i = 1; i < (int) Alldim.size(); ++ i)
            tmp.Alldim.add(Alldim.get(i));
        return "[" + Alldim.get(0) + " x " + tmp + "]";
    }

    public ArrayType(Type oriType) {
        super(TypeTable.ARRAY);
        this.oriType = oriType;
    }

    public ArrayType(Type oriType, int d) {
        super(TypeTable.ARRAY);
        this.oriType = oriType;
        Alldim.add(d);
    }
}
