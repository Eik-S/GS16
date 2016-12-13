/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inputLists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Eike Mücksch
 */
public class GroupIdSet {
    private final String relPath = "./src/inputLists/";
    private final String defaultFileName = "vk_id_list.txt";

    public GroupIdSet(Set<Integer> idSet) {
        this.idSet = idSet;
    }
    
    private Set<Integer> idSet;
    

    public GroupIdSet() {
        this.idSet = new HashSet<>();
    }
    
    public void setGroupIdSetFromFile(String fileName) throws FileNotFoundException, IOException{
        Scanner fileScanner = new Scanner(new File(relPath + fileName));
        int line;
        while(fileScanner.hasNext()){
            idSet.add(fileScanner.nextInt());
        }
    }
    
    public Set getGroupSet(){
        return idSet;
    }
    
    public void addId(int id){
        idSet.add(id);
    }
    
    public void addIdSet(Set<Integer> idSet){
        this.idSet.addAll(idSet);
    }
    
    public void addSetToFile() throws IOException{
        if( idSet.isEmpty()){
            throw new IllegalArgumentException("Nothing to write, set is empty.");
        }
        setGroupIdSetFromFile(defaultFileName);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(relPath + defaultFileName)){
            Iterator it = idSet.iterator();
            while(it.hasNext()){
                fileWriter.write((Integer)it.next() + "\n");
            }   fileWriter.close();
        }
    }

}
