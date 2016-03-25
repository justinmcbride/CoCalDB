package main.Entities;

import main.*;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by justinmcbride on 3/23/16.
 * Defines the singleton instance for controlling access to the CoCal database
 */
public class Database {
    private Path m_root_path;
    private boolean m_initialized;

//    private Map<id, Calendar> m_collection_calendars;
//    private Map<id, User> m_collection_users;
//    private Map<id, Event> m_collection_events;
//    private Map<id, Group> m_collection_groups;

    public Database(Path rootLocation) {
        m_root_path = rootLocation;
        m_initialized = false;
    }

    public boolean Initialize() {
        if( Files.notExists( m_root_path ) ) {
            if( CreateRoot() ) m_initialized = true;
        }
        else {
            // traverse and create db in memory
        }
        return m_initialized;
    }

//    public Calendar GetCalendar( id id ) {
//        return m_collection_calendars.get(id);
//    }
//
//    public User GetUser( id id ) {
//        return m_collection_users.get(id);
//    }
//
//    public Event GetEvent( id id ) {
//        return m_collection_events.get(id);
//    }
//
//    public Group GetGroup( id id ) {
//        return m_collection_groups.get(id);
//    }

    private boolean CreateRoot() {
        System.out.println( "Creating new database at location: " + m_root_path.toString() );
        try {
            Files.createDirectories( m_root_path );
        }
        catch( FileAlreadyExistsException e ) {
            System.out.println( "Error creating database location( file already exists ): " + e );
            return false;
        }
        catch( IOException e ) {
            System.out.println( "Error creating database location: " + e );
            return false;
        }
        return true;
    }
}
