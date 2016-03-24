package main;
import Entities.File;

import java.nio.file.Path;

/**
 * Created by Warren on 3/23/2016.
 * Defines the Event class for Cocal
 */
public class Event /* implements Commitable */ {

    static Integer getID() {
        int i = 0;
        return i++;
    }

    Event( Path parentPath ) {
        m_filepath = parentPath.resolve("/events/").resolve( getID().toString() );
        title = new File<>( "mytitle", m_filepath.resolve("title") );
    }
    Path m_filepath;

    File<String> title;
    File<Float> cost;
    File<String> location;
    File<String> description;
    File<String> date;
    File<String> category;
}