package Entities;

import java.nio.file.Path;

/**
 * Created by justinmcbride on 3/23/16.
 */
public class File<T> {
    Path m_fileLocation;
    T m_value;


    Boolean CommitChange(T newValue) {
        return true;
    }

    T ReadValue() {
        return m_value;
    }

}
