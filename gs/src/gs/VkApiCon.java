/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs;

import java.net.*;
import java.io.*;
import java.util.Date;
import javax.xml.bind.*;
import java.util.Scanner;
/**
 * Hauptklasse des Programms, sucht auf vk.com in 
 * angegebenen Gruppen nach Posts welche auf Keywords matchen 
 * und speicher diese automatisiert in einer XML Datei.
 * 
 * @author Eike Mücksch
 * 
 * @version 0.6
 */
public class VkApiCon {

    /**
     * Schreibt die gesammelten Posts in eine XML-Datei
     * @param      postList       Gesammelte Post-Liste
     * @param      file           Datei zum beschreiben
     */
    private static void writePost(PostCollection postList, File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(PostCollection.class);
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        m.marshal(postList, file);
      }

    /**
     * @param      url        Übergebene URL mit wall.search(id, keyword) Funktion
     * @return     gibt grob formatierten String von vk.com Posts mit zutreffendem Keyword zurück.
     * @throws java.lang.Exception
     */
    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);
        in.close();
        return response.toString();
    }

    /**
     * Schneidet nächstvorkommenden "From", "to", bzw. "Datum" Tag aus content
     * @param      content  String von vk.com Posts
     * @param      type     "from_id", "to_id" oder "date"
     * @return     id oder datum (id bei Gruppen negativ)
     */
    public static int cutData(String content, String type){
        String startSearch = String.format("\"%s\":", type);
        int startLoc = content.indexOf(startSearch);
        int endLoc = content.indexOf(",", startLoc);

        return Integer.parseInt(content.substring(startLoc + startSearch.length(), endLoc));
    }
    /**
     * Schneidet den ersten Post aus content
     * @param      content  String von vk.com Posts
     * @return     Post String
     */
    public static String cutText(String content){
        int startLoc = content.indexOf("\"text\":\"");
        int endLoc = content.indexOf(",\"comments\"");
        String text = content.substring(startLoc + 8, endLoc);
        //Noch nicht ausgereift
        //text = trimText(text);
        return text;
    }


    /**
     *Erstellt Post objekt und weißt Attribute aus vk.com String mittels
     *nutzen der cut Methoden 
     * @param      content  String von vk.com Posts
     * @return     Post Objekt
     */
    public static Post makePost(String content){
        Post post = new Post();
        post.setFromId(cutData(content, "from_id"));
        post.setToId(cutData(content, "to_id"));
        //transform Unix-Time to Date format
        int unixDat = cutData(content, "date");
        Date dat = new Date(unixDat * 1000L);
        post.setDat(dat);
        post.setText(cutText(content));
        return post;
    }


    /**
     * Sucht mittels getText() Methode nach Posts in bestimmter Gruppe, 
     * erstellt für jeden Post ein Post Objekt und speichert diese in einer Liste.
     * @param      id         Gruppen/Personen id (bei Gruppen negativ)
     * @param      keyword    Keyword (sollen mehrere Wörter in Post sein durch - Seperator möglich z.B. bla-blubb)
     * @param      postList   Die Liste in welcher alle gefundenen und formatierten Posts gespeichert sind
     * @return     Liste an Posts
     * @throws java.lang.Exception
     */
    public static PostCollection searchFor(Integer id, String keyword, PostCollection postList) throws Exception{
        //owner_id durch domain und id durch domain-name 
	// (bei http://vk.com/compact.magazin = compact.magazin) austauschen
        String link = String.format("http://api.vk.com/method/wall.search?owner_id=%2d&count=100&query=%s", id, keyword);
        String content = getText(link);
        int sizeBefore = postList.getPostList().size();
        while(content.indexOf(",\"comments\"") > 0){
            Post posting = makePost(content);
            postList.getPostList().add(posting);
            content = content.substring(content.indexOf(",\"comments\"") + 1);
        }
        //Berechnet die Anzahl gefundener Posts zu bestimmtem Keyword 
        //durch subtrahieren der alten länge der postList von aktueller.
        System.out.println((postList.getPostList().size() - sizeBefore) + 
		" Posts mit dem Keyword " + keyword + " gefunden.");
        return postList;
    }

    /**
     * Schneidet Anhänge aus dem Post
     * @param      text  Übergebener Post-Text String
     * @return     getrimmter Post-Text String
     */
    public static String trimText( String text){
        int start = text.indexOf("\",\"attachment\"");
        int end = text.indexOf("}}," , start);
        if(start > 0 && end > 0){
            String before = text.substring( 0 , start);
            String after = text.substring( end + 3);
            text = before + " " + after;
        }
        start = text.indexOf("\"attachments\":");
        end = text.indexOf("}}]");
        if(start > 0 && end > 0){
            String before = text.substring( 0 , start);
            String after = text.substring( end + 3);
            text = before + " " + after;
        }
        return text;
    }
    /**
     * Kleine Eingabeaufforderung mit Erklärung.
     * 
     * ablaufen des keywords arrays um für jedes Keyword searchFor() auszuführen. 102277414
     * @param args
      */
    public static void main(String[] args) throws Exception {

        PostCollection postList = new PostCollection();      
        File vkPosts = new File("vkPostList.xml"); 

        Scanner scanner = new Scanner(System.in);
        System.out.println("Auf welchen Seiten möchtest du suchen? Gib nichts "
		+ "ein um in Pegida zu suchen. \n(id eingeben, bei gruppen negativ)");
        String input = scanner.nextLine();
        //Pegida als Standartsuche, ansonsten die eingegebenen ids
        String[] ids;
        if(input.length() == 0){
            ids = new String[1];
            ids[0] = "-102277414";
        }
       else
            ids = input.split(" ");

        System.out.println("Nach welchen Wörtern soll gesucht werden?");
        System.out.println("Hinweis: Sollen mehrere Wörter in einem Post "
		+ "vorkommen trenne sie bitte durch ein - , \n ansonten "
		+ "genügt ein Leerzeichen.");
	System.out.println("Test, Test");
	System.out.println("And a third one");
        input = scanner.nextLine();
        String[] keywords = input.split(" ");
        for(int j = 0; j < ids.length; j++){
            int id = Integer.parseInt(ids[j]);
            System.out.println("Für die ID " + id);
            for(int i = 0; i < keywords.length; i++){
                String keyword = keywords[i];
                searchFor(id, keyword, postList);
            }
        }
        writePost(postList, vkPosts);
    }
}
