package main.Entities;


import main.Structures.MicroMap;
import main.*;

import java.util.List;

/**
 *  Takes parsed requests and performs the required db operations
 */
public class dbResolverThrd extends dbThrd {
    List<MicroMap<String, String>> m_edits;
    List<String> m_data;
    int m_dID; // document ID


    // constructor for event creation
    public dbResolverThrd(int tID, Op op, Col col, List<String> data){
        super(tID,op,col);
        m_data = data;
    }
    // constructor for editing a document attribute
    public dbResolverThrd(int tID, Op op, Col col, int dID , List<MicroMap<String, String>> edits){
        this(tID,op,col,dID);
        m_edits = edits;
    }
    // constructor for reading/deleting a document
    public dbResolverThrd(int tID, Op op, Col col, int dID){
        super(tID,op,col);
        m_dID = dID;
    }

    private void readDocument(String col){

    }

    public void run(){
        switch(m_op){
            case CREATE:{
                switch(m_col){
                    case CALENDAR:{
                        db.m_collection_calendars.Add(new Calendar(m_root_path, m_data)); break;
                    }
                    case EVENT: {
                        db.m_collection_events.Add(new Event(m_root_path, m_data)); break;
                    }
                    case GROUP: {
                        db.m_collection_groups.Add(new Group(m_root_path, m_data)); break;
                    }
                    case USER: {
                        db.m_collection_users.Add(new User(m_root_path, m_data)); break;
                    }
                }
            } break;
            case EDIT: {
                switch (m_col){
                    case CALENDAR:{
                        db.m_collection_calendars.get(m_dID).edit(m_edits); break;
                    }
                    case EVENT: {
                        db.m_collection_events.get(m_dID).edit(m_edits); break;
                    }
                    case GROUP: {
                        db.m_collection_groups.get(m_dID).edit(m_edits); break;
                    }
                    case USER: {
                        db.m_collection_users.get(m_dID).edit(m_edits); break;
                    }
                }
            } break;
            case READ: {
                switch (m_col) {
                    case CALENDAR: {
                        this.readDocument("calendars"); break;
                    }
                    case EVENT: {
                        this.readDocument("events"); break;
                    }
                    case GROUP: {
                        this.readDocument("groups"); break;
                    }
                    case USER: {
                        this.readDocument("users"); break;
                    }
                }
            } break;
            case DELETE: {
                switch (m_col) {
                    case CALENDAR: {
                        System.out.println("remove calendar " + m_dID);
                        db.m_collection_calendars.Remove(m_dID).delete(); break;
                    }
                    case EVENT: {
                        db.m_collection_events.Remove(m_dID).delete(); break;
                    }
                    case GROUP: {
                        db.m_collection_groups.Remove(m_dID).delete(); break;
                    }
                    case USER: {
                        db.m_collection_users.Remove(m_dID).delete(); break;
                    }
                }
            } break;
            case ADD: {
                switch (m_col) {
                    case CALENDAR: {
                        System.out.println("remove calendar " + m_dID);
                        db.m_collection_calendars.Remove(m_dID).delete(); break;
                    }
                    case EVENT: {
                        db.m_collection_events.Remove(m_dID).delete(); break;
                    }
                    case GROUP: {
                        db.m_collection_groups.Remove(m_dID).delete(); break;
                    }
                    case USER: {
                        db.m_collection_users.Remove(m_dID).delete(); break;
                    }
                }
            } break;

        }
    }

}
