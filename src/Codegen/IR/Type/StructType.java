package Codegen.IR.Type;

import java.util.ArrayList;
import java.util.HashMap;

public class StructType extends Type {
    public String id, pid;
    public ArrayList < Type > AllVar = new ArrayList<>();
    public HashMap < String, Integer > pos = new HashMap<>();

    public StructType(String id) {
        super(TypeTable.STRUCT);
        this.id = id;
    }

    @Override
    public int getSize() {
        if(size == -1) {
            size = 0;
            for(int i = 0; i < (int) AllVar.size(); ++ i) {
                size += AllVar.get(i).getSize();
            }
        }
        return size;
    }

    public int getOffset(int cnt) {
        return cnt * 4;
    }

    @Override
    public boolean equals(Object rhs) {
        if(this == rhs) return true;
        if(rhs == null || getClass() != rhs.getClass())
            return false;
        return id == ((StructType)rhs).id;
    }

    @Override
    public String toString() {
        return pid;
    }
}
