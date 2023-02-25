package com.example.willsrollerdiscobmgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class sceneSelector implements Initializable {
    @FXML
    Button homeButton, announcementsButton, ticketsButton, skateHireButton, maintenanceButton, startSessionButton,
            stopSessionButton;
    @FXML
    Label currentStatusText;

    @FXML
    TextArea newAnnouncementText;

    @FXML
    ListView<String> CAListView;

    private Stage stage;
    private Scene scene;
    private Parent root;

    boolean sessionStarted = false;

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToAnnouncements(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("announcements.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Timer reloadAnnouncements = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    loadAnnouncement();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Schedule the task to run every 2 seconds
        reloadAnnouncements.schedule(task, 0, 2000);

    }

    public void loadAnnouncement() throws SQLException {
        List<String> announcementsData = DBConnect.loadAnnouncement();
        ObservableList<String> items = FXCollections.observableArrayList(announcementsData);
        ListView lv = (ListView) scene.lookup("#CAListView");
        lv.setItems(items);
    }

    @FXML
    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skatehire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void startSession(ActionEvent event) throws IOException {
        //date testing
        currentStatusText.setText("Session Started: \n" + dateTime.fullDateTime());
        String fullDate = dateTime.fullDateTime();
        String date = dateTime.justDate();
        String time = dateTime.justTime();

        System.out.println(fullDate);
        System.out.println(date);
        System.out.println(time);
        DBConnect.insertCurrentSession(fullDate, date, time);

       this.startSessionButton.setVisible(false);
       sessionStarted = true;
       System.out.println(sessionStarted);
       this.stopSessionButton.setVisible(true);

        getStartingSkates startingSkates = new getStartingSkates();
        startingSkates.initialiseSkates();
    }

    @FXML
    public void stopSession(ActionEvent event) throws IOException{
        sessionStarted = false;
        System.out.println(sessionStarted);
        currentStatusText.setText("Session Stopped");
        this.stopSessionButton.setVisible(false);
        this.startSessionButton.setVisible(true);
        //Move current session to previous session
        //Drop current session
        DBConnect dropTable = new DBConnect();
        DBConnect.dropCurrentSession();
    }

    public void createAnnouncement(ActionEvent event) throws IOException {
      //get text entered
        //currently returning null pointer error
        if (newAnnouncementText.getText().isEmpty())
        {
            errors.alertEmptyBox().show();
        }
        else {
            String annText = newAnnouncementText.getText();
            System.out.println("Text Found");
            DBConnect insertAnnounce = new DBConnect();
            DBConnect.insertAnnouncement(annText);
            newAnnouncementText.clear();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
