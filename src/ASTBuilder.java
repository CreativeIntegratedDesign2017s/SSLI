import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.tree.*;

public class ASTBuilder extends SimpleParserBaseListener {

    public List<ASTUnit> prgm;

    ASTBuilder() {
        prgm = new ArrayList<>();
    }

    public void enterPrgm(SimpleParser.PrgmContext ctx) {
        System.out.println("Hello!");
    }
}

class ASTNode {
    public String errorData;
    public int localLine;
}


