/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoBM
 *  FILE TITLE: analytics.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   All java methods that are used to calculate business analytics, isolated for code clarity, used to find key
 *   analytics such as averages, most and least popular and for the creation of Pie Charts*/

//PACKAGES
package com.example.willsrollerdiscobmgui;

//IMPORTS
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.sql.SQLException;

public class analytics {

    public static void mostPopularExtrasAllTime(Scene scene) throws SQLException {
        int glowStickCount = DBConnect.findExtrasAllTime("Glow Stick");
        int foamGlowStickCount = DBConnect.findExtrasAllTime("Foam Glow Stick");
        int skateLacesCount = DBConnect.findExtrasAllTime("Skate Laces");
        int seasonalCount = DBConnect.findExtrasAllTime("Seasonal Sale");
        int freePromotionCount = DBConnect.findExtrasAllTime("Free Promotion");
        int replacementCount = DBConnect.findExtrasAllTime("Replacement");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Glow Sticks", glowStickCount),
                new PieChart.Data("Foam Glow Sticks", foamGlowStickCount),
                new PieChart.Data("Skate Laces", skateLacesCount),
                new PieChart.Data("Seasonal", seasonalCount),
                new PieChart.Data("Free Promotion", freePromotionCount),
                new PieChart.Data("Replacements", replacementCount)
        );

        PieChart chart = new PieChart(chartData);
        chart.setTitle("All Time Extra Sales");
        chart.setLegendVisible(false);
        chart.setLabelsVisible(true);

