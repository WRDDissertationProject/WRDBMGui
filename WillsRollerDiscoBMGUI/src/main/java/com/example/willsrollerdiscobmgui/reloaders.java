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

/*Resources Used:
 * Reloaders:
 * Run Laters:  */

//Used to Reload all the list views, most methods follow the same layout
//Run laters are used due to JavaFX issues when trying to run all at the same time
public class reloaders {
    //Skate Hire
    public static void SkateHireReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadSkateHireListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Skates 5 or Less
    public static void SkateHireAnnouncementsReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHAnnounceListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadAnnouncementSHListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Tickets
    public static void ticketsReloader(Scene scene) {
        Timer reloadTicket = new Timer();
        reloadTicket.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews. loadTicketsListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Announcements
    public static void announcementReloader(Scene scene) {
        Timer reloadAnnouncement = new Timer();
        reloadAnnouncement.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CAListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews. loadAnnouncementsListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 2000); // reload every second
    }

    //Maintenance
    public static void maintenanceReloader(Scene scene) {
        Timer maintenanceTimer = new Timer();
        maintenanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadMaintenanceListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Transactions Reloader
    public static void transactionHistoryReloader(Scene scene) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#THListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadTransactionHistoryListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Extra Sales
    public static void extraSalesReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Reloading all the extra sales analytics
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

    //Admission trends
    public static void admissionTrendsReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Reloading all the admissions analytics
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

    //Skate Hire Analytics
    public static void skateHireTrendsReloader(Scene scene, Parent root) {
        Timer reloadTransactions = new Timer();
        reloadTransactions.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHTListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadNeededSkates(lv);
                        }
                        //Reload the analytics values for the scene
                        analytics.mostPopularSkateSizesAllTime(scene);
                        analytics.setSkateTrendsLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }

    //Reloader for the first opening scene
    public static void homeReloader(Scene scene, Parent root) {
        Timer reloadHome = new Timer();
        reloadHome.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHHListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadAnnouncementSHListView(lv);
                        }
                        //Reloading the analytics trends
                        analytics.setHomeTrendsLabels(root);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second
    }
}
