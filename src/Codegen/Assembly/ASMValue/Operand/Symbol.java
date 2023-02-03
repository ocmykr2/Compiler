package Codegen.Assembly.ASMValue.Operand;

public class Symbol extends Operand {
    String id;
    public Symbol(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }

    public String highbit() {
        return String.format("%%hi(%s)", id);
    }

    public String lowbit() {
        return String.format("%%lo(%s)", id);
    }
}