        StackPane currentExtras = (StackPane) scene.lookup("#allTimeExtraStackPane");
        currentExtras.getChildren().add(chart);
    }
    public static void mostPopularExtrasCurrent(Scene scene) throws SQLException {
        String sessionDateTime = DBConnect.getSessionStartTime();
        int glowStickCount = DBConnect.findExtrasCurrent(sessionDateTime, "Glow Stick");
        int foamGlowStickCount = DBConnect.findExtrasCurrent(sessionDateTime, "Foam Glow Stick");
        int skateLacesCount = DBConnect.findExtrasCurrent(sessionDateTime,"Skate Laces");
        int seasonalCount = DBConnect.findExtrasCurrent(sessionDateTime,"Seasonal Sale");
        int freePromotionCount = DBConnect.findExtrasCurrent(sessionDateTime, "Free Promotion");
        int replacementCount = DBConnect.findExtrasCurrent(sessionDateTime,"Replacement");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Glow Sticks", glowStickCount),
                new PieChart.Data("Foam Glow Sticks", foamGlowStickCount),
                new PieChart.Data("Skate Laces", skateLacesCount),
                new PieChart.Data("Seasonal", seasonalCount),
                new PieChart.Data("Free Promotion", freePromotionCount),
                new PieChart.Data("Replacements", replacementCount)
        );

        PieChart chart = new PieChart(chartData);
        chart.setTitle("Current Extra Sales");
        chart.setLegendVisible(false);

        StackPane currentExtras = (StackPane) scene.lookup("#currentExtraStackPane");
        currentExtras.getChildren().add(chart);
    }

    public static void setExtraTrendsLabels(Parent root) throws SQLException {
        String sessionDateTime = DBConnect.getSessionStartTime();
        int glowStickCount = DBConnect.findExtrasCurrent(sessionDateTime, "Glow Stick");
        int foamGlowStickCount = DBConnect.findExtrasCurrent(sessionDateTime, "Foam Glow Stick");
        int skateLacesCount = DBConnect.findExtrasCurrent(sessionDateTime,"Skate Laces");
        int seasonalCount = DBConnect.findExtrasCurrent(sessionDateTime,"Seasonal Sale");
        int freePromotionCount = DBConnect.findExtrasCurrent(sessionDateTime, "Free Promotion");
        int replacementCount = DBConnect.findExtrasCurrent(sessionDateTime,"Replacement");

        Label glowStick = (Label) root.lookup("#glowSticksCurrentNumber");
        Label foamGlowStick = (Label) root.lookup("#fGlowSticksCurrentNumber");
        Label skateLaces = (Label) root.lookup("#skateLacesCurrentNumber");
        Label seasonal = (Label) root.lookup("#seasonalCurrentNumber");
        Label freePromotion =(Label) root.lookup("#promotionalCurrentNumber");
        Label replacements = (Label) root.lookup("#replacementCurrentNumber");

        glowStick.setText(String.valueOf(glowStickCount));
        foamGlowStick.setText(String.valueOf(foamGlowStickCount));
        skateLaces.setText(String.valueOf(skateLacesCount));
        seasonal.setText(String.valueOf(seasonalCount));
        freePromotion.setText(String.valueOf(freePromotionCount));
        replacements.setText(String.valueOf(replacementCount));
    }

    public static void admissionsAllTimeChart(Scene scene) throws SQLException {
        int skateHireCount = DBConnect.findExtrasAllTime("Skate Hire");
        int skatersCount = DBConnect.findExtrasAllTime("Skater");
        int spectatorsCount = DBConnect.findExtrasAllTime("Spectator");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Skate Hire", skateHireCount),
                new PieChart.Data("Skaters", skatersCount),
                new PieChart.Data("Spectators", spectatorsCount)
        );

        PieChart chart = new PieChart(chartData);
        chart.setTitle("All Time Admissions");
        chart.setLegendVisible(false);

        StackPane admissionsALlTime = (StackPane) scene.lookup("#allTimeAdmissionsStackPane");
        admissionsALlTime.getChildren().add(chart);
    }

    public static void admissionsCurrentChart(Scene scene) throws SQLException {
        String sessionDateTime = DBConnect.getSessionStartTime();
        int skateHireCount = DBConnect.findExtrasCurrent(sessionDateTime, "Skate Hire");
        int skatersCount = DBConnect.findExtrasCurrent(sessionDateTime, "Skater");
        int spectatorsCount = DBConnect.findExtrasCurrent(sessionDateTime,"Spectator");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Skate Hire", skateHireCount),
                new PieChart.Data("Skaters", skatersCount),
                new PieChart.Data("Spectators", spectatorsCount)
        );

        PieChart chart = new PieChart(chartData);
        chart.setTitle("Current Admissions");
        chart.setLegendVisible(false);

        StackPane currentExtras = (StackPane) scene.lookup("#currentAdmissionsStackPane");
        currentExtras.getChildren().add(chart);
    }

    public static void admissionsCurrentLabels(Parent root) throws SQLException {
        String sessionDateTime = DBConnect.getSessionStartTime();
        int skateHireCount = DBConnect.findExtrasCurrent(sessionDateTime, "Skate Hire");
        int skatersCount = DBConnect.findExtrasCurrent(sessionDateTime, "Skater");
        int spectatorsCount = DBConnect.findExtrasCurrent(sessionDateTime,"Spectator");
        int membersCount = DBConnect.findMembers();

        Label skateHire = (Label) root.lookup("#skateHireCurrentNumber");
        Label skaters = (Label) root.lookup("#ownSkatersCurrentNumber");
        Label spectators = (Label) root.lookup("#spectatorsCurrentNumber");
        Label members = (Label) root.lookup("#membersCurrentNumber");

        skateHire.setText(String.valueOf(skateHireCount));
        skaters.setText(String.valueOf(skatersCount));
        spectators.setText(String.valueOf(spectatorsCount));
        members.setText(String.valueOf(membersCount));
    }

    public static void admissionsAverageLabels(Parent root) throws SQLException {
        int skateHireAvg = DBConnect.calculateAverages("hire_skaters");
        int skatersAvg = DBConnect.calculateAverages("own_skaters");
        int spectatorsAvg = DBConnect.calculateAverages("spectators");
        int membersAvg = DBConnect.calculateAverages("memberships");

        Label skateHire = (Label) root.lookup("#skateHireAverage");
        Label skaters = (Label) root.lookup("#ownSkatersAverage");
        Label spectators = (Label) root.lookup("#spectatorsAverage");
        Label members = (Label) root.lookup("#membersAverage");

        skateHire.setText(String.valueOf(skateHireAvg));
        skaters.setText(String.valueOf(skatersAvg));
        spectators.setText(String.valueOf(spectatorsAvg));
        members.setText(String.valueOf(membersAvg));
    }

    public static void mostPopularSkateSizesAllTime(Scene scene) throws SQLException {
        int size1 = DBConnect.findSkateSizeAllTime("size1");
        int size2 = DBConnect.findSkateSizeAllTime("size2");
        int size3 = DBConnect.findSkateSizeAllTime("size3");
        int size4 = DBConnect.findSkateSizeAllTime("size4");
        int size5 = DBConnect.findSkateSizeAllTime("size5");
        int size6 = DBConnect.findSkateSizeAllTime("size6");
        int size7 = DBConnect.findSkateSizeAllTime("size7");
        int size8 = DBConnect.findSkateSizeAllTime("size8");
        int size9 = DBConnect.findSkateSizeAllTime("size9");
        int size10 = DBConnect.findSkateSizeAllTime("size10");
        int size11 = DBConnect.findSkateSizeAllTime("size11");
        int size12 = DBConnect.findSkateSizeAllTime("size12");
        int sizeC11 = DBConnect.findSkateSizeAllTime("sizeC11");
        int sizeC12 = DBConnect.findSkateSizeAllTime("sizeC12");
        int sizeC13 = DBConnect.findSkateSizeAllTime("sizeC13");

        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList(
                new PieChart.Data("Size 1", size1),
                new PieChart.Data("Size 2", size2),
                new PieChart.Data("Size 3", size3),
                new PieChart.Data("Size 4", size4),
                new PieChart.Data("Size 5", size5),
                new PieChart.Data("Size 6", size6),
                new PieChart.Data("Size 7", size7),
                new PieChart.Data("Size 8", size8),
                new PieChart.Data("Size 9", size9),
                new PieChart.Data("Size 10", size10),
                new PieChart.Data("Size 11", size11),
                new PieChart.Data("Size 12", size12),
                new PieChart.Data("Size C11", sizeC11),
                new PieChart.Data("Size C12", sizeC12),
                new PieChart.Data("Size C13", sizeC13)
        );

        PieChart chart = new PieChart(chartData);
        chart.setTitle("Skate Size Popularity");
        chart.setLegendVisible(false);

        StackPane admissionsALlTime = (StackPane) scene.lookup("#skateHireStackPane");
        admissionsALlTime.getChildren().add(chart);
    }

    public static void setSkateTrendsLabels(Parent root) throws SQLException {
        String mostPopularStr = DBConnect.MostPopularSize();
        String leastPopularStr = DBConnect.LeastPopularSize();
        String inMaintenanceStr = DBConnect.countSkatesInMaintenance();

        Label mostPopular = (Label) root.lookup("#mostPopularSkateSize");
        Label leastPopular = (Label) root.lookup("#leastPopularSkateSize");
        Label inMaintenance = (Label) root.lookup("#skatesInMaintenance");

        mostPopular.setText(String.valueOf(mostPopularStr));
        leastPopular.setText(String.valueOf(leastPopularStr));
        inMaintenance.setText(inMaintenanceStr);
    }
    public static void setHomeTrendsLabels(Parent root) throws SQLException {
        String mostPopularSkateSizeStr = DBConnect.MostPopularSize();
        String mostPopularCustomerTypeStr = DBConnect.MostPopularCustomerType();
        int customerAverageInt = DBConnect.totalCustomerAverage();
        int customerAverageNoSpectatorsInt = DBConnect.totalCustomerAverageNoSpec();

        Label mostPopularSkateSize = (Label) root.lookup("#mostPopularSkateSize");
        Label mostPopularCustomerType = (Label) root.lookup("#mostPopularCustomerType");
        Label customerAverage = (Label) root.lookup("#customerAveragePerSession");
        Label customerAverageNoSpec = (Label) root.lookup("#customerAveragePerSessionNoSpec");


        mostPopularSkateSize.setText(String.valueOf(mostPopularSkateSizeStr));
        mostPopularCustomerType.setText(mostPopularCustomerTypeStr);
        customerAverage.setText(String.valueOf(customerAverageInt));
        customerAverageNoSpec.setText(String.valueOf(customerAverageNoSpectatorsInt));

    }
}
