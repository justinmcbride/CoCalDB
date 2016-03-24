package main.Entities;

import main.Entities.File;
import main.Entities.id;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by justinmcbride on 3/24/16.
 * Concrete class for File<id>
 */
public class idFile extends File<main.Entities.id> implements Iterable<main.Entities.id>{
    private java.nio.file.Path idPath;

    //public idFile(Integer value, Path myPath) {}
    public idFile(id value, Path myPath, Path idPath ) {
        super( value, myPath );
        this.idPath = idPath;
    }

    public id ConvertValue(String s) throws ParseException {
        try {
            return new id(idPath.resolve(s));
        }
        catch( NumberFormatException e ) {
            throw new ParseException( "couldn't convert " + s + " to int", 0 );
        }
    }

    public class idReader {
        private BufferedReader reader;

        private idReader() {
            try {
                reader = Files.newBufferedReader(m_fileLocation);
            } catch (IOException e) {
                System.out.println("Error reading value: " + e);
            }
        }

        private boolean hasNext() {
            try {
                reader.mark(32);
                if (reader.readLine() == null) {
                    return false;
                } else {
                    reader.reset();
                    return true;
                }
            } catch (IOException e) {
                System.out.println("Error reading value: " + e);
                return false;
            }
        }

        public id next() {
            try {
                String nID = reader.readLine();
                return new main.Entities.id(idPath.resolve(nID));
            } catch (IOException e) {
                System.out.println("Error reading value: " + e);
                throw new NoSuchElementException();
            }
        }
    }

    public Iterator<main.Entities.id> iterator() {
        return new idIterator<>();
    }

    public class idIterator<id> implements Iterator<main.Entities.id> {
        private idReader reader;
        private idIterator(){
            reader = new idReader();
        }

        public boolean hasNext() {
            return true; //implement...
        }

        public main.Entities.id next(){
            //implement...;
            return reader.next();
        }

        public void remove() {
            //implement... if supported.
        }
    }
    /*@Override
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
    */
}
