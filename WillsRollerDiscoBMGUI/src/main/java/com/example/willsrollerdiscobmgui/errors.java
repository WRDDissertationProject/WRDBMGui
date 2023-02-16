package com.example.willsrollerdiscobmgui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
public class errors {

    public static Alert alertEmptyBox() {
        Alert emptyField = new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
        return emptyField;
    }

}
