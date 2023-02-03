package Codegen.IR.Value.Inst;

import Codegen.IR.Value.Global.BasicBlock;
import Codegen.IR.Value.Value;
import org.antlr.v4.runtime.misc.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/*
  %indvar = phi i32 [ 0, %LoopHeader ], [ %nextindvar, %Loop ]
  %nextindvar = add i32 %indvar, 1
  根据之前的block决定从哪一个过来
 */

public class PhiInst extends Inst {
    public ArrayList < Pair < Value, BasicBlock > > AllBr;

    public PhiInst(Pair < Value, BasicBlock >... branches) {
        super(branches[0].a.type);
        this.AllBr = new ArrayList<>(Arrays.asList(branches));
        for(int i = 0; i < (int) AllBr.size(); ++ i) {
            addUse(AllBr.get(i).a);
            addUse(AllBr.get(i).b);
        }
    }
}
