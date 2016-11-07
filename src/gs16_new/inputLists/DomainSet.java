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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Eike Mücksch<eikm>
 */
public class DomainSet {
    private final String path = "/Users/Eike/Uni/NetBeansProjects/GS16_New/src/gs16_new/inputLists/";
    private final String defaultFileName = "vk_domain_list.txt";
    private final String nameFileName = "vk_name_list.txt";
    private Set<String> domainSet;
    
    public DomainSet() {
        this.domainSet = new HashSet<>();
    }
    
    public void setDomainSetFromFile(String fileName) throws FileNotFoundException, IOException{
        BufferedReader bufferedReader = new BufferedReader( new FileReader(path + fileName));
        String line;
        while((line = bufferedReader.readLine()) != null){
            domainSet.add(line);
        }
    }
    
    public Set getDomainSet(){
        return this.domainSet;
    }
    
    public void addDomain(String domain){
        this.domainSet.add(domain);
    }
    
    public void addDomainSet(Set domainSet){
        this.domainSet.addAll(domainSet);
    }
    
    public void addSetToFile() throws IOException{
        if(domainSet.isEmpty()){
            throw new IllegalArgumentException("Nothing to write, set is empty.");
        }
        setDomainSetFromFile(defaultFileName);
        try (java.io.FileWriter fileWriter = new java.io.FileWriter(path + defaultFileName)) {
            Iterator it = domainSet.iterator();
            while(it.hasNext()){
                fileWriter.write(it.next().toString() + "\n");
            }   fileWriter.close();
        }
    }
}
