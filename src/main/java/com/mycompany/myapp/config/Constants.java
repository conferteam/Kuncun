package com.mycompany.myapp.config;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final Integer NOT_DELETE = 0;
    public static final Integer DELETE = 1;
    //2017.8.22 jo
    
    public static final Integer TYPE_CREATE = 0;
    public static final Integer TYPE_INCREASE = 1;
    public static final Integer TYPE_DECREASE = 2;
    public static final Integer TYPE_DELETE = 3;
    //2017.8.22 jo
    
    
    private Constants() {
    }
}
