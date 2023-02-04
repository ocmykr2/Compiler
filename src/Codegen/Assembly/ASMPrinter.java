package Codegen.Assembly;

import Codegen.Assembly.ASMInst.ASMInst;
import Codegen.Assembly.ASMValue.ASMBlock;
import Codegen.Assembly.ASMValue.ASMFunc;
import Codegen.Assembly.ASMValue.ASMRoot;
import Codegen.IR.Constant.Constant;
import Codegen.IR.Constant.StrConstant;
import Codegen.IR.Value.Global.Variable;

import java.io.PrintStream;

public class ASMPrinter {
     public PrintStream out;

     public int strId = 0;

     public ASMPrinter(PrintStream out) {
         this.out = out;
     }

     public void printOut(String format, Object ... Allpara) {
         out.print("\t");
         out.printf(format, Allpara);
         out.println();
     }

     public void visit(ASMRoot it) {
         printOut(".text");
         printOut(".file\t\"%s\"", it.id);
         out.println();

         if(!it.Allfunc.isEmpty()) {
             it.Allfunc.forEach(i -> visit(i));
             out.println();
         }

         if(!it.AllVar.isEmpty()) {
             printOut(".section\t.sbss");
             it.AllVar.forEach((a, b) -> visit(b));
             out.println();
         }

         if(!it.AllConstant.isEmpty()) {
             printOut(".section .rodata");
             it.AllConstant.forEach(i -> visit(i));
             out.println();
         }
     }

     public void visit(Constant it) {
         out.printf("%s:\n", it.id);
         printOut(".asciz\t\"%s\"", ((StrConstant)it).ASMString());
     }

     public void visit(Variable it) {
         printOut(".globl\t%s", it.id);
         out.printf("%s:\n", it.id);
         printOut(".word\t0");
     }

     public void visit(ASMFunc it) {
         printOut(".globl\t%s", it.id);
         out.printf("%s:\n", it.id);
         it.Allblock.forEach(i -> visit(i));
         out.println();
     }

     public void visit(ASMBlock it) {
         out.printf("%s:\n", it.id);
         for(ASMInst i : it.AllInst) {
             printOut("%s", i.toString());
         }
     }
}
