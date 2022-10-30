package Utils.Error_;

import Utils.position;

public class SemanticError extends Error {
    public String out;
    public SemanticError(position pos, String out) {
        super(pos);
        this.out = "Semantic Error at " + pos.toString() + ":" + out;
    }
    public String toString() {
        return out;
    }
}
