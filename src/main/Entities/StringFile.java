package main.Entities;

import main.Entities.File;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/23/16.
 * Concrete class for File<String>
 */
public class StringFile extends File<String> {
    public StringFile( String value, Path myPath ) {
        super( value, myPath );
    }

    public String ConvertValue(String s) throws ParseException {
//        throw new ParseException( "hello", 2 );
        return s;
    }
    //@Override
}
