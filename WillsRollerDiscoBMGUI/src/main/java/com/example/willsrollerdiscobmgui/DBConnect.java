package com.example.willsrollerdiscobmgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnect {
    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";

    static Connection connection = null;

    static ResultSet rs;

    public static ObservableList<Skate> loadSkates() {
        ObservableList<Skate> data = FXCollections.observableArrayList();

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

        return data;
    }

    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
    }

    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE current_skates SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current skate amount not updated");
        }
    }

    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            PreparedStatement stmt = connection.prepareStatement("UPDATE skate_inventory SET skateAmount = ? WHERE skateSize = ?");
            stmt.setInt(1, newAmount);
            stmt.setString(2, skateSize);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Skate inventory not updated");
        }

    }

    public static void resetLocks() throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE locks SET lockedBy = null, lockTime = null");
        stmt.executeUpdate();
        System.out.println("Locks Reset");
    }

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
                ;
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    public static boolean insertAnnouncement(String text) {
        //insert into query
        boolean success = false;
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO announcements(announcement_details) VALUES('" + text + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
            success = false;
        }
        return success;
    }

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

    public static List<String> loadAnnouncementSH() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM current_skates WHERE skateAmount <= 5");
        List<String> announcementsSHList = new ArrayList<>();

        while (resultSet.next()) {
            String skateSize = resultSet.getString("skateSize");
            String skateAmount = resultSet.getString("skateAmount");
            announcementsSHList.add("Skate Size: " + skateSize + "\nSkate Amount: " + skateAmount);
        }
        return announcementsSHList;
    }

    public static List<String> loadTicket() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket = resultSet.getString("ticket_details");
            String ticketDate = resultSet.getString("ticket_date");
            String ticketTime = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");

            ticketsList.add("Ticket Details: " + ticket
                    + "\nDate and Time Posted: " + ticketDate + " " + ticketTime
                    + "\nPosted By: " + postedBy);
        }
        return ticketsList;
    }

    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            if ((skateSize == null) || (skateSize.equals("null"))) {
                skateSize = "N/A";
            }
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " + skateSize);
        }
        return maintenanceList;
    }


    public static void insertCurrentSession(String fullDate, String date, String time) {

        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO current_session(current_dateTime) VALUES('" + fullDate + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current Session not inserted");
        }
    }


    public static void moveToPreviousSessions() throws SQLException {
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


            //move into the previous table
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
                System.out.println("Moved to Previous Session");
            } catch (SQLException e) {
                System.out.print(e);
                System.out.println("Table could not be moved to Previous Session");
                return; //exit method if there is an error
            }

            //drop current session
            try {
                Statement stmtDrop = connection.createStatement();
                String sql = "TRUNCATE TABLE current_session";
                stmtDrop.executeUpdate(sql);
                System.out.println("Current Session Dropped");
            } catch (SQLException e) {
                System.out.print(e);
                System.out.println("Current Session Table Cannot Be Dropped");
            }
        } finally {

        }
    }

    public static void moveSkatesToPrevious() throws SQLException {
        String sessionDateTime = getSessionStartTime();

// Fetch all records from current_skates_analytics
        String selectQuery = "SELECT skateSize, skateAmount FROM current_skates_analytics";
        Statement selectStmt = connection.createStatement();
        ResultSet selectRs = selectStmt.executeQuery(selectQuery);

// Iterate over the records and insert them into previous_skate_log
        while (selectRs.next()) {
            String skateSize = selectRs.getString("skateSize");
            int skateAmount = selectRs.getInt("skateAmount");

            String column;
            // Determine the appropriate column to update based on skateSize
            switch (skateSize) {
                case "1":
                    column = "size1";
                    break;
                case "2":
                    column = "size2";
                    break;
                case "3":
                    column = "size3";
                    break;
                case "4":
                    column = "size4";
                    break;
                case "5":
                    column = "size5";
                    break;
                case "6":
                    column = "size6";
                    break;
                case "7":
                    column = "size7";
                    break;
                case "8":
                    column = "size8";
                    break;
                case "9":
                    column = "size9";
                    break;
                case "10":
                    column = "size10";
                    break;
                case "11":
                    column = "size11";
                    break;
                case "12":
                    column = "size12";
                    break;
                case "C11":
                    column = "sizeC11";
                    break;
                case "C12":
                    column = "sizeC12";
                    break;
                case "C13":
                    column = "sizeC13";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid skate size: " + skateSize);
            }

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

// Reset the skate amount in current_skates_analytics
        String resetQuery = "UPDATE current_skates_analytics SET skateAmount = 0";
        Statement resetStmt = connection.createStatement();
        resetStmt.executeUpdate(resetQuery);

        System.out.println("Transferred Skate Analytics to Previous Log");
    }


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

    public static void insertIntoCurrentSkates(String skateSize, int skateAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = " + skateAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Skate Size Into Current Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Current Skate Size not inserted to Current Database");
        }
    }

    public static boolean insertTicket(String text) {
        //insert into query
        boolean success = false;
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();
        String postedBy = "Business Management Portal";
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);

            pstmt.executeUpdate();

            System.out.println("Inserted Into Database");
            success = true;

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
            success = false;
        }
        return success;
    }

    public static boolean checkForSessionStart() throws SQLException {
        String query = "SELECT * FROM current_session";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }

    public static boolean deleteTicket(String value) {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM tickets WHERE ticket_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Ticket not Deleted");
            success = false;
        }
        return success;
    }

    public static boolean deleteAnnouncement(String value) throws SQLException {
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM announcements WHERE announcement_details = '" + value + "'";
            stmt.executeUpdate(query);
            success = true;
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not Deleted");
            success = false;
        }
        return success;
    }

    public static boolean deleteMaintenance(String value, String skateSizeIn) throws SQLException {
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
            System.out.println(e);
            System.out.println("Maintenance not Deleted");
            success = false;
        }
        return success;
    }

    public static boolean checkLogin(String username, String password) throws SQLException {
        boolean loggedIn = false;
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

    public static List<String> loadTransactionHistory() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM transaction_history ORDER BY transaction_time DESC");
        List<String> transactionList = new ArrayList<>();

        while (resultSet.next()) {
            String sessionDateTime = resultSet.getString("session_dateTime");
            String type = resultSet.getString("transaction_type");
            String time = resultSet.getString("transaction_time");
            Double value = resultSet.getDouble("transaction_value");

            String formattedValue = String.format("%.2f", value);

            transactionList.add("Type: " + type + "\nValue: £" + formattedValue + "\nPurchase Time: " + time + " Session: " +
                    sessionDateTime);
        }
        return transactionList;
    }

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

    public static int calculateAverages(String type) throws SQLException {
        double result = 0.0;

        PreparedStatement stmt = connection.prepareStatement("SELECT AVG(" + type + ") AS average FROM previous_sessions");
        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            result = resultSet.getDouble("average");
        }

        int rounded = (int) Math.round(result);
        return rounded;
    }

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

    public static String countSkatesInMaintenance() throws SQLException {
        int count = 0;
        String sql = "SELECT COUNT(*) AS count FROM maintenance WHERE maintenance_type = 'Skate Hire' AND resolved = 'N'";

        PreparedStatement stmt = connection.prepareStatement(sql);

        ResultSet resultSet = stmt.executeQuery();

        if (resultSet.next()) {
            count = resultSet.getInt("count");
        }

        String amountStr = String.valueOf(count);
        return amountStr;
    }

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
            switch (mostPopularType) {
                case "own_skaters":
                    return "Own Skaters";
                case "hire_skaters":
                    return "Hire Skaters";
                case "spectators":
                    return "Spectators";
                default:
                    return "Unknown";
            }
        } else {
            return "Not Enough Data";
        }
    }

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
            // Handle the case when there are no records in the table
            return 0;
        }
    }

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