/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eike Mücksch<eikm>
 */
public class Group {
    private JSONObject jsonObject;
    private final String groupIdUrl = "http://api.vk.com/method/groups.getById?group_id=%s";
    private final String groupIdsUrl = "http://api.vk.com/method/groups.getById?group_ids=%s";
    
    public Group(){
        this.jsonObject = new JSONObject();
    }
    
    public JSONObject getJsonObject() {
        return jsonObject;
    }
    
    public void searchGroupInfoById(String idString) throws IOException, JSONException{
        String url = String.format(groupIdUrl, idString);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    public int getGroupId() throws JSONException{
        Set<Integer> idSet = new HashSet();
        if(!jsonObject.has("response")){
            throw new IllegalArgumentException("The jsonObject is empty:" + jsonObject.toString());
        }
        JSONArray array = jsonObject.getJSONArray("response");
        int id = array.getJSONObject(0).getInt("gid");
        return id * -1;
    }
    
    public String getGroupName() throws JSONException{
        if(jsonObject.isNull("response")){
            return "null";
        }
        JSONArray array = jsonObject.getJSONArray("response");
        String groupName = array.getJSONObject(0).getString("name");
        return groupName;
    }
    
    public String getGroupDomain() throws JSONException{
        if(jsonObject.isNull("response")){
            return "null";
        }
        JSONArray array = jsonObject.getJSONArray("response");
        String groupDomain = array.getJSONObject(0).getString("screen_name");
        return groupDomain;
    }
    
    
}
