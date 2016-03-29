package main.Entities;


import main.Structures.MicroMap;
import main.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.Files.delete;

/**
 *  Takes parsed requests and performs the required db operations
 */
public class dbResolverThrd extends dbThrd {
    List<MicroMap<String, String>> m_edits;
    List<String> m_data;
    int m_dID; // document ID


    // constructor for event creation
    public dbResolverThrd(int tID, Operation op, Collection col, List<String> data){
        super(tID,op,col);
        m_data = data;
    }
    // constructor for editing a document attribute
    public dbResolverThrd(int tID, Operation op, Collection col, int dID , List<MicroMap<String, String>> edits){
        this(tID,op,col,dID);
        m_edits = edits;
    }
    // constructor for reading/deleting a document
    public dbResolverThrd(int tID, Operation op, Collection col, int dID){
        super(tID,op,col);
    }

    //each event must have a lock so it would be fine to do edit events in bulk
    private <T extends DirectoryMaker> void editDocument(T doc){
        doc.edit(m_edits);
    }

     private void deleteDocument(String col){   // this needs to be much more verbose
    }
    private void readDocument(String col){   // this needs to be much more verbose

        //Path editPath = m_root_path.resolve(m_dID).resolve(col);
//        Json ret;
        //lock???
        ///*IMPLEMENT WHEN NOT TIRED>!@>/ or when at Balmer peak*//
        //unlock
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
                        Calendar toRemove = (Calendar) db.m_collection_calendars.get(m_dID);
                        db.m_collection_calendars.Remove(toRemove); break;
                    }
                    case EVENT: {
                        Event toRemove = (Event) db.m_collection_events.get(m_dID);
                        db.m_collection_events.Remove(toRemove); break;
                    }
                    case GROUP: {
                        Group toRemove = (Group) db.m_collection_groups.get(m_dID);
                        db.m_collection_groups.Remove(toRemove); break;
                    }
                    case USER: {
                        User toRemove = (User) db.m_collection_users.get(m_dID);
                        db.m_collection_users.Remove(toRemove); break;
                    }
                }
            } break;
        }
    }

}
