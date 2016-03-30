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
    private String m_list_type;
    Server m_server;

    public AbstractConcurrentList<Group> m_collection_groups;
    public AbstractConcurrentList<Event> m_collection_events;
    public AbstractConcurrentList<User> m_collection_users;
    public AbstractConcurrentList<Calendar> m_collection_calendars;

    private Database() {
        m_initialized = false;
    }

    private void CreateCollections() {
        if( m_list_type.equals( "Lazy" ) ) {
            m_collection_groups = new LazyList<>();
            m_collection_events = new LazyList<>();
            m_collection_users = new LazyList<>();
            m_collection_calendars = new LazyList<>();
        }
        else if( m_list_type.equals( "LockFree" ) ) {
            m_collection_groups = new LockFreeList<>();
            m_collection_events = new LockFreeList<>();
            m_collection_users = new LockFreeList<>();
            m_collection_calendars = new LockFreeList<>();
        }
    }

    public boolean Initialize( Path rootLocation, String listType, boolean fresh ) {
        m_root_path = rootLocation;
        m_list_type = listType;
        dbThrd.m_root_path = m_root_path;
        
        if( fresh ) Drop(); // remove everything in the database
        else        CreateCollections();

        if( Files.notExists( m_root_path ) ) {
            if( CreateRoot() ) m_initialized = true;
            // System.out.println( "Root location IS NEW" );
        }
        else {
            // System.out.println( "Root location exists" );
            try {
                DirectoryStream<Path> stream = Files.newDirectoryStream( m_root_path );
                for( Path item : stream ) {
                    // parse all the existing calendars
                    if( item.getFileName().toString().equals( "calendars" ) ) {
                        DirectoryStream<Path> stream_calendars = Files.newDirectoryStream( item );
                        for( Path item_calendar : stream_calendars ) {
                            int id = Integer.parseInt(item_calendar.getFileName().toString());

                            StringFile title = null;
                            ReferenceList events = null;
                            ReferenceList owner = null;

                            DirectoryStream<Path> stream_calendar =  Files.newDirectoryStream( item_calendar );
                            for( Path item_attribute : stream_calendar ) {
                                if( item_attribute.getFileName().toString().equals( "title" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    title = new StringFile( lines.get(0), item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "owner" ) ) {
                                    owner = new ReferenceList( item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "events" ) ) {
                                    events = new ReferenceList( item_attribute, true );
                                }
                            }
                            m_collection_calendars.Add( new Calendar( item_calendar, title, events, owner ) );
                        }
                    } // end calendars
                    else if( item.getFileName().toString().equals( "groups" ) ) {
                        DirectoryStream<Path> stream_groups = Files.newDirectoryStream( item );
                        for( Path item_group : stream_groups ) {
                            int id = Integer.parseInt(item_group.getFileName().toString());

                            StringFile name = null;
                            ReferenceList members = null;
                            ReferenceList calendar = null;

                            DirectoryStream<Path> stream_calendar =  Files.newDirectoryStream( item_group );
                            for( Path item_attribute : stream_calendar ) {
                                if( item_attribute.getFileName().toString().equals( "name" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    name = new StringFile( lines.get(0), item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "members" ) ) {
                                    members = new ReferenceList( item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "calendar" ) ) {
                                    calendar = new ReferenceList( item_attribute, true );
                                }
                            }
                            m_collection_groups.Add( new Group( item_group, name, members, calendar ) );
                        }
                    } // end groups
                    else if( item.getFileName().toString().equals( "events" ) ) {
                        DirectoryStream<Path> stream_events = Files.newDirectoryStream( item );
                        for( Path item_event : stream_events ) {
                            int id = Integer.parseInt( item_event.getFileName().toString() );

                            StringFile title = null;
                            StringFile location = null;
                            StringFile description = null;
                            StringFile date = null;
                            StringFile category = null;
                            FloatFile cost = null;

                            DirectoryStream<Path> stream_calendar =  Files.newDirectoryStream( item_event );
                            for( Path item_attribute : stream_calendar ) {
                                if( item_attribute.getFileName().toString().equals( "title" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    title = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "location" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    location = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "description" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    description = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "date" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    date = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "category" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    category = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "title" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    cost = new FloatFile( lines.get(0), item_attribute, true );
                                }
                            }
                            m_collection_events.Add( new Event( item_event, title, location, description, date, category, cost ) );
                        }
                    } // end events
                    else if( item.getFileName().toString().equals( "users" ) ) {
                        DirectoryStream<Path> stream_events = Files.newDirectoryStream( item );
                        for( Path item_user : stream_events ) {
                            int id = Integer.parseInt( item_user.getFileName().toString() );

                            StringFile name = null;
                            StringFile email = null;
                            StringFile password = null;
                            BooleanFile isadmin = null;
                            ReferenceList groups = null;

                            DirectoryStream<Path> stream_calendar =  Files.newDirectoryStream( item_user );
                            for( Path item_attribute : stream_calendar ) {
                                if( item_attribute.getFileName().toString().equals( "name" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    name = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "email" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    email = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "password" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    password = new StringFile( lines.get(0), item_attribute, true );
                                }
                                else if( item_attribute.getFileName().toString().equals( "isadmin" ) ) {
                                    List<String> lines = Files.readAllLines( item_attribute );
                                    isadmin = new BooleanFile( lines.get(0), item_attribute, true );
                                }
                                if( item_attribute.getFileName().toString().equals( "groups" ) ) {
                                    groups = new ReferenceList( item_attribute, true );
                                }
                            }
                            m_collection_users.Add( new User( item_user, name, email, password, isadmin, groups ) );
                        }
                    } // end users
                } // end iterate over db
                m_initialized = true;
            }
            catch( IOException e ) {
                System.out.println( "Couldn't open " + e.getMessage() + ": " + e.getCause() );
                m_initialized = false;
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

    public boolean AddGroup( Group group ) {
        if( !m_initialized ) return false;
        return m_collection_groups.Add( group );
    }

    public boolean AddUser( User user ) {
        if( !m_initialized ) return false;
        return m_collection_users.Add( user );
    }

    public boolean AddCalendar( Calendar calendar ) {
        if( !m_initialized ) return false;
        return m_collection_calendars.Add( calendar );
    }

    public boolean AddEvent( Event event ) {
        if( !m_initialized ) return false;
        return m_collection_events.Add( event );
    }

    private boolean CreateRoot() {
//        System.out.println( "Creating new database at location: " + m_root_path.toString() );
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

    public boolean Drop() {
        if( m_root_path == null ) return false;
        CreateCollections();

        return DirectoryMaker.delete( m_root_path );
    }
}
