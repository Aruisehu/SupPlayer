/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.supplayer.controllers;

import com.supinfo.supplayer.SupPlayer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Yohann
 */
public class PlayerController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private MenuItem addMusicItem;
    
    @FXML
    private TableView musicTable;
    
    private File selectedFile;
    private ArrayList<MediaPlayer> musics;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Stage primaryStage = SupPlayer.getStage();
        addMusicItem.setOnAction((a) -> {
                addMusic(a, primaryStage);
        });
    }
     
    private void addMusic(ActionEvent a, Stage primaryStage)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
            new ExtensionFilter("All Files", "*.*"));
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        try
        {
            String winPath = selectedFile.toURI().getRawPath();
            String path = convertToFileURL(winPath);
            MediaPlayer m = new MediaPlayer(new Media(path));
            musics.add(m);
            m.play();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private String convertToFileURL ( String path )
    {
        //Convert Windows URL to Java URL
        String newPath = new File(path).getAbsolutePath ();
        if ( File.separatorChar != '/' )
        {
            newPath = newPath.replace ( File.separatorChar, '/' );
        }
        if ( !newPath.startsWith ( "/" ) )
        {
            newPath = "/" + newPath;
        }
        newPath =  "file:" + newPath; //Format URI

        return newPath;
    }
    

}
