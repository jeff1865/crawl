package com.yg.webshow.crawl.data.tables;

import java.util.Date;

public class CrawlRow {
	private String siteId ;
	private String postId ;
	private String anchorTitle ;
	private String author ;
	private long docTs ;
	private String status ;
	private String url ;
	private String filteredContents ;
	private String htmlContents ;
	private String docTitle ;
	
	private long updateDate ;
	private int updateCount ;
	
	public String toString() {
		StringBuffer sb = new StringBuffer() ;
		sb.append(this.siteId).append("\t");
		sb.append(this.postId).append("\t");
		sb.append(this.anchorTitle).append("\t");
		sb.append(this.filteredContents).append("\t");
		sb.append(this.docTitle).append("\t");
		sb.append(this.author).append("\t");
		sb.append(new Date(this.docTs)).append("\t");
		sb.append(this.status).append("\t");
		sb.append(this.url).append("\t");
		sb.append(new Date(this.updateDate)).append("\t");
		sb.append(this.updateCount).append("\t");
		
		return sb.toString() ;
	}
	public String getStrUpdateDate() {
		return new Date(updateDate).toString();
	}
	
	public long getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(long updateDate) {
		this.updateDate = updateDate;
	}

	public int getUpdateCount() {
		return updateCount;
	}

	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
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
	public String getAnchorTitle() {
		return anchorTitle;
	}
	public void setAnchorTitle(String anchorTitle) {
		this.anchorTitle = anchorTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public long getDocTs() {
		return docTs;
	}
	public void setDocTs(long docTs) {
		this.docTs = docTs;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilteredContents() {
		return filteredContents;
	}
	public void setFilteredContents(String filteredContents) {
		this.filteredContents = filteredContents;
	}
	public String getHtmlContents() {
		return htmlContents;
	}
	public void setHtmlContents(String htmlContents) {
		this.htmlContents = htmlContents;
	}
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	
	public String getDocDateTime() {
		return new Date(this.docTs).toString();
	}
}
