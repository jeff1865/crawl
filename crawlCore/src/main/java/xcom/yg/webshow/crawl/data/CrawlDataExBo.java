package xcom.yg.webshow.crawl.data;

import java.util.Date;

public class CrawlDataExBo extends CrawlDataBo {
	private String filteredContents ;
	private String htmlContents ;
	private String docTitle ;
	
	public CrawlDataExBo() {
		;
	}
	
	public String getDocTitle() {
		return docTitle;
	}

	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}


	public String getFilteredContents() {
		return filteredContents;
	}


	public void setFilteredContents(String filteredContents) {
		this.filteredContents = filteredContents;
	}
	
	public String getPostDate() {
		return new Date(super.getDocTs()).toString();
	}

	public String getHtmlContents() {
		return htmlContents;
	}

	public void setHtmlContents(String htmlContents) {
		this.htmlContents = htmlContents;
	}
	
	public String toString() {
		return super.toString() + " | " + this.filteredContents + " | " + this.htmlContents ;
	}
}
