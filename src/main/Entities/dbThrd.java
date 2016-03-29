package main.Entities;

import java.nio.file.Path;

/**
 *
 */
public abstract class dbThrd extends Thread {
    public static Path m_root_path;
    int m_threadID;
    static Database db = Database.GetDB();
    public enum Op {
        CREATE, EDIT, READ, DELETE
    }
    Op m_op;

    public enum Col {
        CALENDAR, EVENT, GROUP, USER
    }
    Col m_col;

//    protected enum Document {
//        title, cost, location, description, date,
//        category, name, email, isadmin, groups, calendar,
//        members, owner, events
//    }
//    Document m_doc;
//
//    protected docs(String s) {
//
//    }

    protected dbThrd() {}

    protected dbThrd(int ID) {
        m_threadID = ID;
    }
    protected dbThrd(int ID, Op op){
       this(ID);
       m_op = op;
    }
    protected dbThrd(int ID, Op op, Col col){
        this(ID,op);
        m_col = col;
    }
//    protected dbThrd(int ID, Op op, Col col, Document doc){
//        this(ID,op,col);
//        m_doc = doc;
//    }
}