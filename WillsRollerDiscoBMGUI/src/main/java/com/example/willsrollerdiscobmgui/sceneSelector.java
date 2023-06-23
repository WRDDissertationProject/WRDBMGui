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

/*Resources Used:
 * Loggers:
 * Choice Boxes:
 * Custom List Views
 * Locks */

public class sceneSelector implements Initializable {
    //Initialising the logger
    private static final Logger log = Logger.getLogger(String.valueOf(DBConnect.class));
    //Setting Variables
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

    //Key variables for locks
    static String resourceName;
    static String lockedBy = "BusinessManagementHub";

    //Empty Initialise class, do not remove
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    //When the home tab button is pressed, is called
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
            //If there is a session the start button is removed, and the stop is displayed
            //Session time displayed for user

            //Else start button is displayed and stop button is not
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
        //Reloading automatically every second
        reloaders.homeReloader(scene, root);
    }

    //Started when Announcements tab is clicked
    @FXML
    public void switchToAnnouncements(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("announcements.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads the announcement details to reflect any new changes
        reloaders.announcementReloader(scene);
    }

    //Loaded when ticket tab is clicked
    @FXML
    public void switchToTickets(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads the tickets details to reflect any new changes
        reloaders.ticketsReloader(scene);
    }
    //Loaded when skate hire is clicked
    @FXML
    public void switchToSkateHire(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skatehire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads both the skate hire and the skate hire when there are less than 5 skates in a size
        reloaders.SkateHireReloader(scene);
        reloaders.SkateHireAnnouncementsReloader(scene);
    }

    //Loaded when maintenance is clicked
    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //Reloads maintenance log to reflect new changes
        reloaders.maintenanceReloader(scene);
    }

    //Loaded when a user wants to add a new maintenance issue
    @FXML
    public void switchToAddMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("createMaintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //setting Choice Box with values for the user to select from
        //otherwise is blank
        setMaintenanceTypeChoiceBox();
        setSkateSizeChoiceBox();
    }

    //Loaded when the user clicks the skate hire trends page
    @FXML
    public void switchToSkateHireTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("skateHireTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //reloads the trends
        //needs the parent scene and root to ensure is on the current scene
        reloaders.skateHireTrendsReloader(scene, root);
    }

    //Loaded when the user clicks the admission trends page
    public void switchToAdmissionTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admissionTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //reloads the trends
        //needs the parent scene and root to ensure is on the current scene
        reloaders.admissionTrendsReloader(scene, root);
    }

    //Method starts when user clicks the extra sales trends page
    public void switchToExtraSalesTrends(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("extraSalesTrends.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //reloads the trends
        //needs the parent scene and root to ensure is on the current scene
        reloaders.extraSalesReloader(scene, root);
    }

    //Method starts when user clicks the transaction history trends page
    public void switchToTransactionHistory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("transactionHistory.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        //reloads the trends
        //needs the parent scene to ensure is on the current scene
        reloaders.transactionHistoryReloader(scene);
    }

    //used to check if the session has started, contains a single call to the database
    public boolean sessionChecker() throws SQLException {
        return DBConnect.checkForSessionStart();
    }

    //Called when the start session button is clicked
    @FXML
    public void startSession() {
        //Date Creation Testing
        currentStatusText.setText("Session Started: \n" + dateTime.fullDateTime());
        String fullDate = dateTime.fullDateTime();
        String date = dateTime.justDate();
        String time = dateTime.justTime();

        System.out.println(fullDate);
        System.out.println(date);
        System.out.println(time);
        //Inserting the full date into the current session table, needed for checking if a session has started
        DBConnect.insertCurrentSession(fullDate);

        //Setting key buttons and key variables, variables are for other methods
        this.startSessionButton.setVisible(false);
        sessionStarted = true;
        System.out.println(sessionStarted);
        this.stopSessionButton.setVisible(true);

        //Getting the starting skates (inventory) and then setting this to the current skates
        //Ensure skate hire is refreshed and up to date when a new session starts
        getStartingSkates startingSkates = new getStartingSkates();
        startingSkates.initialiseSkates();
    }
    //Called when the stop button is pressed
    @FXML
    public void stopSession() throws SQLException {
        //Setting key variables
        sessionStarted = false;
        System.out.println(sessionStarted);
        currentStatusText.setText("Session Stopped");
        this.stopSessionButton.setVisible(false);
        this.startSessionButton.setVisible(true);
        //Move current session to previous session and current skates to previous
        //Ensure the database is tidied up ready for the next session
        DBConnect.moveSkatesToPrevious();
        DBConnect.moveToPreviousSessions();
    }

    //Called when the user clicks to create an announcement
    public void createAnnouncement() throws SQLException {
        //If the text box is empty show an alert box that the user cannot submit an empty field
        if (newAnnouncementText.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            //Get the users text, insert that text into the announcement box
            String annText = newAnnouncementText.getText();
            System.out.println("Text Found");
            DBConnect insertAnnounce = new DBConnect();
            boolean success = DBConnect.insertAnnouncement(annText);

            //If the submission was successful, clear the announcement text box
            //If submission was successful reload the list view (just in case outside the 1-second window)
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

    //Called when the user clicks to create a ticket
    public void createTicket() throws SQLException {
        //used for locks
        resourceName = "tickets";
        //Stops the user from submitting an empty text box as a ticket
        if (ticketsTextBox.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            //Before proceeding, locks so other resources cannot access tickets until complete
            locks.lock(resourceName, lockedBy);
            //Get the users text
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            DBConnect insertTicket = new DBConnect();

            //insert the text into the database
            boolean success = DBConnect.insertTicket(ticketText);

            //if successful clear the text box and reload the list view to show the new ticket
            if (success) {
                ticketsTextBox.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketsListView(lv);
                    System.out.println("Tickets Reload Create");
                }
            }
            //unlock resource when finished with it
            locks.unlock(resourceName,lockedBy);
        }
    }

    //called when the user selects a maintenance record from the list view
    public void selectMaintenance(){
        //Testing for the developer to test if connected to scenebuilder
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#MListView");
        //Gets the text and splits it where there is a new line escape character
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        String[] parts = selectedValue.split("\n");
        //Adds the core text and details to their boxes, so it's easy to read
        String maintenanceDetails = parts[0].substring("Details: ".length());
        maintenanceText.setText(maintenanceDetails);
    }

    //Runs when the user chooses to delete a ticket
    public void deleteTicket(){
        System.out.println("Delete");
        resourceName = "tickets";
        try {
            //locks the resource so other changes can not be made to the tickets while this transaction is running
            locks.lock(resourceName, lockedBy);
            //Get the tect of the ticket to be deleted
            String selectedValue = ticketsTextBox.getText();
            //Calls the database delete function
            boolean success = DBConnect.deleteTicket(selectedValue);

            //If succesful clears the text boxes to reduce confusion
            //Refreshes the list view so the ticket is no longer on the list
            if (success) {
                ticketsTextBox.clear();
                ticketsInfoBox.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketsListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }
            //Unlocks the resource now it's not in use.
            locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
        }
    }

    //Runs when the user chooses to delete and announcement
    public void deleteAnnouncement(){
        //Testing statement
        System.out.println("Delete");
        try {
            //gets the selected announcement
            String selectedValue = newAnnouncementText.getText();
            //deletes the announcement
            boolean success = DBConnect.deleteAnnouncement(selectedValue);
            //Clears the text box and reloads the list view so old announcement is removed
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

    //Called if the user clicks to delete a maintenance record
    public void deleteMaintenance(){
        System.out.println("Delete");
        resourceName = "maintenance";
        try {
            //locks the maintenance table so no other resrouce can access until transaction complete
            locks.lock(resourceName, lockedBy);
            String selectedValue = maintenanceText.getText();
            //Needs to fetch skate size in case the record to be deleted is for a pair of skates
            String skateSize = DBConnect.getSkateSizeFromMaintenance(selectedValue);
            boolean success = DBConnect.deleteMaintenance(selectedValue, skateSize);

            //Refreshing the list view and deleting the select text
            if (success) {
                maintenanceText.clear();
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadMaintenanceListView(lv);
                    System.out.println("Tickets Reload Delete");
                }
            }
            //Unlocking the resource once the action is completed
            locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
            log.log(Level.SEVERE,"Error Delete Maintenance Not Complete", e);
        }
    }

    //Loads when the user is on the skate hire page
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
                    //Colour Coding based on the skate amount (indicator of skates being hired)
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
        //Reloads automatically
        reloaders.SkateHireAnnouncementsReloader(scene);
        return data;
    }

    //If an announcement is selected, it is loaded into the text box to show more details
    public void selectAnnouncement(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#CAListView");
        String selectedValue = (String) lv.getSelectionModel().getSelectedItem();
        newAnnouncementText.setText(selectedValue);
    }

    //If selected to prevent errors from no class
    public void selectTransaction(){
        System.out.println("Clicked");
        ListView lv = (ListView) scene.lookup("#THListView");
    }

    //If selected loads into a text box, to make the details more readable the data is split where there is a
    //new line escape key. Where this key exists the data is seperated
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

    //Checks login details the user has entered
    //If there is a match then the user is taken to the next scene
    //If not the user is shown an error message
    public void loginChecker(ActionEvent event) throws SQLException, IOException {
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

    //Setting the defaults for a choice box
    //Separate method for code quality
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

    //Setting the defaults for a choice box
    //Separate method for code quality
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

    //Used for the maintenance forms
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

    //Used for the maintenance forms
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

    //Called when maintenance log is submitted
    public void createMaintenanceSubmit(){
        //Fetches selected value from choice boxes and text boxes
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        ChoiceBox<String> skateSize = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        TextArea maintenanceDetails = (TextArea) scene.lookup("#maintenanceDetails");

        //if null, show an error
        //No values on the form can be empty
        if (choiceBox == null || choiceBox.getValue() == null || choiceBox.getValue().isEmpty() || maintenanceDetails == null || maintenanceDetails.getText().isEmpty()){
            errors.maintenanceEmpty().show();
        }
        else {
            try {
                //If submitted then lock the maintenance resource so no other transactions can take place on that
                //table
                resourceName = "maintenance";
                locks.lock(resourceName,lockedBy);
                //Gets values and then passes them to the database class for insertion
                String typeIn = choiceBox.getValue();
                String details = maintenanceDetails.getText();
                String skateSizeIn = skateSize.getValue();
                DBConnect.insertMaintenance(typeIn, details, skateSizeIn);
                //Once completed, clear the boxes and unlock the resource
                maintenanceDetails.clear();
                locks.unlock(resourceName,lockedBy);
            } catch (SQLException e) {
                System.out.println("Couldn't Add Maintenance Record");
                throw new RuntimeException(e);
            }
        }
    }
    //Used if the user tries to select an object on a list view
    //Error prevention
    public void selectSHT(){System.out.println("Clicked");}
}