package Codegen.IR.Constant;
import Codegen.IR.Type.Type;
import Codegen.IR.Value.Value;

public abstract class Constant extends Value {
    public Constant(Type type) {
        super(type);
    }
}
