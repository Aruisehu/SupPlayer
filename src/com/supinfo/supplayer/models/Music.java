/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.supplayer.models;

import com.supinfo.supplayer.StringUtil;
import java.io.File;
import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author aruisehu
 */
public class Music {
    
    private final File file;
    private MediaPlayer media;
    private Music next;
    private final SimpleStringProperty name;
    private final SimpleStringProperty format;
    private final SimpleStringProperty duration;
    
    public Music(File f)
    {
        file = f;
        
        String path = StringUtil.convertToFileURL(file.toURI().getRawPath());
        String url = StringUtil.convertToFileURL(file.getAbsolutePath());
        String[] split = url.split("/");
        String uri = url.split("/")[split.length - 1];
        String[] sp = uri.split("\\.");
        format = new SimpleStringProperty(sp[sp.length - 1]);
        sp[sp.length - 1] = "";
        name = new SimpleStringProperty(String.join(".", sp));
        media = new MediaPlayer(new Media(path));
        duration = new SimpleStringProperty("");
        
        
    }
    
    public File getFile()
    {
        return file;
    }
    
    public MediaPlayer getMedia()
    {
        return media;
    }
    
    public Music getNext()
    {
        return next;
    }
    
    public void setNext(Music mus)
    {
        next = mus;
    }
            
    
    public String getName()
    {
        return name.get();
    }
    
    public String getFormat()
    {
        return format.get();
    }
    
    public String getDuration()
    {
        return duration.get();
    }
    
    public void setDuration(String s)
    {
        duration.set(s);
    }
}
