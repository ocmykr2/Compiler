package Utils.Error_;

import Utils.position;

public class SyntaxError extends Error {
    public String out;
    public SyntaxError(position pos, String out) {
        super(pos);
        this.out = "Syntax Error a" + pos.toString() + ":" + out;
    }

    public String toString() {
        return out;
    }
}
