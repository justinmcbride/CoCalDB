package main;

import Entities.File;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/23/16.
 */
public class StringFile extends File<String> {
    @Override
    public String ConvertValue(String s) throws ParseException {
//        throw new ParseException( "hello", 2 );
        return s;
    }

    public StringFile( String value, Path myPath ) {
        super( value, myPath );
    }
}
