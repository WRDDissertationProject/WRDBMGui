/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: errors.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *    methods used to show errors and messages to the user, they are used to make the application more user-friendly.
 *   */

//PACKAGES
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class errors {

    public static Alert alertEmptyBox() {
        return new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
    }

    public static Alert deleteNotComplete() {
        return new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
    }

    public static Alert emptyLogin(){
        return new Alert(Alert.AlertType.ERROR, "Error: Username or Password cannot be empty", ButtonType.OK);
    }

    public static Alert loginFailure() {
        return new Alert(Alert.AlertType.ERROR, "Error: Login data not found, please check your credentials and try again", ButtonType.OK);
    }

    public static Alert maintenanceEmpty() {
        return new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, please check and try again", ButtonType.OK);
    }
}
