import Checker.SemanticChecker;
import Checker.SymbolCollector;
import Checker.Symbols;
import Semantic.AST.ASTBuilder;
import Semantic.AST.Node.ProgramNode;
import Parser.*;
import Utils.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.InputStream;

public class Compiler {
    public static void main(String[] args) throws Exception{
        InputStream input_stream = System.in;
        CharStream input = CharStreams.fromStream((input_stream));
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-24.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-68.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/basic-package/basic-70.mx");
//        CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/array-package/array-4.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/array-package/array-11.mx");
        //CharStream input = CharStreams.fromFileName("/home/sei/Compiler/sema/condition.mx");
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
        }

        catch (Error err) {
            System.err.println(err.toString());
            throw new RuntimeException();
        }
    }
}