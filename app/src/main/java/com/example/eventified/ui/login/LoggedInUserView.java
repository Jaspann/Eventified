package com.example.eventified.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String email;
    private String domain;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {this.displayName = displayName;}

    String getDisplayName() {
        return displayName;
    }

    public void setUserInformation(String email)
    {

        String first = email.substring(0, email.indexOf('.'));
        first = first.substring(0, 1).toUpperCase() + first.substring(1);

        String last = email.substring(email.indexOf('.') + 1, email.indexOf('@'));
        last = last.substring(0, 1).toUpperCase() + last.substring(1);

        String domain = email.substring(email.indexOf('@') + 1);


        //Check if last char is a number, and delete the char if is
        int lastCharAskii = (int) last.charAt(last.length() - 1);

        if(lastCharAskii > 47 && lastCharAskii < 58)
        {
            last = last.substring(0,last.length() - 1);
        }

        this.displayName = first + " " + last;
        this.email = email;
        this.domain = domain;
    }
}