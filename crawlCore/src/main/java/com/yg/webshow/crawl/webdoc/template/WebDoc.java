package com.yg.webshow.crawl.webdoc.template;

import java.util.Date;
import java.util.List;

/**
 * Standard Web Document Template (Tagging Interface)
 */
public class WebDoc {
	protected String docTitle ;
	protected String contTitle ;
	protected String contentsText ;
	private String contentsHtml ;
	protected Date docDate ;
	protected List<String> meta ;
	
	public String toString() {
		StringBuffer sb = new StringBuffer() ;
		sb.append("docTitle:").append(this.docTitle).append("|\n");
		sb.append("docDate:").append(this.docDate).append("|\n");
		sb.append("contTitle:").append(this.contTitle).append("|\n");
		sb.append("contents:").append(this.contentsText).append("|\n");
		sb.append("htmlContents:").append(this.contentsHtml);
		return sb.toString();
	}
	
	public String getDocTitle() {
		return docTitle;
	}
	public void setDocTitle(String docTitle) {
		this.docTitle = docTitle;
	}
	public String getContTitle() {
		return contTitle;
	}
	public void setContTitle(String contTitle) {
		this.contTitle = contTitle;
	}
	public String getContentsText() {
		return contentsText;
	}
	public void setContentsText(String contentsText) {
		this.contentsText = contentsText;
	}
	public Date getDocDate() {
		return docDate;
	}
	public void setDocDate(Date docDate) {
		this.docDate = docDate;
	}
	public List<String> getMeta() {
		return meta;
	}
	public void setMeta(List<String> meta) {
		this.meta = meta;
	}

	public String getContentsHtml() {
		return contentsHtml;
	}

	public void setContentsHtml(String contentsHtml) {
		this.contentsHtml = contentsHtml;
	}
}
