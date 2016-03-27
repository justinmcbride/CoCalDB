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
    MicroMap<String, String> m_edit;
    List<String> m_data;
    String m_dID; // document ID


    // constructor for event creation
    public dbResolverThrd(int tID, Operation op, Collection col, List<String> data){
        super(tID,op,col);
        m_data = data;
    }
    // constructor for editing a document attribute
    public dbResolverThrd(int tID, Operation op, Collection col, String dID, MicroMap<String, String> edit){
        this(tID,op,col,dID);
        m_edit = edit;
    }
    // constructor for reading/deleting a document
    public dbResolverThrd(int tID, Operation op, Collection col, String dID){
        super(tID,op,col);
    }

    private void editDocument(String col){
        String attr = m_edit.getKey();
        Path editPath = m_root_path.resolve(col).resolve(m_dID).resolve(attr);
        //lock
        switch (attr){
            case "cost" :  new FloatFile(m_edit.getVal(), editPath); break;
            case "isadmin": new BooleanFile(m_edit.getVal(), editPath); break;
            default :  new StringFile(m_edit.getVal(), editPath); break;
        }
        //unlock
    }

    private void deleteDocument(String col){   // this needs to be much more verbose
        Path editPath = m_root_path.resolve(col);
        //lock
        try {delete(editPath);}
        catch (IOException e)
        {
            System.out.println("Unable to delete " + editPath.toString());
        }
        //unlock
    }
    private void readDocument(String col){   // this needs to be much more verbose
        Path editPath = m_root_path.resolve(m_dID).resolve(col);
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
                        //lock
                        new Calendar(m_root_path, m_data);
                        //unlock...........
                        break;
                    }
                    case EVENT: {
                        //lock
                        new Event(m_root_path, m_data);
                        //unlock...........
                        break;
                    }
                    case GROUP: {
                        //lock
                        new Group(m_root_path, m_data);
                        //unlock...........
                        break;
                    }
                    case USER: {
                        //lock
                        new User(m_root_path, m_data);
                        //unlock...........
                        break;
                    }
                }
            } break;
            case EDIT: {
                switch (m_col){
                    case CALENDAR:{
                        this.editDocument("calendars"); break;
                    }
                    case EVENT:{
                        this.editDocument("events"); break;
                    }
                    case GROUP:{
                        this.editDocument("groups"); break;
                    }
                    case USER:{
                        this.editDocument("users"); break;
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
                        this.deleteDocument("calendars"); break;
                    }
                    case EVENT: {
                        this.deleteDocument("events"); break;
                    }
                    case GROUP: {
                        this.deleteDocument("groups"); break;
                    }
                    case USER: {
                        this.deleteDocument("users"); break;
                    }
                }
            } break;
        }
    }

}
