package main.Entities;

import main.*;
import main.Structures.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.jsr356.decoders.IntegerDecoder;
import org.omg.PortableServer.LIFESPAN_POLICY_ID;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinmcbride on 3/23/16.
 * Defines the singleton instance for controlling access to the CoCal database
 */
public class Database {
    public static Database GetDB() {
        if( s_Database == null ) {
            s_Database = new Database();
        }
        return s_Database;
    }

    static Database s_Database = null;

    private Path m_root_path;
    private boolean m_initialized;
    Server m_server;

    public AbstractConcurrentList<Group> m_collection_groups;
    public AbstractConcurrentList<Event> m_collection_events;
    public AbstractConcurrentList<User> m_collection_users;
    public AbstractConcurrentList<Calendar> m_collection_calendars;

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

    private Database() {
        m_initialized = false;
    }

    public boolean Initialize( Path rootLocation, String listType ) {
        m_root_path = rootLocation;
        dbThrd.m_root_path = m_root_path;
        if (listType == "Lazy") {
            m_collection_groups = new LazyList<>();
            m_collection_events = new LazyList<>();
            m_collection_users = new LazyList<>();
            m_collection_calendars = new LazyList<>();
        }
        else if (listType == "LockFree"){
            m_collection_groups = new LockFreeList<>();
            m_collection_events = new LockFreeList<>();
            m_collection_users = new LockFreeList<>();
            m_collection_calendars = new LockFreeList<>();
        }

        if( Files.notExists( m_root_path ) ) {
            if( CreateRoot() ) m_initialized = true;
            System.out.println( "Root location IS NEW" );
        }
        else {
            System.out.println( "Root location exists" );
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream( m_root_path );
                for( Path item : stream ) {
                    // parse all the existing calendars
                    if( item.getFileName().toString().equals( "calendars" ) ) {
                        DirectoryStream<Path> stream_calendars = Files.newDirectoryStream( item );
                        for( Path item_calendar : stream_calendars ) {
                            int id = Integer.parseInt(item_calendar.getFileName().toString());
                            System.out.println( "id = " + id );
                            StringFile title = null;
                            ReferenceList events = null;
                            ReferenceList owner = null;
                            DirectoryStream<Path> stream_calendar =  Files.newDirectoryStream( item_calendar );
                            for( Path item_attribute : stream_calendar ) {
                                if( item_attribute.getFileName().toString().equals( "title" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    title = new StringFile( lines.get(0), item_attribute, true );
                                    System.out.println( "title: " + title.ReadValue() );
                                }
                                if( item_attribute.getFileName().toString().equals( "owners" ) ) {
                                    owner = new ReferenceList( item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "events" ) ) {
                                    events = new ReferenceList( item_attribute, true );
                                }
                            }
                            m_collection_calendars.Add( new Calendar( item_calendar, title, events, owner ) );
                        }
                    }
                }

            }
            catch( IOException e ) {
                System.out.println( "Couldn't open " + e.getMessage() + ": " + e.getCause() );
            }

        }

//        m_server = new Server(3000);
//        m_server.setHandler(new dbHandlerThrd());
//        try {
//            m_server.start();
//            m_server.join();
//        }
//        catch (Exception e){
//            System.out.println("Error starting server:" + e);
//        }
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
