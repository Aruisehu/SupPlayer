/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.supplayer.controllers;

import com.supinfo.supplayer.StringUtil;
import com.supinfo.supplayer.SupPlayer;
import com.supinfo.supplayer.models.Music;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Music> musicTable;
    private final ObservableList<Music> musicList = 
            FXCollections.observableArrayList();
    
    @FXML
    private TableColumn titleColumn;
    @FXML
    private TableColumn durationColumn;
    @FXML
    private TableColumn formatColumn;
    
    private File selectedFile;  
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Stage primaryStage = SupPlayer.getStage();
        addMusicItem.setOnAction((a) -> {
                addMusic(a, primaryStage);
        });
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        durationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration")
        );
        formatColumn.setCellValueFactory(
                new PropertyValueFactory<>("format")
        );
        musicTable.setItems(musicList);
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
            if (selectedFile != null)
            {
                
                String winPath = selectedFile.toURI().getRawPath();
                String path = StringUtil.convertToFileURL(winPath);
                musicList.add( 
                        new Music(selectedFile)
                );
                MediaPlayer m = new MediaPlayer(new Media(path));
                musicTable.refresh();
                musicList.get(0).getMedia().play();

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    

}
