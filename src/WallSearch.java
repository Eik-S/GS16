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
 * @author Eike Mücksch
 */
public class WallSearch {
    private JSONObject jsonObject;
    private final String idKeywordUrl = "https://api.vk.com/method/wall.search?owner_id=%2d&count=100&query=%s";
    private final String domainKeywordUrl = "https://api.vk.com/method/wall.search?domain=%s&count=100&query=%s";
    private final String idKeywordUrlExtended = "https://api.vk.com/method/wall.search?owner_id=%2d&count=100&query=%s&extended=1";
    private final String domainKeywordUrlExtended = "https://api.vk.com/method/wall.search?domain=%s&count=100&query=%s&extended=1";
    
    public WallSearch(){
        this.jsonObject = new JSONObject();
    }
    
    public JSONObject getJsonObject(){
        return jsonObject;
    }
    
    /*
    Default Search with Keyword includes postlist with different data
    */
    public void searchWallByIdAndKeyword(int id, String query) throws IOException, JSONException{
        String url = String.format(idKeywordUrl, id, query);
        jsonObject = WebReader.readJsonFromUrl(url);
//        Group group = new Group();
//        group.searchGroupInfoById(Integer.toString(id));
//        String groupName = group.getGroupName().toString();
//        String subString = jsonObject.toString().substring(1);
//        String jsonString = "{" + groupName + "000" + query + "000" + subString;
//        jsonObject = jsonObject.getJSONObject(jsonString);
    }
    
    public void searchWallByDomainAndKeyword(String domain, String query) throws IOException, JSONException{
        String url = String.format(domainKeywordUrl, domain, query);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    /*
    Extended Search with Keyword includes groupinfo, profiles and postlist
    */
    public void searchWallByIdAndKeywordExtended(int id, String query) throws IOException, JSONException{
        String url = String.format(idKeywordUrlExtended, id, query);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    public void searchWallByDomainAndKeywordExtended(String domain, String query) throws IOException, JSONException{
        String url = String.format(domainKeywordUrlExtended, domain, query);
        jsonObject = WebReader.readJsonFromUrl(url);
    }
    
    /*
    Not working for extended search
    */
    public Set getPostOwnerIds() throws JSONException{
        Set<Integer> idSet = new HashSet<>();
        if(jsonObject.length() == 0){
            throw new IllegalArgumentException("The jsonObject is empty");
        }
        JSONArray array = jsonObject.getJSONArray("response");
        for(int i = 1; i < array.length(); i++){
            int id = array.getJSONObject(i).getInt("from_id");
            idSet.add(id);
        }
        return idSet;
    }
            
}
