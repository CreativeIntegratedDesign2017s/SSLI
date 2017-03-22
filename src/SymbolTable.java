/**
 * Created by Holim on 2017-03-21.
 */

public class SymbolTable {
    public final static int varDecl = 0;
    public final static int typeDecl = 1;
    public final static int procDecl = 2;

    int declType;
    String name;
    String type;
    SymbolTable prev;

    /* Constructor */
    public SymbolTable(int declType, String name, String type, SymbolTable prev) {
        this.declType = declType;
        this.name = name;
        this.type = type;
        this.prev = prev;
    }

    /* Procedure 이름은 Variable 이름과 겹치면 안되고,
     * Type 이름과 겹쳐도 되며 오버로딩이 가능하다.
     */
    public boolean checkProcRedundant(String name, String type) {
        if (this.name.equals(name)) {
            if (this.declType == varDecl)
                return true;
            if (this.declType == procDecl && this.type.equals(type))
                return true;
        }
        if (this.prev == null)
            return false;
        else
            return this.prev.checkProcRedundant(name, type);
    }

    public boolean checkProcDeclared(String name, String type) {
        if (this.declType == procDecl &&
            this.name.equals(name) &&
            this.type.equals(type))
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev.checkProcDeclared(name, type);
    }

    /* Type 이름은 Procedure 이름과 겹칠 수 있으나,
     * Variable 이름과 겹칠 수 없다.
     */
    public boolean checkTypeRedundant(String name) {
        if (this.name.equals(name)) {
            if (declType == varDecl || declType == typeDecl)
                return true;
        }
        if (this.prev == null)
            return false;
        else
            return this.prev.checkTypeRedundant(name);
    }

    public boolean checkTypeDeclared(String name) {
        if (this.declType == typeDecl && this.name.equals(name))
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev.checkTypeDeclared(name);
    }

    private boolean _checkVarGlobalRedundant(String name) {
        if (this.name.equals(name) && this.declType != varDecl)
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev._checkVarGlobalRedundant(name);
    }

    /* Variable 이름은, Block 외의 Variable 이름과 겹쳐도 된다.
     * 나머지는 모두 불가하다.
     */
    public boolean checkVarRedundant(String name, SymbolTable limit) {
        if (this == limit)
            return this._checkVarGlobalRedundant(name);
        else if (this.name.equals(name))
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev.checkVarRedundant(name, limit);
    }

    public boolean checkVarDeclared(String name) {
        if (this.declType == varDecl && this.name.equals(name))
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev.checkVarDeclared(name);
    }

    public boolean checkNameDeclared(String name) {
        if (this.name.equals(name))
            return true;
        else if (this.prev == null)
            return false;
        else
            return this.prev.checkNameDeclared(name);
    }

}
