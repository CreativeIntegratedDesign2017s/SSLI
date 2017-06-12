package AST;

import java.io.*;
import ANTLR.*;

public class ASTGraphLog extends ASTListener<Object> {
    private String filename;
    private BufferedWriter bw;
    private int numNode;

    public ASTGraphLog(String filename) throws IOException {
        this.filename = filename;
    }

    public Object visitConstant(ASTConstant node)	{
        int curNode = numNode++;
        try {
            if (node.token.getType() == SimpleParser.STR) {
                String str = node.token.getText();
                str = str.substring(1, node.token.getText().length() - 1);
                bw.write("\t" + String.valueOf(curNode) + " [shape=record, label=\"\\\"" + str + "\\\"\"]\n");
            }
            else
                bw.write("\t" + String.valueOf(curNode) + " [shape=record, label=\"" + node.token.getText() + "\"]\n");
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitVariable(ASTVariable node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [shape=record, label=\"" + node.token.getText() + "\"]\n");
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitUnary(ASTUnary node)			{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"" + node.op.getText() + "\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.oprnd.visit(this);
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitBinary(ASTBinary node)		{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"" + node.op.getText() + "\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.oprnd1.visit(this);
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.oprnd2.visit(this);
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitSubscript(ASTSubscript node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"arr[index]\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.arr.visit(this);
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.index.visit(this);
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitSubstring(ASTSubstring node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"str[idx1:idx2]\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.str.visit(this);
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.index1.visit(this);
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.index2.visit(this);
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }
    public Object visitProcCall(ASTProcCall node)	{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"call '" + node.pid.getText() + "'\"]\n");
            for (ASTExpr argu : node.param) {
                bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
                argu.visit(this);
            }
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }

    public Object visitEval(ASTEval node)			{
        int curNode = numNode++;
        try {
            bw.write("\t" + String.valueOf(curNode) + " [label=\"eval\"]\n");
            bw.write("\t" + String.valueOf(curNode) + "--" + String.valueOf(numNode) + "\n");
            node.expr.visit(this);
        }
        catch (Exception e) { System.err.println(e.getMessage()); }
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
        catch (Exception e) { System.err.println(e.getMessage()); }
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
        catch (Exception e) { System.err.println(e.getMessage()); }
        return null;
    }

    public Object visitStmtUnit(ASTStmtUnit node)	{
        return node.stmt.visit(this);
    }

    public Object visitPrgm(ASTPrgm prgm)			{
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            numNode = 0;
            bw.write("graph {\n");
            bw.write("\tgraph [ranksep=0]\n");
            prgm.units.get(0).visit(this);
            bw.write("}\n");
            bw.close();
        }
        catch (IOException e) { System.err.println(e.getMessage()); }
        return null;
    }
}
