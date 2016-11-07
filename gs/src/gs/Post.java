/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs;

import java.util.*;
import javax.xml.bind.annotation.*;

/**
 * Post Objekt mit Attributen und 
 * Beans konformer Zugriffsberechtigung durch
 * getter/setter
 */
public class Post{

	private int fromId;
	private int toId;
	private Date dat;
	private String text;

	public int getFromId(){
		return fromId;
	}

	public void setFromId( int fromId){
		this.fromId = fromId;
	}

	public int getToId(){
		return toId;
	}

	public void setToId( int toId){
		this.toId = toId;
	}

	public Date getDat(){
		return dat;
	}

	public void setDat( Date dat){
		this.dat = dat;
	}

	public String getText(){
		return text;
	}

	public void setText( String text){
		this.text = text;
	}

}
