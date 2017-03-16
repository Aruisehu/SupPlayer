/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.supplayer;

import com.supinfo.supplayer.controllers.PlayerController;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Yohann
 */
public class SupPlayer extends Application {
    private static Stage stage;
    
    private void setStage(Stage stage)
    {
        SupPlayer.stage = stage;
    }
    
    public static Stage getStage()
    {
        return SupPlayer.stage;
    }
    @Override
    public void start(Stage primaryStage) {
        
        setStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/supinfo/supplayer/views/Player.fxml"));
        try {
            Parent root = (Parent)loader.load();
            Scene scene = new Scene(root, 600, 480);
            primaryStage.setTitle("SupPlayer: Because VLC is so overrated");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
