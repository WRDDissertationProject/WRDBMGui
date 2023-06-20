/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: getStartingSkates.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Starting method, used to launch the application, also initialises Database objects.
 *   */

//PACKAGE
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root,800,600);

        stage.setTitle("Wills Roller Disco Business Management Portal");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) throws SQLException {
        //Database Connection
        DBConnect connect = new DBConnect();
        connect.connect();

        locks locks = new locks();
        locks.connect();

        DBConnect.resetLocks();
        //Launches the app
        launch();
    }
}