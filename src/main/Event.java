package main;

import main.Entities.DirectoryMaker;
import main.Entities.FloatFile;
import main.Entities.ReferenceList;
import main.Entities.StringFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Warren on 3/23/2016.
 * Defines the Event class for CoCal
 */
public class Event /* implements Commitable */ extends DirectoryMaker {
    // a unique id for each event to have
    static Integer __id = 0;
    static String getNextID() {
        String s = __id.toString();
        __id++;
        return s;
    }

    private Path m_filepath; // the path to this specific event's directory

    // the following are all attributes of the event
    private StringFile title;
    private FloatFile cost;
    private StringFile location;
    private StringFile description;
    private StringFile date;
    private StringFile category;
    private ReferenceList owner;

    public Event( Path parentPath ) {
        Path path_events = parentPath.resolve( "events" );
        m_filepath = path_events.resolve( getNextID().toString() );
        CreateDirectory( m_filepath );
        System.out.println( "Path: " + m_filepath );

        title = new StringFile( "titleTEST", m_filepath.resolve( "title" ) );
        cost = new FloatFile( 0.0f, m_filepath.resolve( "cost" ) );
        location = new StringFile( "locationTEST", m_filepath.resolve( "location" ) );
        description = new StringFile( "descTEST", m_filepath.resolve( "description" ) );
        date = new StringFile( "dateTEST", m_filepath.resolve( "date" ) );
        category = new StringFile( "categoryTEST", m_filepath.resolve( "category" ) );
        owner = new ReferenceList( "justin", m_filepath.resolve( "owner" ) );

    }
    static boolean initalize(){
        File folder = new File("your/path");
        return true;
    }
}