package com.example.willsrollerdiscobmgui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.sql.SQLException;
import java.util.List;

public class listViews {
    public static void loadAnnouncementsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadAnnouncement();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    public static void loadTicketsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTicket();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    public static void loadSkateHireListView(ListView lv) throws SQLException {
        ObservableList<Skate> data = sceneSelector.loadSkateHire(lv);
        lv.setItems(data);
    }

    public static void loadMaintenanceListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadMaintenance();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

}
