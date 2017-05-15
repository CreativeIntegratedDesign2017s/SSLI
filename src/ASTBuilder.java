import java.util.*;
import java.util.List;

public class ASTBuilder extends SimpleParserBaseListener {

    public List<ASTUnit> prgm;

    ASTBuilder() {
        prgm = new ArrayList<>();
    }

    public void enterPrgm(SimpleParser.PrgmContext ctx) {
        System.out.println("Hello!");
    }
}
