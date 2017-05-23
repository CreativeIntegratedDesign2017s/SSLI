import java.io.*;

public class ASTGraphLog extends ASTListener<Object> {
    private BufferedWriter bw;
    private int numNode;

    ASTGraphLog() throws IOException {
        bw = new BufferedWriter(new FileWriter("ASTGraph.log"));
    }

    public Object visitConstant(ASTConstant node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [shape=record, label=\"" + node.token.getText() + "\"]");
        }
        catch (Exception e) { System.err.println(e); }
        return null;
    }
    public Object visitVariable(ASTVariable node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [shape=record, label=\"" + node.token.getText() + "\"]");
        }
        catch (Exception e) { System.err.println(e); }
        return null;
    }
    public Object visitUnary(ASTUnary node)			{ return null; }
    public Object visitBinary(ASTBinary node)		{ return null; }
    public Object visitSubscript(ASTSubscript node)	{ return null; }
    public Object visitSubstring(ASTSubstring node)	{ return null; }
    public Object visitProcCall(ASTProcCall node)	{ return null; }

    public Object visitEval(ASTEval node)			{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"eval\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.expr.visit(this);
        }
        catch (Exception e) { System.err.println(e); }
        return null;
    }
    public Object visitDecl(ASTDecl node) {
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"decl\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            bw.write("\t" + String.valueOf(numNode++) + " [shape=record, label=\"");
            bw.write(node.type.tid.getText());
            for (Integer size : node.type.size)
                bw.write("[" + size.toString() + "]");
            bw.write("|");
            bw.write(node.var.getText() + "\"]\n");

            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            if (node.init != null)
                node.init.visit(this);
            else
                bw.write("\t" + String.valueOf(numNode++) + " [shape=record, label=\"default\"]\n");
        }
        catch (Exception e) { System.err.println(e); }
        return null;
    }
    public Object visitAsgn(ASTAsgn node)			{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"asgn\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.lval.visit(this);
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.rval.visit(this);
        }
        catch (Exception e) { System.err.println(e); }
        return null;
    }

    public Object visitStmtUnit(ASTStmtUnit node)	{
        return node.stmt.visit(this);
    }

    public Object visitPrgm(ASTPrgm prgm)			{
        try {
            numNode = 0;
            bw.write("graph {\n");
            bw.write("\tgraph [ranksep=0]\n");
            prgm.units.get(0).visit(this);
            bw.write("}\n");
            bw.close();
        }
        catch (IOException e) { System.err.println(e); }
        return null;
    }
}
