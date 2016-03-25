package main.Entities;

import java.nio.file.Path;
import java.text.ParseException;

/**
 * Created by justinmcbride on 3/24/16.
 */
public class ReferenceFile extends File<String> {
    @Override
    public String ConvertValue(String s) throws ParseException {
        return s;
    }

    ReferenceFile( Path myPath ) {
        super( "", myPath );
    }
}
