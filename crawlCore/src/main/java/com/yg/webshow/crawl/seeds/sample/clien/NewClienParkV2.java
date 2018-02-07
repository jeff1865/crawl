package com.yg.webshow.crawl.seeds.sample.clien;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mortbay.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.webdoc.template.DComment;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class NewClienParkV2 implements IDocWrapper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String seedUrl = null ;
	private final String seedId = "clien.park";
	
	public NewClienParkV2(String seedUrl) {
		this.seedUrl = seedUrl ;
	}
	
	@Override
	public WebDocBbsList getList() {
		WebDocBbsList webDocBbsList = new WebDocBbsList();
		List<DbbsTitleLine> titleLines = new ArrayList<DbbsTitleLine>();
		
		try {
			Document doc = Jsoup.connect(this.seedUrl).get();
			
			Elements list = doc.select("div[class=list_item symph_row]");
			System.out.println("List of selection :" + list.size()); 
			
			Iterator<Element> itrElem = list.iterator();
			DbbsTitleLine line = null;
			
			while(itrElem.hasNext()) {
				
				Element tag = itrElem.next();
				line = new DbbsTitleLine();
				String url = tag.select("div[class=list_title] > a[class=list_subject]").attr("abs:href") ;
				String anchorTitle = tag.select("div[class=list_title] > a[class=list_subject]").text() ;
				String number = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
				String author = tag.select("span[class=nickname]").text() ;
				if(author == null || author.trim().length() == 0) {
					author = tag.select("span[class=nickname] > img[src]").attr("alt");
				}
				String timestamp = tag.select("span[class=timestamp]").text();
//				System.out.println("\n" + number + " : " + timestamp + "\n" + url + "\n" + anchorTitle + "\n" + author);
				
				line.setAuthor(author);
				line.setUrl(url);
				line.setDate(timestamp);
				line.setNo(number);
				line.setTitle(anchorTitle);
				
				titleLines.add(line) ;
			}
			
			webDocBbsList.setTitleLines(titleLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return webDocBbsList;
	}

	@Override
	public WebDocBbs getContent(String url) {
		WebDocBbs wdb = null ;
		try {
			wdb = new WebDocBbs();
			Document doc = Jsoup.connect(url).get();
			
//			wdb.setContentsHtml(doc.select("div[class=post_article fr-view]").toString());
			wdb.setContentsHtml(doc.select("div[class=post_content]").toString());
			wdb.setDocTitle(doc.select("title").text());
			wdb.setContTitle(doc.select("h3[class=post_subject]").text());
			wdb.setContentsText(doc.select("meta[name=description]").attr("content"));
			
			String postId = doc.select("input[id=boardSn]").attr("value");
			String writer = doc.select("input[id=writer]").attr("value");
			wdb.setComment(this.getComments(postId, writer));
			
		} catch(Exception e) {
//			e.printStackTrace();
			log.error("Invalid Link :" + e.getMessage());
			return null ;
		}
		
		return wdb;
	}
	
	private List<DComment> getComments(String postId, String writer) {
		String url = "https://www.clien.net/service/board/park/" + postId + "/comment?order=date&po=0&ps=100&writer=" + writer ;
		System.out.println("Comment URL >" + url);
		return this.getComments(url) ;
	}
	
	
	@Override
	public List<DComment> getComments(String url) {
		List<DComment> lstComment = new ArrayList<DComment>() ;
		
		try {
			Document doc = Jsoup.connect(url).get();
			
			Elements metaUrl = doc.select("div[class=comment_row]");
			int i = 0; 
			DComment cmt = null ;
			for(Element elem : metaUrl) {
				String commentLine = elem.select("div[class=comment_view]").text() ;
				String userId = elem.attr("data-author-id");
				String strDate = elem.select("span[class=timestamp]").text() ;
				String commentId = elem.attr("data-comment-sn");
				
				cmt = new DComment();
				cmt.setAuthor(userId);
				cmt.setStrDate(strDate);
				cmt.setComment(commentLine);
				cmt.setId(commentId);
				
				lstComment.add(cmt) ;
			}
		} catch(IOException e) {
			e.printStackTrace(); 
		}
		
		return lstComment ;
	}

	@Override
	public String getSeedId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDateFormat() {
		return "yyyy-MM-dd HH:mm:ss";
	}
	
	public static void main(String ... v) {
		System.out.println("Start Crawl2 .. " + System.currentTimeMillis());
		NewClienParkV2 test = new NewClienParkV2("https://www.clien.net/service/board/park");
		WebDocBbsList list = test.getList();
		
		int i = 0;
		for(DbbsTitleLine bts : list.getTitleLines()) {
			System.out.println(i++ + "\t" + bts.toString());
		}
		
		System.out.println("[Contents]----------------------------------");
		String url = "https://www.clien.net/service/board/park/11534985?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default";
		url = "https://www.clien.net/service/board/park/11535007?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default";
		url = "https://www.clien.net/service/board/park/11728586";
		System.out.println("===> Cont :\n"+ test.getContent(url)) ;
			
//		List<DComment> comments = test.getComments("https://www.clien.net/service/board/park/11540127/comment?order=date&po=0&ps=100&writer=myclienidy") ;
//		System.out.println("[Comment]----------------------------------");
//		for(DComment comment : comments) {
//			System.out.println(comment);
//		}
		
		
	}

}
