package Entities;

import java.nio.file.Files;
import main.User;

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
        CommitChange( m_value );
    }

    public boolean CommitChange(T newValue) {
//        Files.write( m_fileLocation, )
        return true;
    }

    T ReadValue() {
        return m_value;
    }

}
