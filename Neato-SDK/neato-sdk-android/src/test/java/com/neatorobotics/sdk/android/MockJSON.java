package com.neatorobotics.sdk.android;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

/**
 * Created by Marco on 21/03/16.
 */
public class MockJSON {

    public static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource("test/"+fileName);
        if(resource == null) return null;
        else return new File(resource.getPath());
    }

    public static String loadJSON(Object obj,String filename) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFileFromPath(obj,filename)));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (Exception e) {
            Log.e("Exception","Exception",e);return null;}
        return text.toString();
    }
}