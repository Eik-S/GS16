/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs16_new;

import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eike Mücksch
 */
public class FileWriter {
    public static String defaultPath = "default.json";
    
    public static void writeToNewFile(JSONObject jsonObject) throws IOException, JSONException{
        if(jsonObject.length() == 0){
            throw new IllegalArgumentException("The jsonObject is empty");
        }
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(defaultPath)) {
            String jsonString = jsonObject.toString(4);
            fileWriter.write(jsonString);
            fileWriter.close();
        }
    }
    
    public static void writeToNewFile(JSONObject jsonObject, String path) throws IOException, JSONException{
        if(jsonObject.length() == 0){
            throw new IllegalArgumentException("The jsonObject is empty");
        }
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(path)) {
            String jsonString = jsonObject.toString(4);
            fileWriter.write(jsonString);
            fileWriter.close();
        }
    }
    
    public static void writeToNewFile(JSONArray jsonArray) throws IOException, JSONException{
        if(jsonArray.length() == 0)
            throw new IllegalArgumentException("The jsonArray is empty");
        try( java.io.FileWriter fileWriter = new java.io.FileWriter(defaultPath)) {
            String jsonString = jsonArray.toString(4);
            fileWriter.write(jsonString);
            fileWriter.close();
            System.out.println(jsonArray.length());
        }
    }
    
    public static void appendToFile(JSONObject jsonObject, String path) throws IOException, JSONException{
        if(jsonObject.length() == 0){
            throw new IllegalArgumentException("The jsonObject is empty");
        }
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(path, true)) {
            String jsonString = jsonObject.toString(4);
            fileWriter.write("\n" + jsonString);
            fileWriter.close();
        }
    }
}
