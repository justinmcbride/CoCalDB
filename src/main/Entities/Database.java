package main.Entities;

import main.*;
import main.Structures.*;
import org.eclipse.jetty.server.Server;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by justinmcbride on 3/23/16.
 * Defines the singleton instance for controlling access to the CoCal database
 */
public class Database {
    private Path m_root_path;
    private boolean m_initialized;
    Server m_server;


//    private Map<id, Calendar> m_collection_calendars;
//    private Map<id, User> m_collection_users;
//    private Map<id, Event> m_collection_events;
//    private Map<id, Group> m_collection_groups;

    LazyList<Group> m_collection_groups;
    LazyList<Event> m_collection_events;
    LazyList<User> m_collection_users;
    LazyList<Calendar> m_collection_calendars;

    public boolean AddGroup( Group group ) {
        return m_collection_groups.Add( group );
    }

    public boolean AddUser( User user ) {
        return m_collection_users.Add( user );
    }

    public boolean AddCalendar( Calendar calendar ) {
        return m_collection_calendars.Add( calendar );
    }

    public boolean AddEvent( Event event ) {
        return m_collection_events.Add( event );
    }



    public Database(Path rootLocation) {
        m_root_path = rootLocation;
        dbThrd.m_root_path = m_root_path;
        m_initialized = false;
    }

    public boolean Initialize() {
        if( Files.notExists( m_root_path ) ) {
            if( CreateRoot() ) m_initialized = true;
        }
        else {
            // traverse and create db in memory
        }

        m_collection_groups = new LazyList<>();
        m_collection_events = new LazyList<>();
        m_collection_users = new LazyList<>();
        m_collection_calendars = new LazyList<>();

        m_server = new Server(3000);
        m_server.setHandler(new dbHandlerThrd());
        try {
            m_server.start();
            m_server.join();
        }
        catch (Exception e){
            System.out.println("Error starting server:" + e);
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
