package main;

import main.Entities.FloatFile;
import main.Entities.StringFile;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by Warren on 3/23/2016.
 * Defines the Event class for CoCal
 */
public class Event /* implements Commitable */ {

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

    public Event( Path parentPath ) {
        Path path_events = parentPath.resolve( "events" );
        System.out.println( "Path in: " + path_events.toString() );
        m_filepath = path_events.resolve( getNextID().toString() );
        new File(m_filepath.toString()).mkdirs();                // placed here to not repeat with each attribute
        System.out.println( "Path: " + m_filepath );

        title = new StringFile( "titleTEST", m_filepath.resolve( "title" ) );
        cost = new FloatFile( 0.0f, m_filepath.resolve( "cost" ) );
        location = new StringFile( "locationTEST", m_filepath.resolve( "location" ) );
        description = new StringFile( "descTEST", m_filepath.resolve( "description" ) );
        date = new StringFile( "dateTEST", m_filepath.resolve( "date" ) );
        category = new StringFile( "categoryTEST", m_filepath.resolve( "category" ) );

    }
}