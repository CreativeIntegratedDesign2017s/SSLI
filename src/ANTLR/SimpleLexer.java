// Generated from C:/Users/philg/SSLI/src\SimpleLexer.g4 by ANTLR 4.6
package ANTLR;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SimpleLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.6", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		SLC=1, LRB=2, RRB=3, LCB=4, RCB=5, LSB=6, RSB=7, CM=8, CL=9, SC=10, ASN=11, 
		ADD=12, SUB=13, MUL=14, DIV=15, POW=16, LT=17, GT=18, EQ=19, NEQ=20, LE=21, 
		GE=22, NOT=23, AND=24, OR=25, AMP=26, PROC=27, IF=28, THEN=29, ELSE=30, 
		DO=31, WHILE=32, END=33, RETURN=34, IMPORT=35, VOID=36, BOOL=37, INT=38, 
		STR=39, ID=40, WS=41;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"SLC", "LRB", "RRB", "LCB", "RCB", "LSB", "RSB", "CM", "CL", "SC", "ASN", 
		"ADD", "SUB", "MUL", "DIV", "POW", "LT", "GT", "EQ", "NEQ", "LE", "GE", 
		"NOT", "AND", "OR", "AMP", "PROC", "IF", "THEN", "ELSE", "DO", "WHILE", 
		"END", "RETURN", "IMPORT", "VOID", "BOOL", "INT", "STR", "ID", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'('", "')'", "'{'", "'}'", "'['", "']'", "','", "':'", "';'", 
		"'='", "'+'", "'-'", "'*'", "'/'", "'^'", "'<'", "'>'", "'=='", "'!='", 
		"'<='", "'>='", "'!'", "'&&'", "'||'", "'&'", "'proc'", "'if'", "'then'", 
		"'else'", "'do'", "'while'", "'end'", "'return'", "'import'", "'void'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "SLC", "LRB", "RRB", "LCB", "RCB", "LSB", "RSB", "CM", "CL", "SC", 
		"ASN", "ADD", "SUB", "MUL", "DIV", "POW", "LT", "GT", "EQ", "NEQ", "LE", 
		"GE", "NOT", "AND", "OR", "AMP", "PROC", "IF", "THEN", "ELSE", "DO", "WHILE", 
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


	public SimpleLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SimpleLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2+\u00fd\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\3\2"+
		"\3\2\3\2\7\2Z\n\2\f\2\16\2]\13\2\3\2\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3"+
		"\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3"+
		"\16\3\16\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3"+
		"\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3\27\3\30\3\30\3\31\3\31\3"+
		"\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3"+
		"\36\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\3 \3 \3 \3!\3!\3!\3!"+
		"\3!\3!\3\"\3\"\3\"\3\"\3#\3#\3#\3#\3#\3#\3#\3$\3$\3$\3$\3$\3$\3$\3%\3"+
		"%\3%\3%\3%\3&\3&\3&\3&\3&\3&\3&\3&\3&\5&\u00d6\n&\3\'\6\'\u00d9\n\'\r"+
		"\'\16\'\u00da\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\3(\7(\u00e9\n(\f(\16(\u00ec"+
		"\13(\3(\3(\3)\3)\7)\u00f2\n)\f)\16)\u00f5\13)\3*\6*\u00f8\n*\r*\16*\u00f9"+
		"\3*\3*\4[\u00ea\2+\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r"+
		"\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33"+
		"\65\34\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+\3\2\7\3\2\62;\5\2\f\f\17"+
		"\17^^\4\2C\\c|\5\2\62;C\\c|\5\2\13\f\17\17\"\"\u0107\2\3\3\2\2\2\2\5\3"+
		"\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2"+
		"\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3"+
		"\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63"+
		"\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2"+
		"?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3"+
		"\2\2\2\2M\3\2\2\2\2O\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\3U\3\2\2\2\5b\3\2\2"+
		"\2\7d\3\2\2\2\tf\3\2\2\2\13h\3\2\2\2\rj\3\2\2\2\17l\3\2\2\2\21n\3\2\2"+
		"\2\23p\3\2\2\2\25r\3\2\2\2\27t\3\2\2\2\31v\3\2\2\2\33x\3\2\2\2\35z\3\2"+
		"\2\2\37|\3\2\2\2!~\3\2\2\2#\u0080\3\2\2\2%\u0082\3\2\2\2\'\u0084\3\2\2"+
		"\2)\u0087\3\2\2\2+\u008a\3\2\2\2-\u008d\3\2\2\2/\u0090\3\2\2\2\61\u0092"+
		"\3\2\2\2\63\u0095\3\2\2\2\65\u0098\3\2\2\2\67\u009a\3\2\2\29\u009f\3\2"+
		"\2\2;\u00a2\3\2\2\2=\u00a7\3\2\2\2?\u00ac\3\2\2\2A\u00af\3\2\2\2C\u00b5"+
		"\3\2\2\2E\u00b9\3\2\2\2G\u00c0\3\2\2\2I\u00c7\3\2\2\2K\u00d5\3\2\2\2M"+
		"\u00d8\3\2\2\2O\u00dc\3\2\2\2Q\u00ef\3\2\2\2S\u00f7\3\2\2\2UV\7\61\2\2"+
		"VW\7\61\2\2W[\3\2\2\2XZ\13\2\2\2YX\3\2\2\2Z]\3\2\2\2[\\\3\2\2\2[Y\3\2"+
		"\2\2\\^\3\2\2\2][\3\2\2\2^_\7\f\2\2_`\3\2\2\2`a\b\2\2\2a\4\3\2\2\2bc\7"+
		"*\2\2c\6\3\2\2\2de\7+\2\2e\b\3\2\2\2fg\7}\2\2g\n\3\2\2\2hi\7\177\2\2i"+
		"\f\3\2\2\2jk\7]\2\2k\16\3\2\2\2lm\7_\2\2m\20\3\2\2\2no\7.\2\2o\22\3\2"+
		"\2\2pq\7<\2\2q\24\3\2\2\2rs\7=\2\2s\26\3\2\2\2tu\7?\2\2u\30\3\2\2\2vw"+
		"\7-\2\2w\32\3\2\2\2xy\7/\2\2y\34\3\2\2\2z{\7,\2\2{\36\3\2\2\2|}\7\61\2"+
		"\2} \3\2\2\2~\177\7`\2\2\177\"\3\2\2\2\u0080\u0081\7>\2\2\u0081$\3\2\2"+
		"\2\u0082\u0083\7@\2\2\u0083&\3\2\2\2\u0084\u0085\7?\2\2\u0085\u0086\7"+
		"?\2\2\u0086(\3\2\2\2\u0087\u0088\7#\2\2\u0088\u0089\7?\2\2\u0089*\3\2"+
		"\2\2\u008a\u008b\7>\2\2\u008b\u008c\7?\2\2\u008c,\3\2\2\2\u008d\u008e"+
		"\7@\2\2\u008e\u008f\7?\2\2\u008f.\3\2\2\2\u0090\u0091\7#\2\2\u0091\60"+
		"\3\2\2\2\u0092\u0093\7(\2\2\u0093\u0094\7(\2\2\u0094\62\3\2\2\2\u0095"+
		"\u0096\7~\2\2\u0096\u0097\7~\2\2\u0097\64\3\2\2\2\u0098\u0099\7(\2\2\u0099"+
		"\66\3\2\2\2\u009a\u009b\7r\2\2\u009b\u009c\7t\2\2\u009c\u009d\7q\2\2\u009d"+
		"\u009e\7e\2\2\u009e8\3\2\2\2\u009f\u00a0\7k\2\2\u00a0\u00a1\7h\2\2\u00a1"+
		":\3\2\2\2\u00a2\u00a3\7v\2\2\u00a3\u00a4\7j\2\2\u00a4\u00a5\7g\2\2\u00a5"+
		"\u00a6\7p\2\2\u00a6<\3\2\2\2\u00a7\u00a8\7g\2\2\u00a8\u00a9\7n\2\2\u00a9"+
		"\u00aa\7u\2\2\u00aa\u00ab\7g\2\2\u00ab>\3\2\2\2\u00ac\u00ad\7f\2\2\u00ad"+
		"\u00ae\7q\2\2\u00ae@\3\2\2\2\u00af\u00b0\7y\2\2\u00b0\u00b1\7j\2\2\u00b1"+
		"\u00b2\7k\2\2\u00b2\u00b3\7n\2\2\u00b3\u00b4\7g\2\2\u00b4B\3\2\2\2\u00b5"+
		"\u00b6\7g\2\2\u00b6\u00b7\7p\2\2\u00b7\u00b8\7f\2\2\u00b8D\3\2\2\2\u00b9"+
		"\u00ba\7t\2\2\u00ba\u00bb\7g\2\2\u00bb\u00bc\7v\2\2\u00bc\u00bd\7w\2\2"+
		"\u00bd\u00be\7t\2\2\u00be\u00bf\7p\2\2\u00bfF\3\2\2\2\u00c0\u00c1\7k\2"+
		"\2\u00c1\u00c2\7o\2\2\u00c2\u00c3\7r\2\2\u00c3\u00c4\7q\2\2\u00c4\u00c5"+
		"\7t\2\2\u00c5\u00c6\7v\2\2\u00c6H\3\2\2\2\u00c7\u00c8\7x\2\2\u00c8\u00c9"+
		"\7q\2\2\u00c9\u00ca\7k\2\2\u00ca\u00cb\7f\2\2\u00cbJ\3\2\2\2\u00cc\u00cd"+
		"\7v\2\2\u00cd\u00ce\7t\2\2\u00ce\u00cf\7w\2\2\u00cf\u00d6\7g\2\2\u00d0"+
		"\u00d1\7h\2\2\u00d1\u00d2\7c\2\2\u00d2\u00d3\7n\2\2\u00d3\u00d4\7u\2\2"+
		"\u00d4\u00d6\7g\2\2\u00d5\u00cc\3\2\2\2\u00d5\u00d0\3\2\2\2\u00d6L\3\2"+
		"\2\2\u00d7\u00d9\t\2\2\2\u00d8\u00d7\3\2\2\2\u00d9\u00da\3\2\2\2\u00da"+
		"\u00d8\3\2\2\2\u00da\u00db\3\2\2\2\u00dbN\3\2\2\2\u00dc\u00ea\7$\2\2\u00dd"+
		"\u00de\7^\2\2\u00de\u00e9\7$\2\2\u00df\u00e0\7^\2\2\u00e0\u00e9\7^\2\2"+
		"\u00e1\u00e2\7^\2\2\u00e2\u00e9\7p\2\2\u00e3\u00e4\7^\2\2\u00e4\u00e9"+
		"\7v\2\2\u00e5\u00e6\7^\2\2\u00e6\u00e9\7t\2\2\u00e7\u00e9\n\3\2\2\u00e8"+
		"\u00dd\3\2\2\2\u00e8\u00df\3\2\2\2\u00e8\u00e1\3\2\2\2\u00e8\u00e3\3\2"+
		"\2\2\u00e8\u00e5\3\2\2\2\u00e8\u00e7\3\2\2\2\u00e9\u00ec\3\2\2\2\u00ea"+
		"\u00eb\3\2\2\2\u00ea\u00e8\3\2\2\2\u00eb\u00ed\3\2\2\2\u00ec\u00ea\3\2"+
		"\2\2\u00ed\u00ee\7$\2\2\u00eeP\3\2\2\2\u00ef\u00f3\t\4\2\2\u00f0\u00f2"+
		"\t\5\2\2\u00f1\u00f0\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3"+
		"\u00f4\3\2\2\2\u00f4R\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6\u00f8\t\6\2\2"+
		"\u00f7\u00f6\3\2\2\2\u00f8\u00f9\3\2\2\2\u00f9\u00f7\3\2\2\2\u00f9\u00fa"+
		"\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\u00fc\b*\2\2\u00fcT\3\2\2\2\n\2[\u00d5"+
		"\u00da\u00e8\u00ea\u00f3\u00f9\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}