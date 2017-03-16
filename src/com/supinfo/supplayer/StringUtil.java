/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supinfo.supplayer;

import java.io.File;

/**
 *
 * @author aruisehu
 */
public class StringUtil {
    public static String convertToFileURL ( String path )
    {
        //Convert Windows URL to Java URL
        if ( File.separatorChar != '/' )
        {
            path = path.replace ( File.separatorChar, '/' );
        }
        if ( !path.startsWith ( "/" ) )
        {
            path = "/" + path;
        }
        path =  "file:" + path; //Format URI

        return path;
    }
}
