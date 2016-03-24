package Entities;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by justinmcbride on 3/23/16.
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

    Boolean CommitChange(T newValue) {
//        Files.write( m_fileLocation, )
        return true;
    }

    T ReadValue() {
        return m_value;
    }

}
