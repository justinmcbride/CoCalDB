package main;
import Entities.*;
import java.util.ArrayList;

/**
 * Created by Warren on 3/23/2016.
 * Defines the User class for cocal
 */

public class User {
    File<String> name;
    File<String> email;
    File<String> password;
    File<Boolean> isadmin;
    ArrayList<ID> groups;
    ID calendar;

}
