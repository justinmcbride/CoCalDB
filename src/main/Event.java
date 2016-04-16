package main;

import main.Entities.FileHelper;
import main.Entities.FloatFile;
import main.Entities.StringFile;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by Warren on 3/23/2016.
 * Defines the Event class for CoCal
 */

public class Event extends Collection {
    // a unique id for each event to have
    private static AtomicInteger __id  = new AtomicInteger(0);
    private String getNextID() {
        Integer id = __id.getAndIncrement();
        String s = id.toString();
        m_id = id.intValue();
        return s;
    }

    public static void resetIDs(){
        __id = new AtomicInteger(0);
    }

    public Event( Path myPath, StringFile title, StringFile location, StringFile description, StringFile date, StringFile category, FloatFile cost ) {
        m_filepath = myPath;
        m_attributes.put( "title",  title );
        m_attributes.put( "location",  location );
        m_attributes.put( "description",  description );
        m_attributes.put( "date",  date );
        m_attributes.put( "category",  category );
        m_attributes.put( "cost",  cost );
    }

    public Event( Path parentPath, List<String> data ) {
        java.nio.file.Path path_events = parentPath.resolve( "events" );
        m_filepath = path_events.resolve( getNextID().toString() );
        FileHelper.CreateDirectory( m_filepath );
        System.out.println( "Path: " + m_filepath );

        m_attributes.put( "title", new StringFile( data.get(0), m_filepath.resolve( "title" ) ) );
        m_attributes.put( "cost",  new FloatFile( data.get(1), m_filepath.resolve( "cost" ) ) );
        m_attributes.put( "location", new StringFile( data.get(2), m_filepath.resolve( "location" ) ) );
        m_attributes.put( "description", new StringFile( data.get(3), m_filepath.resolve( "description" ) ) );
        m_attributes.put( "date", new StringFile( data.get(4), m_filepath.resolve( "date" ) ) );
        m_attributes.put( "category", new StringFile( data.get(5), m_filepath.resolve( "category" ) ) );
    }

    public void refresh() {

    }

    public String toString(){
        String ret = "";


        return ret;
    }
}