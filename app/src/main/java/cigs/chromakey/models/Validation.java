package cigs.chromakey.models;

import android.widget.EditText;

import java.util.regex.Pattern;

/**
 *
 */
public class Validation {

    // Regular Expression
    // you can change the expression based on your need
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    //private static final String PHONE_REGEX = "\\d{3}-\\d{7}";

    // Error Messages
    public static final String ERR_EMAIL_MSG = "email invalido";
    //private static final String PHONE_MSG = "###-#######";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(String email, boolean required) {
        return isValid(email, EMAIL_REGEX, required);
    }

    // call this method when you need to check phone number validation
    //public static boolean isPhoneNumber(EditText editText, boolean required) {
    //    return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    //}

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(String txt, String regex, boolean required) {

        // text required and editText is blank, so return false
        if ( required && txt.isEmpty() ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, txt)) {
            return false;
        }

        return true;
    }

}
