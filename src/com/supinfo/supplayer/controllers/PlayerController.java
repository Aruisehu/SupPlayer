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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    private Slider durationSlider;
    @FXML
    private Slider volumeSlider;
    
    ///////////////////
    //Other attributes/
    ///////////////////
    
    private File selectedFile;  
    private boolean stopped; //If we reach the end of the playlist
    private Music current;
    private boolean running;
    private Duration duration;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        Stage primaryStage = SupPlayer.getStage();
        stopped = false;
        running = false;
        musicTable.setItems(musicList);
        
        //Add event everywhere
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
        
        musicTable.setOnMousePressed((ev) -> {
            if(ev.isPrimaryButtonDown() && ev.getClickCount() > 1)
            {
                Music m = musicTable.getSelectionModel().getSelectedItem();
                current.getMedia().stop();
                current = m;
                current.getMedia().play();
                duration = current.getMedia().getMedia().getDuration();
                update();

                
            }
        });
        
        volumeSlider.valueProperty().addListener((ov) -> {
           if (volumeSlider.isValueChanging()) {
               current.getMedia().setVolume(volumeSlider.getValue() / 100.0);
           }
        });
                     
        durationSlider.valueProperty().addListener((ov) -> {
            if (durationSlider.isValueChanging()) {
                // multiply duration by percentage calculated by slider position
                current.getMedia().seek(duration.multiply(durationSlider.getValue() / 100.0));
            }
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
                
                if (!musicList.isEmpty() && !stopped)
                {
                    musicList.get(musicList.size() - 1).setNext(newMusic);
                    newMusic.getMedia().setOnReady(() -> {
                        onReady(newMusic);       
                    });
                }
                else 
                {
                    newMusic.getMedia().play();
                    playButton.setText("pause");
                    running = true;
                    stopped = false;
                    current = newMusic;newMusic.getMedia().setOnReady(() -> {
                        onReady(newMusic);
                        duration = newMusic.getMedia().getMedia().getDuration();
                    });
                    
                    update();
                }
                
                musicList.add(newMusic);
                
                
                newMusic.getMedia().setOnEndOfMedia(() -> {
                    if (current.getNext() != null)
                    {
                        current.getMedia().stop();
                        current = current.getNext();
                        duration = current.getMedia().getMedia().getDuration();
                        current.getMedia().play();
                        update();
                    }
                    else 
                    {
                        stopped = true;
                    }
                });

                musicTable.refresh();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void update()
    {
        current.getMedia().currentTimeProperty().addListener((ov) -> {
            updateValues();
        });
    }
    
    private void endMusic(Music music)
    {
        MediaPlayer media = music.getMedia();
    }

    private void playAndPause()
    {
        if (running)
        {
            current.getMedia().pause();
            playButton.setText("play");
            running = false;
        }
        else
        {
            current.getMedia().play();
            playButton.setText("pause");
            running = true;
        }
    }

    protected void updateValues() {
        if (durationSlider != null && volumeSlider != null)
        {
            Platform.runLater(() -> {
                Duration currentTime = current.getMedia().getCurrentTime();
                if (!durationSlider.isDisabled() 
                        && duration.greaterThan(Duration.ZERO) 
                        && !durationSlider.isValueChanging())
                {
                    durationSlider.setValue(currentTime.divide(duration).toMillis()
                        * 100.0);
                }
                if (!volumeSlider.isValueChanging())
                {
                  volumeSlider.setValue((int)Math.round(current.getMedia().getVolume() 
                        * 100));
                }
           });
        }
    }
    
    private void onReady(Music music)
    {
        double dur;
        dur = music.getMedia().getMedia().getDuration().toSeconds();
        int minutes = (int)(dur / 60);
        int seconds = (int)(dur % 60);
        Platform.runLater(() -> {music.setDuration("" + minutes + "m " + seconds + "s");});
        musicTable.refresh();
    }
}
