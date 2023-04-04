package com.example.willsrollerdiscobmgui;

import javafx.application.Platform;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class sceneSelector implements Initializable {
    @FXML
    Button homeButton, announcementsButton, ticketsButton, skateHireButton, maintenanceButton, startSessionButton,
            stopSessionButton;
    @FXML
    Label currentStatusText;

    @FXML
    TextArea newAnnouncementText, ticketsTextBox;

    private Stage stage;
    private static Scene scene;
    private Parent root;

    boolean sessionStarted = false;


    @FXML
    ListView<String> CAListView;

   @FXML
   ListView<Skate> SHAnnounceListView;
   @FXML
   static
   List<Skate> announcements = new ArrayList<>();
   @FXML
    ListView<String> SHListView;
   @FXML
    ListView<String> CTListView;

    static String resourceName;
    static String lockedBy = "BusinessManagementHub";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
    @FXML
    public void switchToHome(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //check if session has started
        //checkForSession();
    }

    @FXML
    public void switchToAnnouncements(ActionEvent event) throws IOException, SQLException {

        root = FXMLLoader.load(getClass().getResource("announcements.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Timer reloadAnnouncements = new Timer();
        reloadAnnouncements.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    try {
                        ListView lv = (ListView) scene.lookup("#CAListView");
                        listViews.loadAnnouncementsListView(lv);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 2000);

    }

    @FXML
    public void switchToTickets(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView lv = (ListView) scene.lookup("#CTListView");
                        listViews.loadTicketsListView(lv);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 2000);
    }
    @FXML
    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skatehire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SkateHireReloader();
    }

    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        maintenanceReloader();
    }

    public void checkForSession() throws SQLException {
        boolean session = DBConnect.checkForSessionStart();

        //set values
        if(session){
            System.out.println(sessionStarted);
            //this.startSessionButton.setVisible(false);
            //System.out.println(sessionStarted);
            //this.stopSessionButton.setVisible(true);
        }
       else {
            //currentStatusText.setText("Session Stopped");
            //this.stopSessionButton.setVisible(false);
            //this.startSessionButton.setVisible(true);
            //this.startSessionButton.setVisible(true);
        }
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
    public void stopSession(ActionEvent event) throws IOException {
        sessionStarted = false;
        System.out.println(sessionStarted);
        currentStatusText.setText("Session Stopped");
        this.stopSessionButton.setVisible(false);
        this.startSessionButton.setVisible(true);
        //Move current session to previous session
        DBConnect.moveToPreviousSessions();
    }

    public void createAnnouncement(ActionEvent event) throws IOException {
        //get text entered
        //currently returning null pointer error
        if (newAnnouncementText.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            String annText = newAnnouncementText.getText();
            System.out.println("Text Found");
            DBConnect insertAnnounce = new DBConnect();
            DBConnect.insertAnnouncement(annText);
            newAnnouncementText.clear();
        }
    }

    public void createTicket(ActionEvent event) throws IOException, SQLException {
        resourceName = "tickets";
        if (ticketsTextBox.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            locks.lock(resourceName, lockedBy);
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            DBConnect insertTicket = new DBConnect();
            DBConnect.insertTicket(ticketText);
            ticketsTextBox.clear();
            locks.unlock(resourceName,lockedBy);
        }
    }

    public void selectTicket(){
        System.out.println("Clicked");
        //ListView lv = (ListView) scene.lookup("#CTListView");
        // tring selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        //ticketsTextBox.setText(selectedValue);
    }

    public void selectMaintenance(){
        System.out.println("Clicked");
    }

    public void deleteTicket(){
        System.out.println("Delete");
        String selectedValue = ticketsTextBox.getText();
        DBConnect.deleteTicket(selectedValue);
    }

    public void deleteAnnouncement() throws SQLException {
        System.out.println("Delete");
        try {
            String selectedValue = newAnnouncementText.getText();
            DBConnect.deleteAnnouncement(selectedValue);
            newAnnouncementText.clear();
            errors.deleteComplete();

        }
        catch(Exception e) {
            errors.deleteNotComplete();
        }
    }

    public void SkateHireReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            listViews.loadSkateHireListView(lv);
                            System.out.println("Skate Hire Reload");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second

    }

    public static ObservableList<Skate> loadSkateHire(ListView lv) throws SQLException {
        ObservableList<Skate> data = DBConnect.loadSkates();

        // create table columns
        TableColumn<Skate, String> skateSizeCol = new TableColumn<>("Skate Size");
        skateSizeCol.setCellValueFactory(new PropertyValueFactory<>("skateSize"));

        TableColumn<Skate, String> skateAmountCol = new TableColumn<>("Skate Amount");
        skateAmountCol.setCellValueFactory(new PropertyValueFactory<>("skateAmount"));

        // create table view and set data and columns
        TableView<Skate> tableView = new TableView<>();
        tableView.setItems(data);
        tableView.getColumns().addAll(skateSizeCol, skateAmountCol);

        // get the existing list view and replace it with the table view
        lv.getItems().clear();
        lv.setCellFactory(tv -> new ListCell<Skate>() {
            @Override
            protected void updateItem(Skate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label label = new Label( "Skate Size: " + item.getSkateSize() + "\nSkates Available: " + item.getSkateAmount());
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll(label);
                    hbox.setSpacing(15);
                    setGraphic(hbox);
                    if (item.getSkateAmount() > 10) {
                        setStyle("-fx-background-color:#48992B;");
                        label.setTextFill(Color.web("BEBEBE"));
                    } else if (item.getSkateAmount() <= 10 && item.getSkateAmount() > 5) {
                        setStyle("-fx-background-color: #F7AE2C;");
                        label.setTextFill(Color.web("2D2D2D"));
                    } else if (item.getSkateAmount() <= 5) {
                        setStyle("-fx-background-color: #FA3837;");
                        label.setTextFill(Color.web("BEBEBE"));
                        addToAnnouncement(item);
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        reloadAnnouncementsListView(lv);
        return data;
    }

    public static void reloadAnnouncementsListView(ListView lv) {
        if (lv.getItems().isEmpty()) {
            ListView<Skate> SHAnnounceListView = (ListView<Skate>) scene.lookup("#SHAnnounceListView");
            SHAnnounceListView.getItems().clear(); // clear the list
            SHAnnounceListView.setCellFactory(tv -> new ListCell<Skate>() {
                @Override
                protected void updateItem(Skate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label label = new Label( "Skate Size: " + item.getSkateSize() + "\nSkates Available: " + item.getSkateAmount());
                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(label);
                        hbox.setSpacing(15);
                        setGraphic(hbox);
                    }
                }
            });
            SHAnnounceListView.getItems().addAll(announcements); // reload the announcements list
            announcements.clear(); // clear the announcements
        }
        // get the existing list view and replace it with the table view
    }

    public static void addToAnnouncement(Skate item) {

        // get the existing list view and replace it with the table view
        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHAnnounceListView");
        if (lv.getItems().isEmpty()) {
            lv.getItems().add(item);
            lv.setCellFactory(tv -> new ListCell<Skate>() {
                @Override
                protected void updateItem(Skate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Label label = new Label("Skate Size: " + item.getSkateSize() + "\nSkates Available: " + item.getSkateAmount());
                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(label);
                        hbox.setSpacing(15);
                        setGraphic(hbox);
                    }
                }
            });
        }
    }

    public void selectAnnouncement(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#CAListView");
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        newAnnouncementText.setText(selectedValue);
    }

    public void maintenanceReloader() {
        Timer maintenanceTimer = new Timer();
        maintenanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                        if (lv != null) {
                            listViews.loadMaintenanceListView(lv);
                            System.out.println("Maintenance Reload");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second

    }

}