package TzeBot;

import TzeBot.gui.TzeGUI;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws LoginException, SQLException { // Database support will be added in future
        new TzeGUI().start(); // All of the functions and required things will be called and operated in TzeGUI.java
    }
}
