/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
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
    private final String idUrl = "http://api.vk.com/method/wall.get?owner_id=%2d&offset=%d&count=100";
    
    final static String groupPostPath = "postLists/$groupId.json";
    
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
            for(int i = 1; i <= getArrayLength(); i++)
                tmpArray.put(postArray.get(i));
            offset += 100;
            if(getArrayLength() < 100){
                postArray = tmpArray;
                break;
            }
        }
    }
    
    public void updatePostList(int groupId) throws IOException, JSONException{
        String filePath = groupPostPath.replace("$groupId", Integer.toString(groupId));
        String tmp = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
               tmp = br.readLine();
        }
        System.out.println(tmp);
    }
    
    public void searchWallByIdAndOffset(int id, int offset) throws IOException, JSONException {
        String url = String.format(idUrl, id, offset);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    public void setPostArray() throws JSONException{
        this.postArray = jsonObject.getJSONArray("response");
    }
    
    public int getNewestPostId() throws JSONException{
        post = this.postArray.getJSONObject(0);
        if(post.has("is_pinned"))
            return this.postArray.getJSONObject(1).getInt("id");
        return post.getInt("id");
    }
    
    public int getOldestPostId() throws JSONException{
        return this.postArray.getJSONObject(getArrayLength()).getInt("id");
    }
    
    public int getArrayLength() throws  JSONException{
        return postArray.length() - 1;
    }
    
    public void addNewArrayToJson() throws JSONException {
        jsonObject = new JSONObject();
        String newestPostId = Integer.toString(getNewestPostId());
        if(postArray.length() != 0) {
                jsonObject.put(Integer.toString(getArrayLength()),postArray.getJSONObject(getArrayLength()));
            for(int i = getArrayLength() - 1; i >= 0; i--)
                jsonObject.accumulate("postList",postArray.getJSONObject(i));
            System.out.println("#   " + getArrayLength() + " new Posts added.");
        } else {
            System.out.println("No Posts added.");
        }
        postArray = new JSONArray();
    }
    
}
