/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eike Mücksch<eikm>
 */
public class WallGet {
    private JSONObject jsonObject;
    private JSONObject post;
    private JSONArray postArray;
    private final String idUrl = "https://api.vk.com/method/wall.get?owner_id=%2d&offset=%d&count=100";
        
    public WallGet() {
        this.jsonObject = new JSONObject();
    }
    
    public JSONObject getJSONObject(){
        return jsonObject;
    }
    
    public JSONArray getJSONArray(){
        return postArray;
    }
    
    public void fillJsonArray(int groupId) throws IOException, JSONException{
        JSONArray tmpArray = new JSONArray();
        int offset = 0;
        System.out.println("Collecting Posts for " + groupId);
        while(true){
            searchWallByIdAndOffset(groupId, offset);
            setPostArray();
            for(int i = 1; i < getArrayLength(); i++)
                tmpArray.put(postArray.get(i));
            offset += 100;
            if(getArrayLength() - 1 < 100){
                postArray = tmpArray;
                break;
            }
        }
    }
    
    public void updatePostList(int groupId, int postId) throws IOException, JSONException{
        JSONArray tmpArray = new JSONArray();
        int offset = 0;
        System.out.println("# Searching new Posts for " + groupId);
        while(true){
            searchWallByIdAndOffset(groupId, offset);
            setPostArray();
            for(int i = 1; i < getArrayLength(); i++){
                JSONObject post = postArray.getJSONObject(i);
                if(postId < getPostId(post) && !post.has("is_pinned"))
                    tmpArray.put(postArray.get(i));
            }
            if(getPostId(postArray.getJSONObject(getArrayLength() - 1)) < postId) {
                if(tmpArray.length() == 0) {
                    System.out.println("# The current data is up to date.");
                    postArray = new JSONArray();
                } else {
                    System.out.println("# " + tmpArray.length() + " new posts.");
                    this.postArray = tmpArray;
                    loadExistingDailyData(groupId);
                }
                break;
            }
            else
                offset += 100;
        }
    }
    
    public void loadExistingDailyData(int groupId) throws IOException, JSONException{
        String data = "";
        String filePath = GS16.getTodaysGroupFilePath(groupId);
        File dailyData = new File(filePath);
        if(dailyData.exists()) {
            try {
                BufferedReader inStream = new BufferedReader(new FileReader(filePath));
                System.out.println("Reading existsing data from file: " + filePath);
                String line;
                while((line = inStream.readLine()) != null)
                    data += line;
                inStream.close();
            } catch (IOException e) {
            }
            JSONObject tmpObject = new JSONObject(data);
            JSONArray tmpArray = tmpObject.getJSONArray("postList");
            for(int i = 0; i < tmpArray.length(); i++)
            this.postArray.put(tmpArray.getJSONObject(i));
        }
    }
    
    public void searchWallByIdAndOffset(int id, int offset) throws IOException, JSONException {
        String url = String.format(idUrl, id, offset);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    public void setPostArray() throws JSONException{
        this.postArray = jsonObject.getJSONArray("response");
    }
    
    public static int getPostId( JSONObject jsonObject) {
        if(jsonObject.has("is_pinned"))
            return -1;
        else
            return jsonObject.getInt("id");
    }
    
    public int getNewestPostId() throws JSONException{
        if(postArray.length() == 0)
            return -1;
        post = this.postArray.getJSONObject(0);
        if(post.has("is_pinned"))
            return this.postArray.getJSONObject(1).getInt("id");
        return post.getInt("id");
    }
    
    public int getOldestPostId() throws JSONException{
        return this.postArray.getJSONObject(getArrayLength()).getInt("id");
    }
    
    public int getArrayLength() throws  JSONException{
        return postArray.length();
    }
    
    public void addNewArrayToJson() throws JSONException {
        jsonObject = new JSONObject();
        String newestPostId = Integer.toString(getNewestPostId());
        if(postArray.length() != 0) {
            for(int i = 0; i < getArrayLength(); i++)
                jsonObject.accumulate("postList",postArray.getJSONObject(i));
            System.out.println("# Daily data contains now " + getArrayLength() + " posts.");
        } else {
            System.out.println("No Posts added.");
        }
        postArray = new JSONArray();
    }
    
}
