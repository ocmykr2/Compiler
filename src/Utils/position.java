package Utils;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.ParserRuleContext;

public class position {
    public int row, col;

    public position() {
        this.row = 0;
        this.col = 0;
    }

    public position(int _row, int _col) {
        this.row = _row;
        this.col = _col;
    }

    public position(Token token) {
        this.row = token.getLine();
        this.col = token.getCharPositionInLine();
    }

    public position(ParserRuleContext ctx) {
        this(ctx.getStart());
    }

    public String toString() {
        return "[row:" + row + "," + "column:" + col+ "]";
    }
}
