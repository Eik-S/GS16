/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gs;

import java.util.*;
import javax.xml.bind.annotation.*;

/**
 * Post List mit JAXB Root zur XML erstellung
 */
@XmlRootElement(name="postCollect")
public class PostCollection {

	private List<Post> postList;

	public List<Post> getPostList(){
		if( postList == null)
			postList = new LinkedList<>();
		return postList;
	}

	public void setPostList(List<Post> postList){
		this.postList = postList;
	}
}
