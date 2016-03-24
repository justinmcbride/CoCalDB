package main.Entities;

import main.Entities.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/24/16.
 * Concrete class for File<Integer>
 */
public class idFile extends File<Integer> {
    public idFile(Integer value, Path myPath ) {
        super( value, myPath );
    }

    public Integer ConvertValue(String s) throws ParseException {
        try {
            return Integer.parseInt( s );
        }
        catch( NumberFormatException e ) {
            throw new ParseException( "couldn't convert " + s + " to int", 0 );
        }
    }

    @Override
    public void CommitChange() {
    }
    public void Refresh() {
        try(BufferedReader reader = Files.newBufferedReader(m_fileLocation) ) {
            String line = null;
            line = reader.readLine();
            m_value = ConvertValue( line );
        }
        catch( ParseException e ) {
            System.out.println( "Couldn't convert value: " + e );
        }
        catch( IOException e )
        {
            System.out.println( "Error reading value: " + e );
        }
    }
}
