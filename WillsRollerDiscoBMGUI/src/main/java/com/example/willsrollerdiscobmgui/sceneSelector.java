/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: sceneSelector.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   All Methods that are not seperated and grouped, contains all the scene switches and all the methods that relate,
 *   such as scene selectors, actions and buttons etc.
 *   */

//PACKAGE
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.application.Platform;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class sceneSelector implements Initializable {
    private static final Logger log = Logger.getLogger(String.valueOf(DBConnect.class));
    @FXML
    Button startSessionButton, stopSessionButton;
    @FXML
    Label currentStatusText, glowSticksCurrentNumber, fGlowSticksCurrentNumber, skateLacesCurrentNumber,
            seasonalCurrentNumber, promotionalCurrentNumber, replacementCurrentNumber, skateHireCurrentNumber,
            ownSkatersCurrentNumber, spectatorsCurrentNumber, membersCurrentNumber, skateHireAverage, ownSkatersAverage,
            spectatorsAverage, membersAverage, mostPopularSkateSize, leastPopularSkateSize, skatesInMaintenance ;
    @FXML
    TextArea newAnnouncementText, ticketsTextBox, ticketsInfoBox, maintenanceText;
    private Stage stage;
    private static Scene scene;
    private Parent root;
    boolean sessionStarted = false;
    @FXML
    ListView<String> CAListView, SHListView,CTListView ;
    @FXML
    ListView<Skate> SHAnnounceListView;
    @FXML
    static
    List<Skate> announcements = new ArrayList<>();
    @FXML
    private TextField userNameInput;
    @FXML
    private PasswordField passwordInput;
    static String resourceName;
    static String lockedBy = "BusinessManagementHub";

    @Override
    public void initialize(URL location, ResourceBundle resources) {}
    @FXML
    public void switchToHome(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //check if session has started
        Platform.runLater(() -> {
            currentStatusText = (Label) root.lookup("#currentStatusText");
            // Get a reference to the Button elements
            startSessionButton = (Button) root.lookup("#startSessionButton");
            stopSessionButton = (Button) root.lookup("#stopSessionButton");

            // Check if session has started
            try {
                String sessionTime = DBConnect.getSessionStartTime();
                if (sessionChecker()) {
                    currentStatusText.setText("Started On" + sessionTime);
                    System.out.println(sessionStarted);
                    startSessionButton.setVisible(false);
                    stopSessionButton.setVisible(true);
                } else {
                    currentStatusText.setText(sessionTime);
                    stopSessionButton.setVisible(false);
                    startSessionButton.setVisible(true);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        reloaders.homeReloader(scene, root);
    }

    @FXML
    public void switchToAnnouncements(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("announcements.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.announcementReloader(scene);
    }

    @FXML
    public void switchToTickets(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.ticketsReloader(scene);
    }
    @FXML
    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skatehire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.SkateHireReloader(scene);
        reloaders.SkateHireAnnouncementsReloader(scene);
    }

    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.maintenanceReloader(scene);
    }

    @FXML
    public void switchToAddMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("createMaintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setMaintenanceTypeChoiceBox();
        setSkateSizeChoiceBox();
    }

    @FXML
    public void switchToSkateHireTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skateHireTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.skateHireTrendsReloader(scene, root);
    }

    public void switchToAdmissionTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admissionTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.admissionTrendsReloader(scene, root);
    }

    public void switchToExtraSalesTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("extraSalesTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.extraSalesReloader(scene, root);
    }

    public void switchToTransactionHistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("transactionHistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        reloaders.transactionHistoryReloader(scene);
    }

    public boolean sessionChecker() throws SQLException {
        return DBConnect.checkForSessionStart();
    }

    @FXML
    public void startSession() {
        //date testing
        currentStatusText.setText("Session Started: \n" + dateTime.fullDateTime());
        String fullDate = dateTime.fullDateTime();
        String date = dateTime.justDate();
        String time = dateTime.justTime();

        System.out.println(fullDate);
        System.out.println(date);
        System.out.println(time);
        DBConnect.insertCurrentSession(fullDate);

        this.startSessionButton.setVisible(false);
        sessionStarted = true;
        System.out.println(sessionStarted);
        this.stopSessionButton.setVisible(true);

        getStartingSkates startingSkates = new getStartingSkates();
        startingSkates.initialiseSkates();
    }
    @FXML
    public void stopSession() throws SQLException {
        sessionStarted = false;
        System.out.println(sessionStarted);
        currentStatusText.setText("Session Stopped");
        this.stopSessionButton.setVisible(false);
        this.startSessionButton.setVisible(true);
        //Move current session to previous session
        DBConnect.moveSkatesToPrevious();
        DBConnect.moveToPreviousSessions();
    }

    public void createAnnouncement() throws SQLException {
        //get text entered
        if (newAnnouncementText.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            String annText = newAnnouncementText.getText();
            System.out.println("Text Found");
            DBConnect insertAnnounce = new DBConnect();
            boolean success = DBConnect.insertAnnouncement(annText);

            if (success) {
                newAnnouncementText.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CAListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadAnnouncementsListView(lv);
                    System.out.println("Announcements Reload Create");
                }
            }

        }
    }

    public void createTicket() throws SQLException {
        resourceName = "tickets";
        if (ticketsTextBox.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            locks.lock(resourceName, lockedBy);
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            DBConnect insertTicket = new DBConnect();

            boolean success = DBConnect.insertTicket(ticketText);

            if (success) {
                ticketsTextBox.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketsListView(lv);
                    System.out.println("Tickets Reload Create");
                }
            }
            locks.unlock(resourceName,lockedBy);
        }
    }

    public void selectMaintenance(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#MListView");
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        String[] parts = selectedValue.split("\n");
        String maintenanceDetails = parts[0].substring("Details: ".length());
        maintenanceText.setText(maintenanceDetails);
    }

    public void deleteTicket(){
        System.out.println("Delete");
        resourceName = "tickets";
        try {
            locks.lock(resourceName, lockedBy);
            String selectedValue = ticketsTextBox.getText();
            boolean success = DBConnect.deleteTicket(selectedValue);

            if (success) {
                ticketsTextBox.clear();
                ticketsInfoBox.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketsListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
        }
    }


    public void deleteAnnouncement(){
        System.out.println("Delete");
        try {
            String selectedValue = newAnnouncementText.getText();
            boolean success = DBConnect.deleteAnnouncement(selectedValue);
            newAnnouncementText.clear();

            if (success) {
                newAnnouncementText .clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CAListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadAnnouncementsListView(lv);
                    System.out.println("Announcements Reload Delete");
                }
            }
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
        }
    }

    public void deleteMaintenance(){
        System.out.println("Delete");
        resourceName = "maintenance";
        try {
            locks.lock(resourceName, lockedBy);
            String selectedValue = maintenanceText.getText();
            String skateSize = DBConnect.getSkateSizeFromMaintenance(selectedValue);
            boolean success = DBConnect.deleteMaintenance(selectedValue, skateSize);

            if (success) {
                maintenanceText.clear();
                maintenanceText.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadMaintenanceListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
            log.log(Level.SEVERE,"Error Delete Maintenance Not Complete", e);
        }
    }

    public static ObservableList<Skate> loadSkateHire(ListView lv){
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
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        reloaders.SkateHireAnnouncementsReloader(scene);
        return data;
    }

    public void selectAnnouncement(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#CAListView");
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        newAnnouncementText.setText(selectedValue);
    }

    public void selectTransaction(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#THListView");
    }

    public void selectTicket(){
        ListView lv = (ListView) scene.lookup("#CTListView");
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();

        // Parse the selected value string to get the individual details
        String[] parts = selectedValue.split("\n");
        String ticketDetails = parts[0].substring("Ticket Details: ".length());
        String dateAndTime = parts[1];
        String postedBy = parts[2];

        // Set the individual details in separate text boxes
        ticketsTextBox.setText(ticketDetails);
        ticketsInfoBox.setText(dateAndTime + "\n\n" + postedBy);
        ticketsInfoBox.setWrapText(true);
    }

    public void loginChecker(ActionEvent event) throws SQLException, IOException {
        System.out.println("Clicked");
        //ListView lv = (ListView) scene.lookup("#CAListView");
        String userName = userNameInput.getText();
        String password = passwordInput.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            errors.emptyLogin().show();
        } else {

            if(DBConnect.checkLogin(userName, password)){
                switchToHome(event);
            }
            else {
                errors.loginFailure().show();
            }
        }
    }

    public void setMaintenanceTypeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        List<String> type = new ArrayList<>();
        type.add("Skate Hire");
        type.add("Games Equipment");
        type.add("Lighting Rig");
        type.add("DJ Equipment");
        type.add("Front Door");
        type.add("Other");
        choiceBox.getItems().addAll(type);
    }

    public void setSkateSizeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        List<String> skate = new ArrayList<>();

        skate.add(null);
        skate.add("C11");
        skate.add("C12");
        skate.add("C13");
        skate.add("1");
        skate.add("2");
        skate.add("3");
        skate.add("4");
        skate.add("5");
        skate.add("6");
        skate.add("7");
        skate.add("8");
        skate.add("9");
        skate.add("10");
        skate.add("11");
        skate.add("12");
        skate.add("13");
        choiceBox.getItems().addAll(skate);
    }

    public void createMaintenanceYesButton(){
      Button noButton = (Button) scene.lookup("#CMRNo");
      noButton.setVisible(true);

      Button yesButton = (Button) scene.lookup("#CMRYes");
      yesButton.setVisible(false);

      Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
      skateSizeLbl.setVisible(true);

      ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
      choiceBox.setVisible(true);
    }

    public void createMaintenanceNoButton(){
        Button yesButton = (Button) scene.lookup("#CMRYes");
        yesButton.setVisible(true);

        Button noButton = (Button) scene.lookup("#CMRNo");
        noButton.setVisible(false);

        Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
        skateSizeLbl.setVisible(false);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        choiceBox.setVisible(false);
    }

    public void createMaintenanceSubmit(){
        System.out.println("Clicked");

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        ChoiceBox<String> skateSize = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        TextArea maintenanceDetails = (TextArea) scene.lookup("#maintenanceDetails");

        //if null
        if (choiceBox == null || choiceBox.getValue() == null || choiceBox.getValue().isEmpty() || maintenanceDetails == null || maintenanceDetails.getText().isEmpty()){
            errors.maintenanceEmpty().show();
        }
        else {
            try {
                resourceName = "maintenance";
                locks.lock(resourceName,lockedBy);
                String typeIn = choiceBox.getValue();
                String details = maintenanceDetails.getText();
                String skateSizeIn = skateSize.getValue();
                DBConnect.insertMaintenance(typeIn, details, skateSizeIn);

                maintenanceDetails.clear();
                locks.unlock(resourceName,lockedBy);
            } catch (SQLException e) {
                System.out.println("Couldn't Add Maintenance Record");
                throw new RuntimeException(e);

            }

        }
    }
    public void selectSHT(){System.out.println("Clicked");}
}