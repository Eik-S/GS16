/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import inputLists.DomainSet;
import inputLists.GroupIdSet;
import inputLists.KeywordSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

public class GS16 {
    final static String idFileDefault = "vk_id_list.txt";
    final static String keywordFileDefault = "vk_keyword_list.txt";
    final static String domainFileDefault = "vk_domain_list.txt";
    final static String groupPostPath = "postLists/$groupId.json";
    final static String idInfoPath = "vk_group_table.json";
    
    final static String groupLogFilePath = "data/groupInfo.json";
    
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

    public static void updateData( String idFile) throws IOException, JSONException{
        GroupIdSet groupIdSet = new GroupIdSet();
        groupIdSet.setGroupIdSetFromFile(idFile);
        Set<Integer> idList = groupIdSet.getGroupSet();
        for(int id : idList){
            System.out.println("\n### Processing for group " + id + " started...");
            Format formatter = new SimpleDateFormat("dd");
            Date date = new Date();
            String path = "./data/" + Integer.toString(id) + "/" + getTodaysDateFolderPath() + "/" + formatter.format(date) + ".json";
            checkFolderStructure(id);
            int offset = getNewestPost(id);
            if(offset == 0){
                offset = collectAllPostsById(id, path);
                logNewestPost(id, offset);
            } else {
                offset = collectNewPostsById(id, offset, path);
                if( offset == -1)
                    System.out.println("# No posts added.");
                else
                    logNewestPost(id, offset);
            }
        }
    }
    
    public static void checkFolderStructure( int id) {
        Date date = new Date();
        Format formatterYear = new SimpleDateFormat("YYYY");
        Format formatterMonth = new SimpleDateFormat("MM");
        
        String groupFolderPath = "./data/" + Integer.toString(id);
        String yearFolderPath = groupFolderPath + "/" + formatterYear.format(date);
        String lastYearFolderPath = groupFolderPath + "/" + ((Integer.parseInt(formatterYear.format(date))) - 1);
        String monthFolderPath = yearFolderPath + "/" + formatterMonth.format(date);
        
        File groupFolder = new File(groupFolderPath);
        File yearFolder = new File(yearFolderPath);
        File lastYearFolder = new File(lastYearFolderPath);
        File monthFolder = new File(monthFolderPath);
        String outString;
        if(!groupFolder.exists()){
            outString = "Generating new group and date folders for ";
            groupFolder.mkdir();
            yearFolder.mkdir();
            monthFolder.mkdir();
        } else if(!yearFolder.exists()){
            if(lastYearFolder.exists())
                System.out.println("||||||||||||| Happy New Year! |||||||||||||");
            outString = "Generating new year and month folders for ";
            yearFolder.mkdir();
            monthFolder.mkdir();
        } else if(!monthFolder.exists()){
            outString = "Generating new month folder for ";
            monthFolder.mkdir();
        } else {
            outString = "All Folders are up to date for ";
        }
        System.out.println("# " + outString + id);   
    }
    
    public static String getTodaysDateFolderPath() {
        Date date = new Date();
        Format formatterYear = new SimpleDateFormat("YYYY");
        Format formatterMonth = new SimpleDateFormat("MM");
        return formatterYear.format(date) + "/" + formatterMonth.format(date);
    }
    
    public static String getTodaysGroupFilePath(int groupId) {
        String group = Integer.toString(groupId);
        Format dayFormat = new SimpleDateFormat("dd");
        String dailyJson = dayFormat.format(new Date()) + ".json";
        return "data/" + group + "/" + getTodaysDateFolderPath() + "/" + dailyJson;
    }
    
    public static String readLogFile() throws FileNotFoundException {
        String path = groupLogFilePath;
        Scanner fileScanner = new Scanner(new File(path));
        String data = "";
        while(fileScanner.hasNext()){
            data += fileScanner.nextLine();
        }
        fileScanner.close();
        return data;
    }
    
    public static int getNewestPost(int groupId) throws FileNotFoundException, IOException{
        String data = readLogFile();
        if(data.equals("")){
            logNewestPost(groupId, 0);
            return 0;
        }   else {
            JSONObject logData = new JSONObject(data);
            if(logData.has(Integer.toString(groupId)))
                return logData.getInt(Integer.toString(groupId));
            else
                logNewestPost(groupId, 0);
                return 0;
        }
    }
    
    public static void logNewestPost(int groupId, int postNumber) throws FileNotFoundException, IOException {
        String logData = readLogFile();
        JSONObject groupData;
        if(logData.equals("")){
            groupData = new JSONObject();
            groupData.put(Integer.toString(groupId), postNumber);
        } else {
            groupData = new JSONObject(logData);
            if(groupData.has(Integer.toString(groupId)))
                groupData.put(Integer.toString(groupId), postNumber);
            else
                groupData.put(Integer.toString(groupId), postNumber);
        }
        FileWriter.writeToNewFile(groupData, groupLogFilePath);
    }
    
    public static int collectAllPostsById(int id, String path) throws IOException, JSONException{
        WallGet wallGet = new WallGet();
        wallGet.fillJsonArray(id);
        int postId = wallGet.getNewestPostId();
        wallGet.addNewArrayToJson();
        FileWriter.writeToNewFile(wallGet.getJSONObject(), path);
        return postId;
    }
    
    public static int collectNewPostsById(int id, int newestPost, String path) throws IOException, JSONException{
        WallGet wallGet = new WallGet();
        wallGet.updatePostList(id, newestPost);
        int postId = wallGet.getNewestPostId();
        if( postId != -1){
            wallGet.addNewArrayToJson();
            FileWriter.writeToNewFile(wallGet.getJSONObject(), path);
        }
        return postId;
    }
    
    public static void main(String[] args) throws IOException, JSONException {
        if(args.length != 0){
            if(args[0].equals("updateData") && args.length == 1){
                updateData(idFileDefault);
            }
            else if(args[0].equals("wallSearch") && args.length == 1){
                searchOnWallByIdFileKeywordFileAndWriteExtended(idFileDefault, keywordFileDefault);
            }
            else if(args[0].equals("createGroupInfo") && args.length == 1){
                addIdsToGroupTable(idFileDefault);
            }
            else if(args[0].equals("findIds") && args.length == 1){
                addDomainsToIdList(domainFileDefault);
            }
        }
    }
}