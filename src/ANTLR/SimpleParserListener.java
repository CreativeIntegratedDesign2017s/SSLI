package ANTLR;// Generated from C:/Users/philg/SSLI/src\SimpleParser.g4 by ANTLR 4.6
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SimpleParser}.
 */
public interface SimpleParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SimpleParser#prgm}.
	 * @param ctx the parse tree
	 */
	void enterPrgm(SimpleParser.PrgmContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#prgm}.
	 * @param ctx the parse tree
	 */
	void exitPrgm(SimpleParser.PrgmContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterStatement(SimpleParser.StatementContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitStatement(SimpleParser.StatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Procedure}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterProcedure(SimpleParser.ProcedureContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Procedure}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitProcedure(SimpleParser.ProcedureContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Import}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterImport(SimpleParser.ImportContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Import}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitImport(SimpleParser.ImportContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(SimpleParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(SimpleParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void enterStmt_list(SimpleParser.Stmt_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#stmt_list}.
	 * @param ctx the parse tree
	 */
	void exitStmt_list(SimpleParser.Stmt_listContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Evaluate}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterEvaluate(SimpleParser.EvaluateContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Evaluate}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitEvaluate(SimpleParser.EvaluateContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Declare}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterDeclare(SimpleParser.DeclareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Declare}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitDeclare(SimpleParser.DeclareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterAssign(SimpleParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitAssign(SimpleParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IfElse}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterIfElse(SimpleParser.IfElseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IfElse}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitIfElse(SimpleParser.IfElseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DoWhile}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterDoWhile(SimpleParser.DoWhileContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DoWhile}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitDoWhile(SimpleParser.DoWhileContext ctx);
	/**
	 * Enter a parse tree produced by the {@code WhileDo}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterWhileDo(SimpleParser.WhileDoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code WhileDo}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitWhileDo(SimpleParser.WhileDoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterReturn(SimpleParser.ReturnContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitReturn(SimpleParser.ReturnContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Nested}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterNested(SimpleParser.NestedContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Nested}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitNested(SimpleParser.NestedContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Substring}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubstring(SimpleParser.SubstringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Substring}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubstring(SimpleParser.SubstringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBracket(SimpleParser.BracketContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBracket(SimpleParser.BracketContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Or}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterOr(SimpleParser.OrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitOr(SimpleParser.OrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(SimpleParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(SimpleParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(SimpleParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(SimpleParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Subscript}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubscript(SimpleParser.SubscriptContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Subscript}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubscript(SimpleParser.SubscriptContext ctx);
	/**
	 * Enter a parse tree produced by the {@code String}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterString(SimpleParser.StringContext ctx);
	/**
	 * Exit a parse tree produced by the {@code String}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitString(SimpleParser.StringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInteger(SimpleParser.IntegerContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInteger(SimpleParser.IntegerContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Not}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNot(SimpleParser.NotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNot(SimpleParser.NotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SimpleParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SimpleParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProcCall}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterProcCall(SimpleParser.ProcCallContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProcCall}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitProcCall(SimpleParser.ProcCallContext ctx);
	/**
	 * Enter a parse tree produced by the {@code And}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAnd(SimpleParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code And}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAnd(SimpleParser.AndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Pow}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPow(SimpleParser.PowContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Pow}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPow(SimpleParser.PowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterCompare(SimpleParser.CompareContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitCompare(SimpleParser.CompareContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterBoolean(SimpleParser.BooleanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitBoolean(SimpleParser.BooleanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryPM}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnaryPM(SimpleParser.UnaryPMContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryPM}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnaryPM(SimpleParser.UnaryPMContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(SimpleParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(SimpleParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(SimpleParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(SimpleParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#para_list}.
	 * @param ctx the parse tree
	 */
	void enterPara_list(SimpleParser.Para_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#para_list}.
	 * @param ctx the parse tree
	 */
	void exitPara_list(SimpleParser.Para_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#argu_list}.
	 * @param ctx the parse tree
	 */
	void enterArgu_list(SimpleParser.Argu_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#argu_list}.
	 * @param ctx the parse tree
	 */
	void exitArgu_list(SimpleParser.Argu_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#rtype}.
	 * @param ctx the parse tree
	 */
	void enterRtype(SimpleParser.RtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#rtype}.
	 * @param ctx the parse tree
	 */
	void exitRtype(SimpleParser.RtypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link SimpleParser#ptype}.
	 * @param ctx the parse tree
	 */
	void enterPtype(SimpleParser.PtypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link SimpleParser#ptype}.
	 * @param ctx the parse tree
	 */
	void exitPtype(SimpleParser.PtypeContext ctx);
}