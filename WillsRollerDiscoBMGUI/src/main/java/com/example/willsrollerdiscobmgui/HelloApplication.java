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

/*Resources Used:
 * On Exit Code:  */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //loading the starting scene and setting window size
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root,800,600);

        //setting window related values, such as name, icon and scene
        stage.setTitle("Wills Roller Disco Business Management Portal");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        scene.setFill(null);
        stage.setScene(scene);
        stage.show();

        //on exit stops all process to ensure nothing continues running in the background.
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) throws SQLException {
        //When started, application connects to the core database
        DBConnect connect = new DBConnect();
        connect.connect();

        //After the database connection is established, the locks' connection is established
        locks locks = new locks();
        locks.connect();
        //all locks are reset so any hang-ups from previous are removed
        locks.resetLocks();

        //application launch
        launch();
    }
}