package Entities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by justinmcbride on 3/23/16.
 * defines a generic constructing for encapsulating file operations
 */
public class File<T> {
    Path m_fileLocation;
    T m_value;

    public File( T value, Path myPath )
    {
        m_value = value;
        m_fileLocation = myPath;
    }

    public void SetValue( T newValue, boolean commit ) {
        m_value = newValue;

        if( commit ) CommitChange();
    }

    public void SetValue( T newValue ) {
        SetValue( newValue, false );
    }

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

    T ReadValue() {
        return m_value;
    }

}
