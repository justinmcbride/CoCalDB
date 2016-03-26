package main.Entities;

import java.nio.file.Path;

/**
 *
 */
public abstract class dbThrd extends Thread {
    public static Path m_root_path;
    int m_threadID;
    public enum Operation {
        CREATE, EDIT, READ, DELETE
    }
    Operation m_op;

    public enum Collection {
        CALENDAR, EVENT, GROUP, USER
    }
    Collection m_col;

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
    protected dbThrd(int ID, Operation op){
       this(ID);
       m_op = op;
    }
    protected dbThrd(int ID, Operation op, Collection col){
        this(ID,op);
        m_col = col;
    }
//    protected dbThrd(int ID, Operation op, Collection col, Document doc){
//        this(ID,op,col);
//        m_doc = doc;
//    }
}