/**
 * Created by Holim on 2017-03-21.
 */

import java.util.*;

public class SymbolTable {

    /* Field Variables */
    HashMap<String, String> vars;
    HashMap<String, String> types;
    HashMap<String, HashMap<String, String>> procs;
    SymbolTable prev;

    /* Constructor */
    public SymbolTable(SymbolTable prev) {
        if (prev == null) {
            /* Global Scope */
            vars = new HashMap<String, String>();
            types = new HashMap<String, String>();
            procs = new HashMap<String, HashMap<String, String>>();
            this.prev = null;
        }
        else {
            /* Local Scope */
            vars = new HashMap<String, String>();
            types = null;
            procs = null;
            this.prev = prev;
        }
    }

    public void declVar(String name, String type) { vars.put(name, type); }

    public void declType(String name) { types.put(name, name); }

    public void declProc(String name, String ptype, String rtype) {
        HashMap<String, String> loads = procs.get(name);
        if (loads == null) {
            loads = new HashMap<String, String>();
            loads.put(ptype, rtype);
            procs.put(name, loads);
        }
        else loads.put(ptype, rtype);
    }

    public boolean hasVar(String name) { return (vars.get(name) != null); }

    public boolean hasType(String name) { return (types.get(name) != null); }

    public boolean hasProc(String name) { return (procs.get(name) != null); }

    public boolean hasProc(String name, String type) {
        if (procs.get(name) == null) return false;
        else if (procs.get(name).get(type) == null) return false;
        else return true;
    }

    public boolean isNameDeclared(String name) {
        if (this.hasVar(name)) return true;
        else if (prev == null) {
            if (this.hasType(name)) return true;
            else if (this.hasProc(name)) return true;
            else return false;
        }
        else return prev.isNameDeclared(name);
    }

    public void print() {
        System.out.println("\n-- Variables --");
        for (String key: vars.keySet()){
            System.out.println(key.toString() + " " + vars.get(key).toString());
        }
        System.out.println("--   Types   --");
        for (String key: types.keySet()){
            System.out.println(key.toString() + " " + (types.containsKey(key) ? types.get(key).toString() : ""));
        }
        System.out.println("--   Procs   --");
        for (String key1: procs.keySet()){
            for (String key2: procs.get(key1).keySet()) {
                System.out.println(key1.toString() + " " + key2.toString() + " "
                        + (types.containsKey(key2) ? types.get(key2).toString() : ""));
            }
        }
        System.out.print("---------------\n");
    }

}
