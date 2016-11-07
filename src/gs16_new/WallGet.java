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
 * @author Eike Mücksch<eikm>
 */
public class WallGet {
    private JSONObject jsonObject;
    private JSONArray postArray;
    private final String idUrl = "http://api.vk.com/method/wall.get?owner_id=%2d&offset=%d&count=100";
    private final String filePath = "postLists/$groupId.json";
    
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
    
    public void searchWallByIdAndOffset(int id, int offset) throws IOException, JSONException {
        String url = String.format(idUrl, id, offset);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    public void setPostArray() throws JSONException{
        this.postArray = jsonObject.getJSONArray("response");
    }
    
    public int getNewestPostId() throws JSONException{
        return this.postArray.getJSONObject(1).getInt("id");
    }
    
    public int getOldestPostId() throws JSONException{
        return this.postArray.getJSONObject(postArray.length() - 1).getInt("id");
    }
    
    public int getArrayLength() throws  JSONException{
        return postArray.length() - 1;
    }
    
    public void addNewArrayToJson() throws JSONException {
        jsonObject = new JSONObject();
        if(postArray.length() != 0) {
                jsonObject.put("postList",postArray.getJSONObject(getArrayLength()));
            for(int i = getArrayLength() - 1; i >= 0; i--)
                jsonObject.accumulate("postList",postArray.getJSONObject(i));
            System.out.println(getArrayLength() + " new Posts added.");
        } else {
            System.out.println("No Posts added.");
        }
        postArray = new JSONArray();
    }
    
}
