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
import javafx.application.Platform;
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

    /////////////////////
    //FXML constants/////
    /////////////////////
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
    @FXML
    private Button playButton;
    
    ///////////////////
    //Other attributes/
    ///////////////////
    
    private File selectedFile;  
    private boolean played;
    private Music current;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Stage primaryStage = SupPlayer.getStage();
        played = false;
        musicTable.setItems(musicList);
        
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        durationColumn.setCellValueFactory(
                new PropertyValueFactory<>("duration")
        );
        formatColumn.setCellValueFactory(
                new PropertyValueFactory<>("format")
        );
        
        addMusicItem.setOnAction((a) -> {
                addMusic(a, primaryStage);
        });
        
        playButton.setOnAction((a) -> {
            playAndPause();
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
            if (selectedFile != null)
            {
                
                String winPath = selectedFile.toURI().getRawPath();
                String path = StringUtil.convertToFileURL(winPath);
                Music newMusic = new Music(selectedFile);
                if (!musicList.isEmpty())
                {
                    musicList.get(musicList.size() - 1).setNext(newMusic);
                } else 
                {
                    newMusic.getMedia().play();
                    current = newMusic;

                }
                musicList.add(newMusic);
                newMusic.getMedia().setOnReady(() -> {
                    double dur;
                    dur = newMusic.getMedia().getMedia().getDuration().toSeconds();
                    int minutes = (int)(dur / 60);
                    int seconds = (int)(dur % 60);
                    Platform.runLater(() -> {newMusic.setDuration("" + minutes + "m " + seconds + "s");});
                });
                
                newMusic.getMedia().setOnEndOfMedia(() -> {
                    if (current.getNext() != null)
                    {
                       current.getMedia().stop();
                       current = current.getNext();
                       current.getMedia().play();
                        System.out.println("End");
                    }
                });
                System.out.println(current.getNext().getName());
                musicTable.refresh();


            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        
    }
    
    private void endMusic(Music music)
    {
        MediaPlayer media = music.getMedia();
    }
    private void playAndPause()
    {
        
    }
}
