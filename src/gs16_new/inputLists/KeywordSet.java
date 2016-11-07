/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs16_new.inputLists;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Eike Mücksch<eikm>
 */
public class KeywordSet {
    private final String path = "/Users/Eike/Uni/NetBeansProjects/GS16_New/src/gs16_new/inputLists/";
    private Set<String> keywordSet;

    public KeywordSet() {
        this.keywordSet = new HashSet<>();
    }
    
    public void setKeywordSetFromFile(String fileName) throws FileNotFoundException, IOException{
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path + fileName));
        String line;
        while((line = bufferedReader.readLine()) != null){
            keywordSet.add(line);
        }
    }
    
    public Set getKeywordSet(){
        return this.keywordSet;
    }
    
    public void addKeyword(String keyword){
        this.keywordSet.add(keyword);
    }
    
}
