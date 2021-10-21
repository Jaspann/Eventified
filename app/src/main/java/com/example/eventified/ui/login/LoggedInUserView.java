package com.example.eventified.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String email;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {this.displayName = displayName;}

    String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String email)
    {
/*
        String[] usernameParts = email.split(".");
        usernameParts[1] = usernameParts[1].substring(0,usernameParts[1].indexOf('@') - 1);

        int lastCharAskii = (int) usernameParts[1].charAt(usernameParts[1].length() - 1);

        //Check if last value is a number, and delete the char if is
        if(lastCharAskii > 47 && lastCharAskii < 58)
        {
                usernameParts[1] = usernameParts[1].substring(0,usernameParts[1].length() - 2);
        }
*/
        String username = email.substring(0,email.indexOf('@'));

        this.displayName = username;
    }
}