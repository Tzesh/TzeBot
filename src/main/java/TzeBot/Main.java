/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TzeBot;

import TzeBot.GUI.TzeGUI;
import java.sql.SQLException;
import javax.security.auth.login.LoginException;

/**
 *
 * @author Tzesh
 */
public class Main {
    public static void main(String args[]) throws LoginException, SQLException {
        TzeGUI tzegui = new TzeGUI();
        tzegui.start();
    }
}
