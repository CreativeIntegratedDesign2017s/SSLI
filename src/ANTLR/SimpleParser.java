package ANTLR;// Generated from C:/Users/philg/SSLI/src\SimpleParser.g4 by ANTLR 4.6
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SimpleParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.6", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SLC=1, LRB=2, RRB=3, LCB=4, RCB=5, LSB=6, RSB=7, CM=8, CL=9, SC=10, ASN=11, 
		ADD=12, SUB=13, MUL=14, DIV=15, POW=16, LT=17, GT=18, EQ=19, NEQ=20, LE=21, 
		GE=22, NOT=23, AND=24, OR=25, AMP=26, IF=27, THEN=28, ELSE=29, DO=30, 
		WHILE=31, END=32, RETURN=33, IMPORT=34, VOID=35, BOOL=36, INT=37, STR=38, 
		ID=39, WS=40;
	public static final int
		RULE_prgm = 0, RULE_unit = 1, RULE_block = 2, RULE_stmt_list = 3, RULE_stmt = 4, 
		RULE_expr = 5, RULE_type = 6, RULE_init = 7, RULE_para_list = 8, RULE_argu_list = 9, 
		RULE_rtype = 10, RULE_ptype = 11;
	public static final String[] ruleNames = {
		"prgm", "unit", "block", "stmt_list", "stmt", "expr", "type", "init", 
		"para_list", "argu_list", "rtype", "ptype"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", "','", "':'", "';'", 
		"'='", "'+'", "'-'", "'*'", "'/'", "'^'", "'<'", "'>'", "'=='", "'!='", 
		"'<='", "'>='", "'!'", "'&&'", "'||'", "'&'", "'if'", "'then'", "'else'", 
		"'do'", "'while'", "'end'", "'return'", "'import'", "'void'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SLC", "LRB", "RRB", "LCB", "RCB", "LSB", "RSB", "CM", "CL", "SC", 
		"ASN", "ADD", "SUB", "MUL", "DIV", "POW", "LT", "GT", "EQ", "NEQ", "LE", 
		"GE", "NOT", "AND", "OR", "AMP", "IF", "THEN", "ELSE", "DO", "WHILE", 
		"END", "RETURN", "IMPORT", "VOID", "BOOL", "INT", "STR", "ID", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "SimpleParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SimpleParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class PrgmContext extends ParserRuleContext {
		public List<UnitContext> unit() {
			return getRuleContexts(UnitContext.class);
		}
		public UnitContext unit(int i) {
			return getRuleContext(UnitContext.class,i);
		}
		public PrgmContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prgm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterPrgm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitPrgm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitPrgm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrgmContext prgm() throws RecognitionException {
		PrgmContext _localctx = new PrgmContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prgm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LRB) | (1L << LCB) | (1L << SC) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << IF) | (1L << DO) | (1L << WHILE) | (1L << RETURN) | (1L << IMPORT) | (1L << VOID) | (1L << BOOL) | (1L << INT) | (1L << STR) | (1L << ID))) != 0)) {
				{
				{
				setState(24);
				unit();
				}
				}
				setState(29);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnitContext extends ParserRuleContext {
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
	 
		public UnitContext() { }
		public void copyFrom(UnitContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ImportContext extends UnitContext {
		public TerminalNode IMPORT() { return getToken(SimpleParser.IMPORT, 0); }
		public TerminalNode STR() { return getToken(SimpleParser.STR, 0); }
		public ImportContext(UnitContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterImport(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitImport(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitImport(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StatementContext extends UnitContext {
		public StmtContext stmt() {
			return getRuleContext(StmtContext.class,0);
		}
		public StatementContext(UnitContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProcedureContext extends UnitContext {
		public RtypeContext rtype() {
			return getRuleContext(RtypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public Para_listContext para_list() {
			return getRuleContext(Para_listContext.class,0);
		}
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public ProcedureContext(UnitContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterProcedure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitProcedure(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitProcedure(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_unit);
		try {
			setState(41);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				_localctx = new StatementContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(30);
				stmt();
				}
				break;
			case 2:
				_localctx = new ProcedureContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(31);
				rtype();
				setState(32);
				match(ID);
				setState(33);
				match(LRB);
				setState(34);
				para_list();
				setState(35);
				match(RRB);
				setState(36);
				block();
				}
				break;
			case 3:
				_localctx = new ImportContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(38);
				match(IMPORT);
				setState(39);
				match(STR);
				setState(40);
				match(SC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BlockContext extends ParserRuleContext {
		public Stmt_listContext stmt_list() {
			return getRuleContext(Stmt_listContext.class,0);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_block);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43);
			match(LCB);
			setState(44);
			stmt_list();
			setState(45);
			match(RCB);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Stmt_listContext extends ParserRuleContext {
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public Stmt_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterStmt_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitStmt_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitStmt_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stmt_listContext stmt_list() throws RecognitionException {
		Stmt_listContext _localctx = new Stmt_listContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_stmt_list);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(47);
					stmt();
					}
					} 
				}
				setState(52);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StmtContext extends ParserRuleContext {
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
	 
		public StmtContext() { }
		public void copyFrom(StmtContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ReturnContext extends StmtContext {
		public TerminalNode RETURN() { return getToken(SimpleParser.RETURN, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public ReturnContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterReturn(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitReturn(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitReturn(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EvaluateContext extends StmtContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public EvaluateContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterEvaluate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitEvaluate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitEvaluate(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfElseContext extends StmtContext {
		public TerminalNode IF() { return getToken(SimpleParser.IF, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode THEN() { return getToken(SimpleParser.THEN, 0); }
		public List<Stmt_listContext> stmt_list() {
			return getRuleContexts(Stmt_listContext.class);
		}
		public Stmt_listContext stmt_list(int i) {
			return getRuleContext(Stmt_listContext.class,i);
		}
		public TerminalNode END() { return getToken(SimpleParser.END, 0); }
		public TerminalNode ELSE() { return getToken(SimpleParser.ELSE, 0); }
		public IfElseContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterIfElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitIfElse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitIfElse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NestedContext extends StmtContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public NestedContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterNested(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitNested(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitNested(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignContext extends StmtContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AssignContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterAssign(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitAssign(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitAssign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DoWhileContext extends StmtContext {
		public TerminalNode DO() { return getToken(SimpleParser.DO, 0); }
		public Stmt_listContext stmt_list() {
			return getRuleContext(Stmt_listContext.class,0);
		}
		public TerminalNode WHILE() { return getToken(SimpleParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode END() { return getToken(SimpleParser.END, 0); }
		public DoWhileContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterDoWhile(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitDoWhile(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitDoWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeclareContext extends StmtContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public InitContext init() {
			return getRuleContext(InitContext.class,0);
		}
		public DeclareContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterDeclare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitDeclare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitDeclare(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileDoContext extends StmtContext {
		public TerminalNode WHILE() { return getToken(SimpleParser.WHILE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode DO() { return getToken(SimpleParser.DO, 0); }
		public Stmt_listContext stmt_list() {
			return getRuleContext(Stmt_listContext.class,0);
		}
		public TerminalNode END() { return getToken(SimpleParser.END, 0); }
		public WhileDoContext(StmtContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterWhileDo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitWhileDo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitWhileDo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_stmt);
		int _la;
		try {
			setState(95);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				_localctx = new EvaluateContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LRB) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BOOL) | (1L << INT) | (1L << STR) | (1L << ID))) != 0)) {
					{
					setState(53);
					expr(0);
					}
				}

				setState(56);
				match(SC);
				}
				break;
			case 2:
				_localctx = new DeclareContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				type();
				setState(58);
				match(ID);
				setState(59);
				init();
				setState(60);
				match(SC);
				}
				break;
			case 3:
				_localctx = new AssignContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(62);
				expr(0);
				setState(63);
				match(ASN);
				setState(64);
				expr(0);
				setState(65);
				match(SC);
				}
				break;
			case 4:
				_localctx = new IfElseContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(67);
				match(IF);
				setState(68);
				expr(0);
				setState(69);
				match(THEN);
				setState(70);
				stmt_list();
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ELSE) {
					{
					setState(71);
					match(ELSE);
					setState(72);
					stmt_list();
					}
				}

				setState(75);
				match(END);
				}
				break;
			case 5:
				_localctx = new DoWhileContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(77);
				match(DO);
				setState(78);
				stmt_list();
				setState(79);
				match(WHILE);
				setState(80);
				expr(0);
				setState(81);
				match(END);
				}
				break;
			case 6:
				_localctx = new WhileDoContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(83);
				match(WHILE);
				setState(84);
				expr(0);
				setState(85);
				match(DO);
				setState(86);
				stmt_list();
				setState(87);
				match(END);
				}
				break;
			case 7:
				_localctx = new ReturnContext(_localctx);
				enterOuterAlt(_localctx, 7);
				{
				setState(89);
				match(RETURN);
				setState(91);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LRB) | (1L << ADD) | (1L << SUB) | (1L << NOT) | (1L << BOOL) | (1L << INT) | (1L << STR) | (1L << ID))) != 0)) {
					{
					setState(90);
					expr(0);
					}
				}

				setState(93);
				match(SC);
				}
				break;
			case 8:
				_localctx = new NestedContext(_localctx);
				enterOuterAlt(_localctx, 8);
				{
				setState(94);
				block();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public ExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SubstringContext extends ExprContext {
		public ExprContext Container;
		public ExprContext From;
		public ExprContext To;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public SubstringContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterSubstring(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitSubstring(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitSubstring(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BracketContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public BracketContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterBracket(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitBracket(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitBracket(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrContext extends ExprContext {
		public ExprContext Oprnd1;
		public ExprContext Oprnd2;
		public TerminalNode OR() { return getToken(SimpleParser.OR, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public OrContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterOr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitOr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MulDivContext extends ExprContext {
		public ExprContext Oprnd1;
		public Token op;
		public ExprContext Oprnd2;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public MulDivContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterMulDiv(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitMulDiv(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitMulDiv(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AddSubContext extends ExprContext {
		public ExprContext Oprnd1;
		public Token op;
		public ExprContext Oprnd2;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AddSubContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterAddSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitAddSub(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitAddSub(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SubscriptContext extends ExprContext {
		public ExprContext Container;
		public ExprContext Indexer;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public SubscriptContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterSubscript(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitSubscript(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitSubscript(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StringContext extends ExprContext {
		public TerminalNode STR() { return getToken(SimpleParser.STR, 0); }
		public StringContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IntegerContext extends ExprContext {
		public TerminalNode INT() { return getToken(SimpleParser.INT, 0); }
		public IntegerContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterInteger(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitInteger(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitInteger(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotContext extends ExprContext {
		public TerminalNode NOT() { return getToken(SimpleParser.NOT, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public NotContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterNot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitNot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdentifierContext extends ExprContext {
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public IdentifierContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProcCallContext extends ExprContext {
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public Argu_listContext argu_list() {
			return getRuleContext(Argu_listContext.class,0);
		}
		public ProcCallContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterProcCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitProcCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitProcCall(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndContext extends ExprContext {
		public ExprContext Oprnd1;
		public ExprContext Oprnd2;
		public TerminalNode AND() { return getToken(SimpleParser.AND, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AndContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitAnd(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PowContext extends ExprContext {
		public ExprContext Base;
		public ExprContext Exponent;
		public TerminalNode POW() { return getToken(SimpleParser.POW, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public PowContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterPow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitPow(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitPow(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CompareContext extends ExprContext {
		public ExprContext Oprnd1;
		public Token op;
		public ExprContext Oprnd2;
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public TerminalNode LT() { return getToken(SimpleParser.LT, 0); }
		public TerminalNode LE() { return getToken(SimpleParser.LE, 0); }
		public TerminalNode EQ() { return getToken(SimpleParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(SimpleParser.NEQ, 0); }
		public TerminalNode GE() { return getToken(SimpleParser.GE, 0); }
		public TerminalNode GT() { return getToken(SimpleParser.GT, 0); }
		public CompareContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterCompare(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitCompare(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitCompare(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BooleanContext extends ExprContext {
		public TerminalNode BOOL() { return getToken(SimpleParser.BOOL, 0); }
		public BooleanContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterBoolean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitBoolean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitBoolean(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryPMContext extends ExprContext {
		public Token op;
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public UnaryPMContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterUnaryPM(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitUnaryPM(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitUnaryPM(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr() throws RecognitionException {
		return expr(0);
	}

	private ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState);
		ExprContext _prevctx = _localctx;
		int _startState = 10;
		enterRecursionRule(_localctx, 10, RULE_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				_localctx = new IdentifierContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(98);
				match(ID);
				}
				break;
			case 2:
				{
				_localctx = new BooleanContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(99);
				match(BOOL);
				}
				break;
			case 3:
				{
				_localctx = new IntegerContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100);
				match(INT);
				}
				break;
			case 4:
				{
				_localctx = new StringContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(101);
				match(STR);
				}
				break;
			case 5:
				{
				_localctx = new ProcCallContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(102);
				match(ID);
				setState(103);
				match(LRB);
				setState(104);
				argu_list();
				setState(105);
				match(RRB);
				}
				break;
			case 6:
				{
				_localctx = new UnaryPMContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(107);
				((UnaryPMContext)_localctx).op = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
					((UnaryPMContext)_localctx).op = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(108);
				expr(9);
				}
				break;
			case 7:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(109);
				match(NOT);
				setState(110);
				expr(4);
				}
				break;
			case 8:
				{
				_localctx = new BracketContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(111);
				match(LRB);
				setState(112);
				expr(0);
				setState(113);
				match(RRB);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(149);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(147);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new PowContext(new ExprContext(_parentctx, _parentState));
						((PowContext)_localctx).Base = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(117);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(118);
						match(POW);
						setState(119);
						((PowContext)_localctx).Exponent = expr(8);
						}
						break;
					case 2:
						{
						_localctx = new MulDivContext(new ExprContext(_parentctx, _parentState));
						((MulDivContext)_localctx).Oprnd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(120);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(121);
						((MulDivContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==MUL || _la==DIV) ) {
							((MulDivContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(122);
						((MulDivContext)_localctx).Oprnd2 = expr(8);
						}
						break;
					case 3:
						{
						_localctx = new AddSubContext(new ExprContext(_parentctx, _parentState));
						((AddSubContext)_localctx).Oprnd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(123);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(124);
						((AddSubContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((AddSubContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(125);
						((AddSubContext)_localctx).Oprnd2 = expr(7);
						}
						break;
					case 4:
						{
						_localctx = new CompareContext(new ExprContext(_parentctx, _parentState));
						((CompareContext)_localctx).Oprnd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(126);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(127);
						((CompareContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LT) | (1L << GT) | (1L << EQ) | (1L << NEQ) | (1L << LE) | (1L << GE))) != 0)) ) {
							((CompareContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(128);
						((CompareContext)_localctx).Oprnd2 = expr(6);
						}
						break;
					case 5:
						{
						_localctx = new AndContext(new ExprContext(_parentctx, _parentState));
						((AndContext)_localctx).Oprnd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(129);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(130);
						match(AND);
						setState(131);
						((AndContext)_localctx).Oprnd2 = expr(4);
						}
						break;
					case 6:
						{
						_localctx = new OrContext(new ExprContext(_parentctx, _parentState));
						((OrContext)_localctx).Oprnd1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(132);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(133);
						match(OR);
						setState(134);
						((OrContext)_localctx).Oprnd2 = expr(3);
						}
						break;
					case 7:
						{
						_localctx = new SubscriptContext(new ExprContext(_parentctx, _parentState));
						((SubscriptContext)_localctx).Container = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(135);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(136);
						match(LSB);
						setState(137);
						((SubscriptContext)_localctx).Indexer = expr(0);
						setState(138);
						match(RSB);
						}
						break;
					case 8:
						{
						_localctx = new SubstringContext(new ExprContext(_parentctx, _parentState));
						((SubstringContext)_localctx).Container = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(140);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(141);
						match(LSB);
						setState(142);
						((SubstringContext)_localctx).From = expr(0);
						setState(143);
						match(CL);
						setState(144);
						((SubstringContext)_localctx).To = expr(0);
						setState(145);
						match(RSB);
						}
						break;
					}
					} 
				}
				setState(151);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public List<TerminalNode> INT() { return getTokens(SimpleParser.INT); }
		public TerminalNode INT(int i) {
			return getToken(SimpleParser.INT, i);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_type);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			match(ID);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LSB) {
				{
				{
				setState(153);
				match(LSB);
				setState(154);
				match(INT);
				setState(155);
				match(RSB);
				}
				}
				setState(160);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InitContext extends ParserRuleContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public InitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_init; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InitContext init() throws RecognitionException {
		InitContext _localctx = new InitContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_init);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ASN) {
				{
				setState(161);
				match(ASN);
				setState(162);
				expr(0);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Para_listContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(SimpleParser.VOID, 0); }
		public List<PtypeContext> ptype() {
			return getRuleContexts(PtypeContext.class);
		}
		public PtypeContext ptype(int i) {
			return getRuleContext(PtypeContext.class,i);
		}
		public List<TerminalNode> ID() { return getTokens(SimpleParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(SimpleParser.ID, i);
		}
		public Para_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_para_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterPara_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitPara_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitPara_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Para_listContext para_list() throws RecognitionException {
		Para_listContext _localctx = new Para_listContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_para_list);
		int _la;
		try {
			setState(179);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RRB:
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VOID) {
					{
					setState(165);
					match(VOID);
					}
				}

				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(168);
				ptype();
				setState(169);
				match(ID);
				setState(176);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CM) {
					{
					{
					setState(170);
					match(CM);
					setState(171);
					ptype();
					setState(172);
					match(ID);
					}
					}
					setState(178);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Argu_listContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(SimpleParser.VOID, 0); }
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public Argu_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argu_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterArgu_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitArgu_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitArgu_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Argu_listContext argu_list() throws RecognitionException {
		Argu_listContext _localctx = new Argu_listContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_argu_list);
		int _la;
		try {
			setState(192);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case RRB:
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==VOID) {
					{
					setState(181);
					match(VOID);
					}
				}

				}
				break;
			case LRB:
			case ADD:
			case SUB:
			case NOT:
			case BOOL:
			case INT:
			case STR:
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(184);
				expr(0);
				setState(189);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==CM) {
					{
					{
					setState(185);
					match(CM);
					setState(186);
					expr(0);
					}
					}
					setState(191);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RtypeContext extends ParserRuleContext {
		public TerminalNode VOID() { return getToken(SimpleParser.VOID, 0); }
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public RtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rtype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterRtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitRtype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitRtype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RtypeContext rtype() throws RecognitionException {
		RtypeContext _localctx = new RtypeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_rtype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_la = _input.LA(1);
			if ( !(_la==VOID || _la==ID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PtypeContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(SimpleParser.ID, 0); }
		public PtypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ptype; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).enterPtype(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SimpleParserListener ) ((SimpleParserListener)listener).exitPtype(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SimpleParserVisitor ) return ((SimpleParserVisitor<? extends T>)visitor).visitPtype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PtypeContext ptype() throws RecognitionException {
		PtypeContext _localctx = new PtypeContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_ptype);
		int _la;
		try {
			setState(206);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(196);
				match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(197);
				match(ID);
				setState(198);
				match(AMP);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(199);
				match(ID);
				setState(202); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(200);
					match(LSB);
					setState(201);
					match(RSB);
					}
					}
					setState(204); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LSB );
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 5:
			return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 8);
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 3);
		case 5:
			return precpred(_ctx, 2);
		case 6:
			return precpred(_ctx, 11);
		case 7:
			return precpred(_ctx, 10);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3*\u00d3\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\3\2\7\2\34\n\2\f\2\16\2\37\13\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3,\n\3\3\4\3\4\3\4\3\4\3\5\7\5\63\n\5\f\5"+
		"\16\5\66\13\5\3\6\5\69\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\5\6L\n\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\6\5\6^\n\6\3\6\3\6\5\6b\n\6\3\7\3\7\3\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7v\n\7\3\7"+
		"\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\7\7\u0096\n\7\f\7\16\7"+
		"\u0099\13\7\3\b\3\b\3\b\3\b\7\b\u009f\n\b\f\b\16\b\u00a2\13\b\3\t\3\t"+
		"\5\t\u00a6\n\t\3\n\5\n\u00a9\n\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u00b1\n\n"+
		"\f\n\16\n\u00b4\13\n\5\n\u00b6\n\n\3\13\5\13\u00b9\n\13\3\13\3\13\3\13"+
		"\7\13\u00be\n\13\f\13\16\13\u00c1\13\13\5\13\u00c3\n\13\3\f\3\f\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\6\r\u00cd\n\r\r\r\16\r\u00ce\5\r\u00d1\n\r\3\r\2\3"+
		"\f\16\2\4\6\b\n\f\16\20\22\24\26\30\2\6\3\2\16\17\3\2\20\21\3\2\23\30"+
		"\4\2%%))\u00ee\2\35\3\2\2\2\4+\3\2\2\2\6-\3\2\2\2\b\64\3\2\2\2\na\3\2"+
		"\2\2\fu\3\2\2\2\16\u009a\3\2\2\2\20\u00a5\3\2\2\2\22\u00b5\3\2\2\2\24"+
		"\u00c2\3\2\2\2\26\u00c4\3\2\2\2\30\u00d0\3\2\2\2\32\34\5\4\3\2\33\32\3"+
		"\2\2\2\34\37\3\2\2\2\35\33\3\2\2\2\35\36\3\2\2\2\36\3\3\2\2\2\37\35\3"+
		"\2\2\2 ,\5\n\6\2!\"\5\26\f\2\"#\7)\2\2#$\7\4\2\2$%\5\22\n\2%&\7\5\2\2"+
		"&\'\5\6\4\2\',\3\2\2\2()\7$\2\2)*\7(\2\2*,\7\f\2\2+ \3\2\2\2+!\3\2\2\2"+
		"+(\3\2\2\2,\5\3\2\2\2-.\7\6\2\2./\5\b\5\2/\60\7\7\2\2\60\7\3\2\2\2\61"+
		"\63\5\n\6\2\62\61\3\2\2\2\63\66\3\2\2\2\64\62\3\2\2\2\64\65\3\2\2\2\65"+
		"\t\3\2\2\2\66\64\3\2\2\2\679\5\f\7\28\67\3\2\2\289\3\2\2\29:\3\2\2\2:"+
		"b\7\f\2\2;<\5\16\b\2<=\7)\2\2=>\5\20\t\2>?\7\f\2\2?b\3\2\2\2@A\5\f\7\2"+
		"AB\7\r\2\2BC\5\f\7\2CD\7\f\2\2Db\3\2\2\2EF\7\35\2\2FG\5\f\7\2GH\7\36\2"+
		"\2HK\5\b\5\2IJ\7\37\2\2JL\5\b\5\2KI\3\2\2\2KL\3\2\2\2LM\3\2\2\2MN\7\""+
		"\2\2Nb\3\2\2\2OP\7 \2\2PQ\5\b\5\2QR\7!\2\2RS\5\f\7\2ST\7\"\2\2Tb\3\2\2"+
		"\2UV\7!\2\2VW\5\f\7\2WX\7 \2\2XY\5\b\5\2YZ\7\"\2\2Zb\3\2\2\2[]\7#\2\2"+
		"\\^\5\f\7\2]\\\3\2\2\2]^\3\2\2\2^_\3\2\2\2_b\7\f\2\2`b\5\6\4\2a8\3\2\2"+
		"\2a;\3\2\2\2a@\3\2\2\2aE\3\2\2\2aO\3\2\2\2aU\3\2\2\2a[\3\2\2\2a`\3\2\2"+
		"\2b\13\3\2\2\2cd\b\7\1\2dv\7)\2\2ev\7&\2\2fv\7\'\2\2gv\7(\2\2hi\7)\2\2"+
		"ij\7\4\2\2jk\5\24\13\2kl\7\5\2\2lv\3\2\2\2mn\t\2\2\2nv\5\f\7\13op\7\31"+
		"\2\2pv\5\f\7\6qr\7\4\2\2rs\5\f\7\2st\7\5\2\2tv\3\2\2\2uc\3\2\2\2ue\3\2"+
		"\2\2uf\3\2\2\2ug\3\2\2\2uh\3\2\2\2um\3\2\2\2uo\3\2\2\2uq\3\2\2\2v\u0097"+
		"\3\2\2\2wx\f\n\2\2xy\7\22\2\2y\u0096\5\f\7\nz{\f\t\2\2{|\t\3\2\2|\u0096"+
		"\5\f\7\n}~\f\b\2\2~\177\t\2\2\2\177\u0096\5\f\7\t\u0080\u0081\f\7\2\2"+
		"\u0081\u0082\t\4\2\2\u0082\u0096\5\f\7\b\u0083\u0084\f\5\2\2\u0084\u0085"+
		"\7\32\2\2\u0085\u0096\5\f\7\6\u0086\u0087\f\4\2\2\u0087\u0088\7\33\2\2"+
		"\u0088\u0096\5\f\7\5\u0089\u008a\f\r\2\2\u008a\u008b\7\b\2\2\u008b\u008c"+
		"\5\f\7\2\u008c\u008d\7\t\2\2\u008d\u0096\3\2\2\2\u008e\u008f\f\f\2\2\u008f"+
		"\u0090\7\b\2\2\u0090\u0091\5\f\7\2\u0091\u0092\7\13\2\2\u0092\u0093\5"+
		"\f\7\2\u0093\u0094\7\t\2\2\u0094\u0096\3\2\2\2\u0095w\3\2\2\2\u0095z\3"+
		"\2\2\2\u0095}\3\2\2\2\u0095\u0080\3\2\2\2\u0095\u0083\3\2\2\2\u0095\u0086"+
		"\3\2\2\2\u0095\u0089\3\2\2\2\u0095\u008e\3\2\2\2\u0096\u0099\3\2\2\2\u0097"+
		"\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\r\3\2\2\2\u0099\u0097\3\2\2\2"+
		"\u009a\u00a0\7)\2\2\u009b\u009c\7\b\2\2\u009c\u009d\7\'\2\2\u009d\u009f"+
		"\7\t\2\2\u009e\u009b\3\2\2\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0"+
		"\u00a1\3\2\2\2\u00a1\17\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00a4\7\r\2"+
		"\2\u00a4\u00a6\5\f\7\2\u00a5\u00a3\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\21"+
		"\3\2\2\2\u00a7\u00a9\7%\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9"+
		"\u00b6\3\2\2\2\u00aa\u00ab\5\30\r\2\u00ab\u00b2\7)\2\2\u00ac\u00ad\7\n"+
		"\2\2\u00ad\u00ae\5\30\r\2\u00ae\u00af\7)\2\2\u00af\u00b1\3\2\2\2\u00b0"+
		"\u00ac\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\u00b6\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5\u00a8\3\2\2\2\u00b5"+
		"\u00aa\3\2\2\2\u00b6\23\3\2\2\2\u00b7\u00b9\7%\2\2\u00b8\u00b7\3\2\2\2"+
		"\u00b8\u00b9\3\2\2\2\u00b9\u00c3\3\2\2\2\u00ba\u00bf\5\f\7\2\u00bb\u00bc"+
		"\7\n\2\2\u00bc\u00be\5\f\7\2\u00bd\u00bb\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf"+
		"\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2"+
		"\2\2\u00c2\u00b8\3\2\2\2\u00c2\u00ba\3\2\2\2\u00c3\25\3\2\2\2\u00c4\u00c5"+
		"\t\5\2\2\u00c5\27\3\2\2\2\u00c6\u00d1\7)\2\2\u00c7\u00c8\7)\2\2\u00c8"+
		"\u00d1\7\34\2\2\u00c9\u00cc\7)\2\2\u00ca\u00cb\7\b\2\2\u00cb\u00cd\7\t"+
		"\2\2\u00cc\u00ca\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00cf\u00d1\3\2\2\2\u00d0\u00c6\3\2\2\2\u00d0\u00c7\3\2"+
		"\2\2\u00d0\u00c9\3\2\2\2\u00d1\31\3\2\2\2\26\35+\648K]au\u0095\u0097\u00a0"+
		"\u00a5\u00a8\u00b2\u00b5\u00b8\u00bf\u00c2\u00ce\u00d0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}