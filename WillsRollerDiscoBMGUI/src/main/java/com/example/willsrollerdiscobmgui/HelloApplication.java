package com.example.willsrollerdiscobmgui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
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