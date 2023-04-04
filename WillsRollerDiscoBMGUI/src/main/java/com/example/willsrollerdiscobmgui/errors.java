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

}
