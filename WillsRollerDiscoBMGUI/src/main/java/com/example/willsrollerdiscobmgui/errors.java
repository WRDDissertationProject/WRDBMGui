package com.example.willsrollerdiscobmgui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class errors {

    public static Alert alertEmptyBox() {
        Alert emptyField = new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
        return emptyField;
    }

    public static Alert deleteComplete() {
        Alert deleteComplete = new Alert(Alert.AlertType.INFORMATION,"Record Successfully Deleted", ButtonType.CLOSE);
        return deleteComplete;
    }

    public static Alert deleteNotComplete() {
        Alert deleteNotComplete = new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
        return deleteNotComplete;
    }

    public static Alert emptyLogin(){
        Alert emptyLogin = new Alert(Alert.AlertType.ERROR, "Error: Username or Password cannot be empty", ButtonType.OK);
        return emptyLogin;
    }

    public static Alert emptyLoginBoth(){
        Alert emptyLogin = new Alert(Alert.AlertType.ERROR, "Error: Username and Password cannot be empty", ButtonType.OK);
        return emptyLogin;
    }

    public static Alert loginFailure() {
        Alert loginFailure = new Alert(Alert.AlertType.ERROR, "Error: Login data not found, please check your credentials and try again", ButtonType.OK);
        return loginFailure;
    }

    public static Alert maintenanceEmpty() {
        Alert maintenanceEmpty = new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, please check and try again", ButtonType.OK);
        return maintenanceEmpty;
    }
}
