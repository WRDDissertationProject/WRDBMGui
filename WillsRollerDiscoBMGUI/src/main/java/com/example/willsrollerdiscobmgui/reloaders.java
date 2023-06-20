/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: reloaders.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Methods that automatically reloads other methods, used for live updates within the application.
 *   */
//PACKAGE
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class reloaders {
    public static void SkateHireReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            listViews.loadSkateHireListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void SkateHireAnnouncementsReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHAnnounceListView");
                        if (lv != null) {
                            listViews.loadAnnouncementSHListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void ticketsReloader(Scene scene) {
        Timer reloadTicket = new Timer();
        reloadTicket.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                        if (lv != null) {
                            listViews. loadTicketsListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void announcementReloader(Scene scene) {
        Timer reloadAnnouncement = new Timer();
        reloadAnnouncement.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CAListView");
                        if (lv != null) {
                            listViews. loadAnnouncementsListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 2000); // reload every second
    }

    public static void maintenanceReloader(Scene scene) {
        Timer maintenanceTimer = new Timer();
        maintenanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                        if (lv != null) {
                            listViews.loadMaintenanceListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void transactionHistoryReloader(Scene scene) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#THListView");
                        if (lv != null) {
                            listViews.loadTransactionHistoryListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void extraSalesReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        analytics.mostPopularExtrasAllTime(scene);
                        analytics.mostPopularExtrasCurrent(scene);
                        analytics.setExtraTrendsLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void admissionTrendsReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        analytics.admissionsAllTimeChart(scene);
                        analytics.admissionsCurrentChart(scene);
                        analytics.admissionsCurrentLabels(root);
                        analytics.admissionsAverageLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void skateHireTrendsReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHTListView");
                        if (lv != null) {
                            listViews.loadNeededSkates(lv);
                        }
                        analytics.mostPopularSkateSizesAllTime(scene);
                        analytics.setSkateTrendsLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    public static void homeReloader(Scene scene, Parent root) {
        Timer reloadHome = new Timer();
        reloadHome.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHHListView");
                        if (lv != null) {
                            listViews.loadAnnouncementSHListView(lv);
                        }
                        analytics.setHomeTrendsLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }
}
