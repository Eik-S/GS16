/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs16_new;

import gs16_new.inputLists.DomainSet;
import gs16_new.inputLists.GroupIdSet;
import gs16_new.inputLists.KeywordSet;
import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.Attributes.Name;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

public class GS16_New {
    final static String idFileDefault = "vk_id_list.txt";
    final static String keywordFileDefault = "vk_keyword_list.txt";
    final static String domainFileDefault = "vk_domain_list.txt";
    final static String groupPostPath = "postLists/$groupId.json";
    final static String idInfoPath = "vk_group_table.json";
    
    public static void searchOnWallAndWriteExtended(int id, String keyword, String filename) throws IOException, JSONException{
        WallSearch wallSearch = new WallSearch();
        wallSearch.searchWallByIdAndKeywordExtended(id,keyword);
        FileWriter.appendToFile(wallSearch.getJsonObject(), filename);
        
    }
    
    public static void searchOnWallByIdFileKeywordFileAndWriteExtended(String idFileName, String keywordFileName) throws IOException, JSONException{
        GroupIdSet groupIdSet = new GroupIdSet();
        groupIdSet.setGroupIdSetFromFile(idFileName);
        Set<Integer> idList = groupIdSet.getGroupSet();
        
        KeywordSet keywordSet = new KeywordSet();
        keywordSet.setKeywordSetFromFile(keywordFileName);
        Set<String> keywordList = keywordSet.getKeywordSet();
        Format formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
        Date date = new Date();
        File dir = new File(formatter.format(date));
        dir.mkdir();
        for(String keyword : keywordList){ 
            String filename = formatter.format(date) + "/" + keyword + ".json";
            for(int id : idList){
                searchOnWallAndWriteExtended(id, keyword, filename);
            }
        }
    }
    
    public static void collectAllPostsByIdList(String idFileName) throws IOException, JSONException{
        GroupIdSet groupIdSet = new GroupIdSet();
        groupIdSet.setGroupIdSetFromFile(idFileName);
        Set<Integer> idList = groupIdSet.getGroupSet();
        for(int id : idList){
            WallGet wallGet = new WallGet();
            wallGet.fillJsonArray(id);
            wallGet.addNewArrayToJson();
            String filePath = groupPostPath.replace("$groupId", Integer.toString(id));
            FileWriter.writeToNewFile(wallGet.getJSONObject(), filePath);
        }
        
    }
    
    public static void addDomainsToIdList(String domainListName) throws IOException, JSONException{
        DomainSet domainSet = new DomainSet();
        domainSet.setDomainSetFromFile(domainListName);
        Set<String> domainNames = new HashSet<>();
        domainNames = domainSet.getDomainSet();
        GroupIdSet groupIdSet = new GroupIdSet();
        for(String domainName : domainNames){
            Group group = new Group();
            group.searchGroupInfoById(domainName);
            groupIdSet.addId(group.getGroupId());
        }
        groupIdSet.addSetToFile();
    }
    
    public static void addIdsToGroupTable( String idListName) throws IOException, JSONException{
        GroupIdSet groupIdSet = new GroupIdSet();
        groupIdSet.setGroupIdSetFromFile(idListName);
        Set<Integer> idIntegers = groupIdSet.getGroupSet();
        JSONObject idInfo = new JSONObject();
        for(int id : idIntegers){
            String idString = new String();
            JSONArray groupInfoArray = new JSONArray();
            if(id < 0)
                idString = Integer.toString(id * -1);
            else
                idString = Integer.toString(id);
            Group group = new Group();
            group.searchGroupInfoById(idString);
            JSONObject json = new JSONObject();
            json.put("Name",group.getGroupName());
            json.put("Domain",group.getGroupDomain());
            json.put("Url","www.vk.com/" + group.getGroupDomain());
            idInfo.put(idString, json);
        }
        FileWriter.writeToNewFile(idInfo, idInfoPath);
    }

    public static void searchPostIdsInGroup(String keyword) throws IOException, JSONException{
        GroupIdSet groupIdSet = new GroupIdSet();
        groupIdSet.setGroupIdSetFromFile("vk_id_list.txt");
        Set<Integer> idSet = groupIdSet.getGroupSet();
        for(int id : idSet){
            WallSearch wallSearch = new WallSearch();
            wallSearch.searchWallByIdAndKeyword(id, keyword);
            groupIdSet.addIdSet(wallSearch.getPostOwnerIds());
            groupIdSet.addSetToFile();
        }
    }
    
    public static void main(String[] args) throws IOException, JSONException {
        
//        searchOnWallByIdFileKeywordFileAndWriteExtended(idFileDefault, keywordFileDefault);
        
//        collectAllPostsByIdList(idFileDefault);
        addIdsToGroupTable(idFileDefault);
    }
}