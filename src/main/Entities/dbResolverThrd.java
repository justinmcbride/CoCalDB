package main.Entities;


import main.Structures.MicroMap;
import main.*;
import main.Exceptions.*;

import java.util.List;

/**
 *  Takes parsed requests and performs the required db operations
 */
public class dbResolverThrd  extends dbThrd  {
    private List<MicroMap<String, String>> m_edits;
    private List<String> m_data;
    private Integer m_dID; // document ID


    // constructor for event creation
    public dbResolverThrd(int tID, Op op, Col col, List<String> data){
        super(tID,op,col);
        m_data = data;
    }
    // constructor for editing a document attribute
    public dbResolverThrd(int tID, Op op, Col col, Integer dID , List<MicroMap<String, String>> edits){
        this(tID,op,col,dID);
        m_edits = edits;
    }
    // constructor for reading/deleting a document
    public dbResolverThrd(int tID, Op op, Col col, Integer dID){
        super(tID,op,col);
        m_dID = dID;
    }



    public void run() {
        switch(m_op){
            case CREATE: {
                try { create(); }
                catch (Exception e){
                    System.out.println("Create failed " + e);
                    e.printStackTrace();
                } break; }
            case EDIT: {
                try { edit(); }
                catch (NoSuchElementException e){
                    System.out.println("Edit failed " + e);
                    e.printStackTrace();
                    e.printStructure();
                } break; }
            case READ: {
                try { read(); }
                catch (NoSuchElementException e){
                    System.out.println("Read failed " + e);
                    e.printStackTrace();
                    e.printStructure();
                } break; }
            case DELETE: {
                try { delete();}
                catch (NoSuchElementException e) {
                    System.out.println("Delete failed " + e);
                    e.printStackTrace();
                    e.printStructure();
                }
            } break;
            case LINK: {
                try { link();}
                catch (Exception e) {
                    System.out.println("Link failed " + e);
                    e.printStackTrace();
                }
            } break;
            case UNLINK: {
                try { unlink();}
                catch (Exception e) {
                    System.out.println("Unlink failed" + e);
                    e.printStackTrace();
                }
            } break;
        }
    }


    private void create() {
        switch(m_col) {
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
    }

    private void edit() throws NoSuchElementException {
        switch (m_col) {
            case CALENDAR:{
                Calendar c = db.m_collection_calendars.get(m_dID);
                if( c!= null ) c.edit(m_edits);
                break;
            }
            case EVENT: {
                Event c = db.m_collection_events.get(m_dID);
                if( c!= null ) c.edit(m_edits);
                break;
            }
            case GROUP: {
                Group c = db.m_collection_groups.get(m_dID);
                if( c!= null ) c.edit(m_edits);
                break;
            }
            case USER: {
                User c = db.m_collection_users.get(m_dID);
                if( c!= null ) c.edit(m_edits);
                break;
            }
        }
    }
    private void readDocument(String col){

    }

    private void read() throws NoSuchElementException {
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
    }

    private void delete() throws NoSuchElementException {
        switch (m_col) {
            case CALENDAR: {
                Calendar c = db.m_collection_calendars.Remove(m_dID);
                if (c != null)
                    c.delete();
                break;
            }
            case EVENT: {
                Event c = db.m_collection_events.Remove(m_dID);
                if (c != null)
                    c.delete();
                break;
            }
            case GROUP: {
                Group c = db.m_collection_groups.Remove(m_dID);
                if (c != null)
                    c.delete();
                break;
            }
            case USER: {
                User c = db.m_collection_users.Remove(m_dID);
                if (c != null)
                    c.delete();
                break;
            }
        }
    }

    private void link() throws NoSuchElementException {
//        switch (m_col) {
//            case CALENDAR: {
//                Calendar c = db.m_collection_calendars.Remove(m_dID);
//                if( c != null )
//                    c.delete();
//                break;
//            }
//            case EVENT: {
//                Event c = db.m_collection_events.Remove(m_dID);
//                if( c != null )
//                    c.delete();
//                break;
//            }
//            case GROUP: {
//                Group c = db.m_collection_groups.Remove(m_dID);
//                if( c != null )
//                    c.delete();
//                break;
//            }
//            case USER: {
//                User c = db.m_collection_users.Remove(m_dID);
//                if( c != null )
//                    c.delete();
//                break;
//            }
//        }
    }
    private void unlink() throws NoSuchElementException {
    }
}
