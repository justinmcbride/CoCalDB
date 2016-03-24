package main.Entities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/23/16.
 * defines a generic class for encapsulating file operations
 * on files that store the variables of the CoCal primary classes
 */
public abstract class File<T> {
    Path m_fileLocation;
    T m_value;

    public File( T value, Path myPath )
    {
        m_value = value;
        m_fileLocation = myPath;
        System.out.println( "File: " + myPath );
    }

    public abstract T ConvertValue( String s ) throws ParseException;

    public void CommitChange() {
        try( BufferedWriter writer = Files.newBufferedWriter(m_fileLocation) ) {
            System.out.println( m_value.toString() );
            writer.write( m_value.toString() );
        }
        catch (Exception e) {
            System.out.println( e.getMessage() );
        }
        finally {
        }
    }

    public void Refresh() {
        try(BufferedReader reader = Files.newBufferedReader(m_fileLocation) ) {
            String line = null;
            line = reader.readLine();
            m_value = ConvertValue( line );
        }
        catch( ParseException e ) {
            System.out.println( "Couldn't convert value: " + e.getMessage() );
        }
        catch( IOException e )
        {
            System.out.println( "Error reading value: " + e.getMessage() );
        }
    }

    public T ReadValue( boolean refresh ) {
        if( refresh ) Refresh();
        return m_value;
    }

    public T ReadValue() {
        return ReadValue( false );
    }

    public void SetValue( T newValue, boolean commit ) {
        m_value = newValue;

        if( commit ) CommitChange();
    }

    public void SetValue( T newValue ) {
        SetValue( newValue, false );
    }

}
