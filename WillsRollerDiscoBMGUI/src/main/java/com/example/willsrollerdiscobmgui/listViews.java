/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: listViews.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Method for the creation and initialisation of all the listViews within the application. Calls to some database
 *   methods to get values for the listViews.
 *   */

//PACKAGE
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.sql.SQLException;
import java.util.List;

/*Resources Used:
 * List Views:  */

//All classes work the same in this method, if the listView is empty then related values are fetched from the database
//Then those values are added to the list view in layout defined in their database method.
public class listViews {
    //Announcements
    public static void loadAnnouncementsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadAnnouncement();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    //Tickets
    public static void loadTicketsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTicket();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    //Skate Hire
    public static void loadSkateHireListView(ListView lv) throws SQLException {
        ObservableList<Skate> data = sceneSelector.loadSkateHire(lv);
        lv.setItems(data);
    }

    //Maintenance
    public static void loadMaintenanceListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadMaintenance();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    //Skate hire where Skates are 5 or Less
    public static void loadAnnouncementSHListView(ListView lv) throws SQLException {
        List<String> data = DBConnect.loadAnnouncementSH();
        ObservableList<String> items = FXCollections.observableArrayList(data);
        lv.setItems(items);
    }

    //All transactions (Admissions, Extra Sales and Replacements)
    public static void loadTransactionHistoryListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTransactionHistory();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        };
    }

    //Skates have reached 0 and more need purchasing
    public static void loadNeededSkates(ListView lv) throws SQLException {
        List<String> data = DBConnect.loadNeededSkates();
        ObservableList<String> items = FXCollections.observableArrayList(data);
        lv.setItems(items);
    }
}
