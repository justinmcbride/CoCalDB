package main;

import main.Entities.DirectoryMaker;
import main.Entities.FloatFile;
import main.Entities.ReferenceList;
import main.Entities.StringFile;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Warren on 3/23/2016.
 * Defines the Event class for CoCal
 */
@javax.ws.rs.Path( "event" )
public class Event /* implements Commitable */ extends DirectoryMaker {
    // a unique id for each event to have
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }

    @GET
    @Produces( MediaType.TEXT_PLAIN )
    public String getIT() {
        return "wow no way";
    }

    private java.nio.file.Path m_filepath; // the path to this specific event's directory

    // the following are all attributes of the event
    private StringFile m_title;
    private FloatFile m_cost;
    private StringFile m_location;
    private StringFile m_description;
    private StringFile m_date;
    private StringFile m_category;

    public Event(java.nio.file.Path parentPath, List<String> data) {
        java.nio.file.Path path_events = parentPath.resolve( "events" );
        m_filepath = path_events.resolve( getNextID().toString() );
        CreateDirectory( m_filepath );
        System.out.println( "Path: " + m_filepath );

        m_title = new StringFile( data.get(0), m_filepath.resolve( "title" ) );
        m_cost = new FloatFile( data.get(1), m_filepath.resolve( "cost" ) );
        m_location = new StringFile( data.get(2), m_filepath.resolve( "location" ) );
        m_description = new StringFile( data.get(3), m_filepath.resolve( "description" ) );
        m_date = new StringFile( data.get(4), m_filepath.resolve( "date" ) );
        m_category = new StringFile( data.get(5), m_filepath.resolve( "category" ) );

    }
    static boolean initalize(){
        File folder = new File("your/path");
        return true;
    }
}