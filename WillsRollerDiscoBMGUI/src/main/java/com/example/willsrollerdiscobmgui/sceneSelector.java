package com.example.willsrollerdiscobmgui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class sceneSelector {
    Button homeButton, announcementsButton, ticketsButton, skateHireButton, maintenanceButton;
   private Stage stage;
   private Scene scene;
   private Parent root;

   @FXML
   public void switchToHome(ActionEvent event) throws IOException{
       root = FXMLLoader.load(getClass().getResource("home.fxml"));
       stage = (Stage)((Node)event.getSource()).getScene().getWindow();
       scene = new Scene(root);
       stage.setScene(scene);
       stage.show();
   }

    @FXML
    public void switchToAnnouncements(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("announcements.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToTickets(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSkateHire(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("skatehire.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void buttonPress(ActionEvent event) throws IOException{

    }
}
