/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: DBConnect.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *    all methods that connect or interact with the database within the application, methods within this method are
 *    all called by outside methods, often the sceneSelector.
 *   */

//PACKAGES
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/*Resources Used:
 *Java Loggers:
 *Loggers instead of System prints
 *Connecting to a Database
 *Connecting to Docker Hosted Database
 *Observable Lists
 *Locks
 *Database Concurrency
 *Lock Hanging */
public class DBConnect {

    //Defining a Logger, used for error recording
    private static final Logger log = Logger.getLogger(String.valueOf(DBConnect.class));

    /******************************************************
    Local Testing Database Connections

    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";
    ********************************************************/

    /*Docker Database Connections
    * Using Local host currently due to firewall issues with University Connections*/
    String url = "jdbc:mysql://localhost:3307/wrddatabase";
    //String url = "jdbc:mysql://172.17.0.2:3307/wrddatabase";
    String username = "root";
    String password = "my-secret-pw";
    static Connection connection = null;
    static ResultSet rs;

    //List that fetches all the skate sizes and amount and sets them in a layout for use in a list view
    public static ObservableList<Skate> loadSkates() {
        //Defining List
        ObservableList<Skate> data = FXCollections.observableArrayList();

        //Trying to connect to the database
        //If connected, query is executed
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT skateSize, skateAmount FROM current_skates");

            // iterate through the result set and add Skate objects to the ObservableList
            while (rs.next()) {
                String size = rs.getString("skateSize");
                int amount = rs.getInt("skateAmount");
                data.add(new Skate(size, amount));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //returns results in layout needed for list view
        return data;
    }

    //Inserts the user input into the maintenance database
    //Receives type, details of issues, and the skate size
    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        //Trying to connect to the database
        //If connected, query is executed and values are inserted
        //If successful statement is printed to the console
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            //if a skate size is entered, skate sizes are fetched
            //Amount of inventory and current is reduced by 1 so data tables match
            //New value is passed and skate records are updated so pair cannot be hired out
            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error Within Insert Maintenance", e);
        }
    }

    //Used to set the skates to a new value, receives the value and size from parent call
    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE current_skates SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error Within Update Skate Size", e);
        }
    }

    //Updates to the skate inventory value, very similar to previous function
    //on a code review could be merged together by adding a table name value and having one function.
    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE skate_inventory SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error Within Update Skate Inventory", e);
        }

    }

    //Gets the session start time, uses the one saved to the current session rather than creating new
    //Prevents app incompatibility issues by ensuring the start time is consistent.
    public static String getSessionStartTime() {
        String query = "SELECT current_dateTime FROM current_session";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("current_dateTime");
            } else {
                return "Session not started";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //code for starting a database connection
    //various tries and catches are used to account for common issues and debugging a broken connection
    public void connect() {
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
            }
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Connection)");
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    //Gets the users input and inserts it to the announcement table
    //Uses a try and catch in case query cannot be executed
    public static boolean insertAnnouncement(String text) {
        //insert into query
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES('" + text + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error within Insert Announcement", e);
            success = false;
        }
        return success;
    }

    //Creates a list that defines the layouts for announcements
    //Used for announcements list view
    public static List<String> loadAnnouncement() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT announcement_details FROM announcements");
        List<String> announcementsList = new ArrayList<>();

        while (resultSet.next()) {
            String announcement = resultSet.getString("announcement_details");
            announcementsList.add(announcement);
        }
        return announcementsList;
    }

    //similar to previous function in name, however used to fetch skates where five or fewer pairs are remaining
    //Fetches data that is used for a list view
    public static List<String> loadAnnouncementSH() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM current_skates WHERE skateAmount <= 5");
        List<String> announcementsSHList = new ArrayList<>();

        while (resultSet.next()) {
            String skateSize = resultSet.getString("skateSize");
            String skateAmount = resultSet.getString("skateAmount");
            //Rows are combined, so they are displayed all on the same row
            announcementsSHList.add("Skate Size: " + skateSize + "\nSkate Amount: " + skateAmount);
        }
        return announcementsSHList;
    }

    //Used to fetch all tickets currently stored
    public static List<String> loadTicket() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        //while there are values, get them and store them as strings
        while (resultSet.next()) {
            String ticket = resultSet.getString("ticket_details");
            String ticketDate = resultSet.getString("ticket_date");
            String ticketTime = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");

            //Sets all these values in a layout for each row in a list view
            ticketsList.add("Ticket Details: " + ticket
                    + "\nDate and Time Posted: " + ticketDate + " " + ticketTime
                    + "\nPosted By: " + postedBy);
        }
        return ticketsList;
    }

    //used to fetch all the maintenance records currently stored
    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        //while there are values, get them and store them as strings
        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            //If there is no skate size variable, then set the display as N/A for the list view
            if ((skateSize == null) || (skateSize.equals("null"))) {
                skateSize = "N/A";
            }
            //Sets all these values in a layout for each row in a list view
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " + skateSize);
        }
        return maintenanceList;
    }

    //insert date log into current session table, used when the start session button is pressed
    public static void insertCurrentSession(String fullDate) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO current_session(current_dateTime) VALUES('" + fullDate + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error Within Current Session Insertion", e);
        }
    }

    //Fetches the data in the current session and adds it to the previous session
    //called when the user stops the session
    public static void moveToPreviousSessions() throws SQLException {
        //Defining local variables
        String sessionDateTime = null;
        int currentOwnSkaters = 0;
        int currentHireSkaters = 0;
        int currentSpectators = 0;
        int currentMembershipCards = 0;
        double currentAdmissionProfitOwnSkates = 0.00;
        double currentAdmissionProfitHireSkates = 0.00;
        double currentAdmissionProfitTotal = 0.00;
        int currentExtrasSoldAmount = 0;
        double currentExtrasSoldTotal = 0.00;

        //select from current session
        try {
            String query = "SELECT * FROM current_session";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            //If results are found, set these to their relative local value
            if (rs.next()) {
                sessionDateTime = rs.getString("current_dateTime");
                currentOwnSkaters = rs.getInt("Current_Own_skaters");
                currentHireSkaters = rs.getInt("Current_Hire_skaters");
                currentSpectators = rs.getInt("Current_Spectators");
                currentMembershipCards = rs.getInt("Current_Membership_cards_used");
                currentAdmissionProfitOwnSkates = rs.getDouble("Current_Admission_profit_hire_skaters");
                currentAdmissionProfitHireSkates = rs.getDouble("Current_Admission_profit_hire_skaters");
                currentAdmissionProfitTotal = rs.getDouble("Current_Admission_profit_total");
                currentExtrasSoldAmount = rs.getInt("Current_Extras_sold_amount");
                currentExtrasSoldTotal = rs.getDouble("Current_Extras_sold_total");
            }
            //Add these local variables into the previous table
            try {
                PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO previous_sessions(session_dateTime, own_skaters, hire_skaters, spectators," +
                                "memberships, admission_profit_own_skaters, admission_profit_hire_skaters, admission_profit_total, " +
                                "extras_sold_amount, extras_sold_total) VALUES (?,?,?,?,?,?,?,?,?,? )");
                pstmt.setString(1, sessionDateTime);
                pstmt.setInt(2, currentOwnSkaters);
                pstmt.setInt(3, currentHireSkaters);
                pstmt.setInt(4, currentSpectators);
                pstmt.setInt(5, currentMembershipCards);
                pstmt.setDouble(6, currentAdmissionProfitOwnSkates);
                pstmt.setDouble(7, currentAdmissionProfitHireSkates);
                pstmt.setDouble(8, currentAdmissionProfitTotal);
                pstmt.setDouble(9, currentExtrasSoldAmount);
                pstmt.setDouble(10, currentExtrasSoldTotal);
                pstmt.executeUpdate();
                //testing statement for debugging
                System.out.println("Moved to Previous Session");
            } catch (SQLException e) {
                log.log(Level.SEVERE,"Table could not be moved to previous session", e);
                return;
            }
            //Delete the current session that is stored
            //Resets the table for when the user wants to create another session

            //Called after the current session has been moved to previous so no data is lost
            try {
                Statement stmtDrop = connection.createStatement();
                String sql = "TRUNCATE TABLE current_session";
                stmtDrop.executeUpdate(sql);
                System.out.println("Current Session Dropped");
            } catch (SQLException e) {
                log.log(Level.SEVERE,"Current Session Table Could Not Be Dropped", e);
            }
        } finally {

        }
    }

    //Called when a session is stopped
    //Moves all the current skates being used for analytics into previous
    public static void moveSkatesToPrevious() throws SQLException {
        //Fetches the related session time, needed for adding a new record
        String sessionDateTime = getSessionStartTime();

        // Fetch all records from current_skates_analytics
        String selectQuery = "SELECT skateSize, skateAmount FROM current_skates_analytics";
        Statement selectStmt = connection.createStatement();
        ResultSet selectRs = selectStmt.executeQuery(selectQuery);

        // Iterate over the records and insert them into previous_skate_log
        while (selectRs.next()) {
            String skateSize = selectRs.getString("skateSize");
            int skateAmount = selectRs.getInt("skateAmount");

            //matching the sizes to their database name, so it's more readable for the user when the data is used
            //by other methods
            String column = switch (skateSize) {
                case "1" -> "size1";
                case "2" -> "size2";
                case "3" -> "size3";
                case "4" -> "size4";
                case "5" -> "size5";
                case "6" -> "size6";
                case "7" -> "size7";
                case "8" -> "size8";
                case "9" -> "size9";
                case "10" -> "size10";
                case "11" -> "size11";
                case "12" -> "size12";
                case "C11" -> "sizeC11";
                case "C12" -> "sizeC12";
                case "C13" -> "sizeC13";
                default -> throw new IllegalArgumentException("Invalid skate size: " + skateSize);
            };

            // Check if a row exists for the sessionDateTime in previous_skate_log
            String checkQuery = "SELECT sessionDateTime FROM previous_skate_log WHERE sessionDateTime = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, sessionDateTime);
            ResultSet checkRs = checkStmt.executeQuery();

            if (checkRs.next()) {
                // Update the value in previous_skate_log
                String updateQuery = "UPDATE previous_skate_log SET `" + column + "` = ? WHERE sessionDateTime = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setInt(1, skateAmount);
                updateStmt.setString(2, sessionDateTime);
                updateStmt.executeUpdate();
            } else {
                // Insert a new row in previous_skate_log
                String insertQuery = "INSERT INTO previous_skate_log (sessionDateTime, `" + column + "`) VALUES (?, ?)";
                PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                insertStmt.setString(1, sessionDateTime);
                insertStmt.setInt(2, skateAmount);
                insertStmt.executeUpdate();
            }
        }

        //Reset the skate amount in current_skates_analytics
        //used to prepare this table for the next session
        String resetQuery = "UPDATE current_skates_analytics SET skateAmount = 0";
        Statement resetStmt = connection.createStatement();
        resetStmt.executeUpdate(resetQuery);

        System.out.println("Transferred Skate Analytics to Previous Log");
    }

    //Used to fetch the amount of skates for a size inputted by the parent function
    //retrieves only the amount for that specific skate size
    public static int fetchSkateSizeAmount(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, skateSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Used to fetch the amount of skates for a certain size from the current skates table
    public static int fetchSkateSizeCurrent(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM current_skates WHERE skateSize = '" + skateSize + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Insert the fetched values into the current skates' table
    //receives a size and amount from parent method
    public static void insertIntoCurrentSkates(String skateSize, int skateAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = " + skateAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Skate Size Into Current Database");

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Current Skate Size not inserted to Current Database", e);
        }
    }

    //used to insert the users input as a new ticket
    public static boolean insertTicket(String text) {
        boolean success;
        //Creates a date and time object for the new ticket
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();

        //Sets the posted by
        String postedBy = "Business Management Portal";

        //tries to insert the above as a new tickets
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);
            pstmt.executeUpdate();

            //developer print statement to confirm ticket creation
            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Ticket Could Not be Inserted", e);
            success = false;
        }
        return success;
    }

    //Used to check if there is a current session in place
    public static boolean checkForSessionStart() throws SQLException {
        String query = "SELECT * FROM current_session";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }

    //removes the ticket where the details match the value recieved from the parent class
    public static boolean deleteTicket(String value) {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM tickets WHERE ticket_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Ticket could not be deleted", e);
            success = false;
        }
        return success;
    }

    //removes the announcement where the details match the value recieved from the parent class
    public static boolean deleteAnnouncement(String value) {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM announcements WHERE announcement_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Announcement Was Not Deleted", e);
            success = false;
        }
        return success;
    }

    //removes the maintenance record where the details match the value recieved from the parent class
    public static boolean deleteMaintenance(String value, String skateSizeIn){
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM maintenance WHERE maintenance_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount + 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount + 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Maintenance Record Could Not Be Deleted", e);
            success = false;
        }
        return success;
    }

    //Checks the users input against the login table, if there is a match then the user is logged in
    //Needs fixing, due to the database structure the username and password is not case-sensitive
    public static boolean checkLogin(String username, String password) throws SQLException {
        boolean loggedIn;
        String query = "SELECT * FROM login WHERE username=? AND password=?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, username);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            loggedIn = true;
            System.out.print("Logged In");
        } else {
            loggedIn = false;
            System.out.print("Not Logged In");
        }
        return loggedIn;
    }

    //Used to fetch the size of skates that are in the maintenance table
    public static String getSkateSizeFromMaintenance(String value) throws SQLException {
        String skateSize = null;
        PreparedStatement stmt = connection.prepareStatement("SELECT skateSize FROM maintenance WHERE maintenance_details = ?");
        stmt.setString(1, value);
        String queryString = stmt.toString();
        System.out.println(queryString);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            skateSize = rs.getString("skateSize");
        }
        rs.close();
        stmt.close();
        return skateSize;
    }

    //Loads all the transactions stored in the database, used to create listview rows
    public static List<String> loadTransactionHistory() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM transaction_history ORDER BY transaction_time DESC");
        List<String> transactionList = new ArrayList<>();

        //Setting new strings as the values fetched from the database
        while (resultSet.next()) {
            String sessionDateTime = resultSet.getString("session_dateTime");
            String type = resultSet.getString("transaction_type");
            String time = resultSet.getString("transaction_time");
            Double value = resultSet.getDouble("transaction_value");

            //setting the double value to dwo decimal places, removes issues such as £2.50 being 2.5
            String formattedValue = String.format("%.2f", value);

            //Adds values in the layout needed for the list view
            transactionList.add("Type: " + type + "\nValue: £" + formattedValue + "\nPurchase Time: " + time + " Session: " +
                    sessionDateTime);
        }
        return transactionList;
    }

    //Counts all the transactions of the same type. Only counts those from the current session, used for analytics
    public static int findExtrasCurrent(String dateTime, String type) throws SQLException {
        int count = 0;
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(transaction_type) AS count " +
                "FROM transaction_history WHERE session_dateTime = ? AND transaction_type = ?");

        stmt.setString(1, dateTime);
        stmt.setString(2, type);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            count = resultSet.getInt("count");
        }

        return count;
    }

    //Counts all the extras ever sold and returns the value for analytics
    public static int findExtrasAllTime(String type) throws SQLException {
        int count = 0;
        PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(transaction_type) AS count " +
                "FROM transaction_history WHERE transaction_type = ?");

        stmt.setString(1, type);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            count = resultSet.getInt("count");
        }

        return count;
    }

    //Finds and returns the membership cards used in the current session
    public static int findMembers() throws SQLException {
        int count = 0;
        PreparedStatement stmt = connection.prepareStatement("SELECT Current_Membership_cards_used  " +
                "FROM current_session");

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            count = resultSet.getInt("Current_Membership_cards_used");
        }

        return count;
    }

    //Used to calculate the averages of objects within the previous session table
    public static int calculateAverages(String type) throws SQLException {
        double result = 0.0;

        PreparedStatement stmt = connection.prepareStatement("SELECT AVG(" + type + ") AS average FROM previous_sessions");
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            result = resultSet.getDouble("average");
        }
        return (int) Math.round(result);
    }

    //Finds all the skates hired of all time, used for charts to show the most popular
    public static int findSkateSizeAllTime(String sizeType) throws SQLException {
        int totalCount = 0;

        String sql = "SELECT " + sizeType + " FROM previous_skate_log";

        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            int count = resultSet.getInt(sizeType);
            totalCount += count;
        }

        return totalCount;
    }

    //Finds the most popular skate size, finds all the sizes.
    //Compares the sizes and then returns the most popular
    public static String MostPopularSize() throws SQLException {
        String mostPopularSize = "";
        int max = Integer.MIN_VALUE;

        String[] columns = {"size1", "size2", "size3", "size4", "size5", "size6", "size7", "size8", "size9",
                "size10", "size11", "size12", "sizeC11", "sizeC12", "sizeC13"};

        for (String sizeColumn : columns) {
            int count = DBConnect.findSkateSizeAllTime(sizeColumn);
            if (count > max) {
                max = count;
                mostPopularSize = sizeColumn;
            }
        }
        return mostPopularSize;
    }

    //Similar to above function, but looks for the smallest count to return the least popular
    public static String LeastPopularSize() throws SQLException {
        String leastPopularSize = "";
        int min = Integer.MAX_VALUE;

        String[] columns = {"size1", "size2", "size3", "size4", "size5", "size6", "size7", "size8", "size9",
                "size10", "size11", "size12", "sizeC11", "sizeC12", "sizeC13"};

        for (String sizeColumn : columns) {
            int count = DBConnect.findSkateSizeAllTime(sizeColumn);
            if (count < min) {
                min = count;
                leastPopularSize = sizeColumn;
            }
        }

        return leastPopularSize;
    }

    //Counter for skates that are in the maintenance logs
    //Searches maintenance table for anytime Skate Hire is saved as the maintenance type
    public static String countSkatesInMaintenance() throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS count FROM maintenance WHERE maintenance_type = 'Skate Hire' AND resolved = 'N'";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        return String.valueOf(count);
    }

    //Uses an array list to load all the skates that have reached 0 and the date that occurred
    public static List<String> loadNeededSkates() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT skateSize, dateNeeded FROM needed_skates");
        List<String> neededList = new ArrayList<>();

        while (resultSet.next()) {
            String skateSize = resultSet.getString("skateSize");
            String dateNeeded = resultSet.getString("dateNeeded");
            neededList.add("Size: " + skateSize + " Date Needed: " + dateNeeded);
        }
        return neededList;
    }

    //Returns the most popular customer type
    //Uses an array to search for customer type matches
    //If more than 0 matches the result is counted and the largest type is returned
    public static String MostPopularCustomerType() throws SQLException {
        String mostPopularType = null;
        int maxCount = 0;

        String[] customerTypes = {"own_skaters", "hire_skaters", "spectators"};
        for (String type : customerTypes) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT COUNT(*) AS count FROM previous_sessions WHERE " + type + " > 0"
            );
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                if (count > maxCount) {
                    maxCount = count;
                    mostPopularType = type;
                }
            }
        }

        if (mostPopularType != null) {
            return switch (mostPopularType) {
                case "own_skaters" -> "Own Skaters";
                case "hire_skaters" -> "Hire Skaters";
                case "spectators" -> "Spectators";
                default -> "Unknown";
            };
        } else {
            return "Not Enough Data";
        }
    }

    //Returns the average amount of customers per session
    // Fetches all customer types, counts them and divides by how many sessions
    public static int totalCustomerAverage() throws SQLException {
        int totalCustomers = 0;
        int sessionCount = 0;

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT own_skaters, hire_skaters, spectators FROM previous_sessions"
        );
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            int ownSkaters = resultSet.getInt("own_skaters");
            int hireSkaters = resultSet.getInt("hire_skaters");
            int spectators = resultSet.getInt("spectators");

            totalCustomers += ownSkaters + hireSkaters + spectators;
            sessionCount++;
        }

        if (sessionCount > 0) {
            return totalCustomers / sessionCount;
        } else {
            //No Results Catch
            return 0;
        }
    }

    //Similar role as above function, however no spectators are included
    //spectators are not paid customers so may not be as use to client
    public static int totalCustomerAverageNoSpec() throws SQLException {
        int totalCustomers = 0;
        int sessionCount = 0;

        PreparedStatement stmt = connection.prepareStatement(
                "SELECT own_skaters, hire_skaters FROM previous_sessions"
        );
        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next()) {
            int ownSkaters = resultSet.getInt("own_skaters");
            int hireSkaters = resultSet.getInt("hire_skaters");

            totalCustomers += ownSkaters + hireSkaters;
            sessionCount++;
        }

        if (sessionCount > 0) {
            return totalCustomers / sessionCount;
        } else {
            // Handle the case when there are no records in the table
            return 0;
        }
    }
}