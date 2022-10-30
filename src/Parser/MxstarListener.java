// Generated from /home/sei/Compiler/src/Parser/Mxstar.g4 by ANTLR 4.10.1
package Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxstarParser}.
 */
public interface MxstarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxstarParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void enterClassDef(MxstarParser.ClassDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#classDef}.
	 * @param ctx the parse tree
	 */
	void exitClassDef(MxstarParser.ClassDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncDef(MxstarParser.FuncDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#funcDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncDef(MxstarParser.FuncDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#constructFunc}.
	 * @param ctx the parse tree
	 */
	void enterConstructFunc(MxstarParser.ConstructFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#constructFunc}.
	 * @param ctx the parse tree
	 */
	void exitConstructFunc(MxstarParser.ConstructFuncContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#lambda}.
	 * @param ctx the parse tree
	 */
	void enterLambda(MxstarParser.LambdaContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#lambda}.
	 * @param ctx the parse tree
	 */
	void exitLambda(MxstarParser.LambdaContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxstarParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxstarParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(MxstarParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(MxstarParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStatement(MxstarParser.IfStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStatement(MxstarParser.IfStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStatement(MxstarParser.WhileStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStatement(MxstarParser.WhileStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStatement(MxstarParser.ForStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStatement(MxstarParser.ForStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ctrlStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterCtrlStatement(MxstarParser.CtrlStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ctrlStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitCtrlStatement(MxstarParser.CtrlStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStatement(MxstarParser.ReturnStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStatement(MxstarParser.ReturnStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code varDefStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVarDefStatement(MxstarParser.VarDefStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code varDefStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVarDefStatement(MxstarParser.VarDefStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExprStatement(MxstarParser.ExprStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExprStatement(MxstarParser.ExprStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code emptyStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterEmptyStatement(MxstarParser.EmptyStatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code emptyStatement}
	 * labeled alternative in {@link MxstarParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitEmptyStatement(MxstarParser.EmptyStatementContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#funcVarDef}.
	 * @param ctx the parse tree
	 */
	void enterFuncVarDef(MxstarParser.FuncVarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#funcVarDef}.
	 * @param ctx the parse tree
	 */
	void exitFuncVarDef(MxstarParser.FuncVarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#varDefStmt}.
	 * @param ctx the parse tree
	 */
	void enterVarDefStmt(MxstarParser.VarDefStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#varDefStmt}.
	 * @param ctx the parse tree
	 */
	void exitVarDefStmt(MxstarParser.VarDefStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void enterVarDef(MxstarParser.VarDefContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#varDef}.
	 * @param ctx the parse tree
	 */
	void exitVarDef(MxstarParser.VarDefContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#varDefSub}.
	 * @param ctx the parse tree
	 */
	void enterVarDefSub(MxstarParser.VarDefSubContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#varDefSub}.
	 * @param ctx the parse tree
	 */
	void exitVarDefSub(MxstarParser.VarDefSubContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#funcVarDefList}.
	 * @param ctx the parse tree
	 */
	void enterFuncVarDefList(MxstarParser.FuncVarDefListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#funcVarDefList}.
	 * @param ctx the parse tree
	 */
	void exitFuncVarDefList(MxstarParser.FuncVarDefListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nvarExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNvarExpr(MxstarParser.NvarExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nvarExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNvarExpr(MxstarParser.NvarExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(MxstarParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(MxstarParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(MxstarParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(MxstarParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subscriptExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubscriptExpr(MxstarParser.SubscriptExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subscriptExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubscriptExpr(MxstarParser.SubscriptExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code lambdaExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterLambdaExpr(MxstarParser.LambdaExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code lambdaExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitLambdaExpr(MxstarParser.LambdaExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code bracketExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBracketExpr(MxstarParser.BracketExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code bracketExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBracketExpr(MxstarParser.BracketExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSuffixExpr(MxstarParser.SuffixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code suffixExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSuffixExpr(MxstarParser.SuffixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(MxstarParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryExpr(MxstarParser.BinaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterCallExpr(MxstarParser.CallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code callExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitCallExpr(MxstarParser.CallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAssignExpr(MxstarParser.AssignExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assignExpr}
	 * labeled alternative in {@link MxstarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAssignExpr(MxstarParser.AssignExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxstarParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MxstarParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#ifStmt}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MxstarParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MxstarParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#whileStmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MxstarParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MxstarParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#forStmt}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MxstarParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void enterReturnType(MxstarParser.ReturnTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#returnType}.
	 * @param ctx the parse tree
	 */
	void exitReturnType(MxstarParser.ReturnTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxstarParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxstarParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#newType}.
	 * @param ctx the parse tree
	 */
	void enterNewType(MxstarParser.NewTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#newType}.
	 * @param ctx the parse tree
	 */
	void exitNewType(MxstarParser.NewTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#basicType}.
	 * @param ctx the parse tree
	 */
	void enterBasicType(MxstarParser.BasicTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#basicType}.
	 * @param ctx the parse tree
	 */
	void exitBasicType(MxstarParser.BasicTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#const}.
	 * @param ctx the parse tree
	 */
	void enterConst(MxstarParser.ConstContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#const}.
	 * @param ctx the parse tree
	 */
	void exitConst(MxstarParser.ConstContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxstarParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterPrimary(MxstarParser.PrimaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxstarParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitPrimary(MxstarParser.PrimaryContext ctx);
}