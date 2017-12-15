package com.yg.webshow.crawl.seeds.sample.clien;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.webdoc.template.DComment;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class NewClienParkV2 implements IDocWrapper {
	
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
		WebDocBbs wdb = new WebDocBbs();
		try {
			Document doc = Jsoup.connect(url).get();
			String contTitle = doc.select("div[class=post_article fr-view]").toString();
			System.out.println("Contents :" + contTitle);
			wdb.setContTitle(contTitle);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return wdb;
	}

	@Override
	public List<DComment> getComments(String url) {
		List<DComment> lstComment = new ArrayList<DComment>() ;
		
		try {
			Document doc = Jsoup.connect(url).get();
			
			Elements metaUrl = doc.select("div[class=comment_row]");
			int i = 0; 
			for(Element elem : metaUrl) {
				String commentLine = elem.select("div[class=comment_view]").text() ;
				String userId = elem.attr("data-author-id");
				String strDate = elem.select("span[class=timestamp]").text() ;
				
				System.out.println(i ++ + "\t" + strDate + "\t" + userId + "\n\tComment >" + commentLine);
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
		
		System.out.println("----------------------------------");
		String url = "https://www.clien.net/service/board/park/11534985?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default";
		url = "https://www.clien.net/service/board/park/11535007?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default";
		test.getContent(url) ;
		
		System.out.println("----------------------------------");
		test.getComments("https://www.clien.net/service/board/park/11540127/comment?order=date&po=0&ps=100&writer=myclienidy") ;
		
	}

}
