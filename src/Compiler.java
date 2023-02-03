import Checker.SemanticChecker;
import Checker.SymbolCollector;
import Checker.Symbols;
import Codegen.Assembly.ASMPrinter;
import Codegen.Assembly.ASMValue.ASMRoot;
import Codegen.Assembly.ViolentBuilder;
import Codegen.IR.IRBuilder;
import Codegen.IR.Value.Global.Module;
import Semantic.AST.ASTBuilder;
import Semantic.AST.Node.ProgramNode;
import Parser.*;
import Utils.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public class Compiler {
    public static void main(String[] args) throws Exception{
        InputStream input_stream = System.in;
        CharStream input = CharStreams.fromStream((input_stream));
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-55.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-44.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-70.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/array-package/array-4.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/array-package/array-11.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/codegen/t70.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/codegen/e1.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/codegen/sorting/merge_sort.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/codegen/shortest_path/spfa.mx");
        try {
            MxstarLexer lexer;
            lexer = new MxstarLexer(input);
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxstarErrorListener());

            System.out.println("Alive in lexer");

            MxstarParser parser = new MxstarParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxstarErrorListener());
            ParseTree root = parser.program();

            System.out.println("Alive in parser");
            if(root != null) {
                System.out.println("Root is not Null");
            }

            ASTBuilder builder = new ASTBuilder();
            ProgramNode converted_root = (ProgramNode)builder.visit(root);
            Symbols symbols = new Symbols();
            new SymbolCollector(symbols).visit(converted_root);
            new SemanticChecker(symbols).visit(converted_root);

            System.out.println("Alive in SemanticChecker");

            IRBuilder irBuilder = new IRBuilder("cur.mx", converted_root);

            System.out.println("Alive in Builder");

            Module module = irBuilder.work();

//            new IRPrinter(System.out).visit(module);

            ViolentBuilder violentBuilder = new ViolentBuilder(module);
            ASMRoot asmRoot = violentBuilder.doit();
            //new RegAllocator(asmRoot).work();
//            FileOutputStream out = new FileOutputStream("test.s");
            new ASMPrinter(new PrintStream("output.s")).visit(asmRoot);
            new BuiltinPrinter("builtin.s");
        }

        catch (Error err) {
            System.err.println(err.toString());
            throw new RuntimeException();
        }
    }
}