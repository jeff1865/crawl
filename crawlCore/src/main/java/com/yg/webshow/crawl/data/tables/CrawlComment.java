package com.yg.webshow.crawl.data.tables;

import java.util.Date;

public class CrawlComment {
	private String siteId ;
	private String postId ;
	private String commentNo ;
	private String user ;
	private long initTime ;
	private String contents ;
	
	public String toString() {
		StringBuffer sb = new StringBuffer() ;
		sb.append(this.siteId).append("\t") ;
		sb.append(this.postId).append("\t") ;
		sb.append(this.commentNo).append("\t");
		sb.append(this.user).append("\t");
		sb.append(new Date(this.initTime)).append("\t");
		if(this.contents != null) sb.append(this.contents.trim()) ;
				
		return sb.toString() ;
	}
	public String getStrTimestamp() {
		return new Date(this.initTime).toString();
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getCommentNo() {
		return commentNo;
	}
	public void setCommentNo(String commentNo) {
		this.commentNo = commentNo;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public long getInitTime() {
		return initTime;
	}
	public void setInitTime(long initTime) {
		this.initTime = initTime;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
