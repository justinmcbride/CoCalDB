package main;

import Entities.File;

/**
 * Created by Warren on 3/23/2016.
 */
public class User {
    File<String> name;
    File<String> email;
    File<String> password;
    File<Boolean> isadmin;
    File<ID> groups;
    File<ID> calendar;
}
