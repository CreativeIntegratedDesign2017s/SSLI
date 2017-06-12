// Generated from C:/Users/philg/SSLI/src\SimpleParser.g4 by ANTLR 4.6
package ANTLR;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SimpleParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SimpleParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SimpleParser#prgm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrgm(SimpleParser.PrgmContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Statement}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(SimpleParser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Procedure}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcedure(SimpleParser.ProcedureContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Import}
	 * labeled alternative in {@link SimpleParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport(SimpleParser.ImportContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#stmt_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt_list(SimpleParser.Stmt_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Evaluate}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluate(SimpleParser.EvaluateContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Declare}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclare(SimpleParser.DeclareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(SimpleParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IfElse}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfElse(SimpleParser.IfElseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DoWhile}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoWhile(SimpleParser.DoWhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code WhileDo}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileDo(SimpleParser.WhileDoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Return}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturn(SimpleParser.ReturnContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Nested}
	 * labeled alternative in {@link SimpleParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNested(SimpleParser.NestedContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Substring}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubstring(SimpleParser.SubstringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Bracket}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBracket(SimpleParser.BracketContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Or}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr(SimpleParser.OrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMulDiv(SimpleParser.MulDivContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAddSub(SimpleParser.AddSubContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Subscript}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubscript(SimpleParser.SubscriptContext ctx);
	/**
	 * Visit a parse tree produced by the {@code String}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(SimpleParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Integer}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(SimpleParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Not}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(SimpleParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Identifier}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(SimpleParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ProcCall}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProcCall(SimpleParser.ProcCallContext ctx);
	/**
	 * Visit a parse tree produced by the {@code And}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd(SimpleParser.AndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Pow}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPow(SimpleParser.PowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Compare}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare(SimpleParser.CompareContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Boolean}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoolean(SimpleParser.BooleanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryPM}
	 * labeled alternative in {@link SimpleParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryPM(SimpleParser.UnaryPMContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(SimpleParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit(SimpleParser.InitContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#para_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPara_list(SimpleParser.Para_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#argu_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgu_list(SimpleParser.Argu_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#rtype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRtype(SimpleParser.RtypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link SimpleParser#ptype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPtype(SimpleParser.PtypeContext ctx);
}