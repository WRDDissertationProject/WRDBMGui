package com.example.willsrollerdiscobmgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class home {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
