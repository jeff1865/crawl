package com.yg.webshow.crawl.seeds.sample.nhnnews;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.webdoc.template.DComment;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class NaverStockNews implements IDocWrapper {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private String seedUrl = "http://finance.naver.com/news/mainnews.nhn" ;
	private String seedId = "naver.stock.news";
	
	private SimpleDateFormat defaultDateFormat = null ;
	
	public NaverStockNews() {
		this.defaultDateFormat = new SimpleDateFormat(this.getDateFormat());
	}
	
	@Override
	public WebDocBbsList getList() {
		WebDocBbsList webDocBbsList = new WebDocBbsList();
		List<DbbsTitleLine> titleLines = new ArrayList<DbbsTitleLine>();
		
		try {
			Document doc = Jsoup.connect(this.seedUrl).get();
			
			Elements list = doc.select("li[class=block1]");
			System.out.println("List of selection :" + list.size()); 
			
			Iterator<Element> itrElem = list.iterator();
			DbbsTitleLine line = null;
			
			while(itrElem.hasNext()) {
				Element tag = itrElem.next();
				line = new DbbsTitleLine();
				
				String url = tag.select("*[class=articleSubject] > a").attr("abs:href") ;
				String anchorTitle = tag.select("*[class=articleSubject] > a").text() ;
				int i = url.indexOf("article_id=") + "article_id=".length();
				String number = url.substring(i, url.indexOf("&", i));
				String author = tag.select("span[class=press]").text() ;
				String timestamp = tag.select("span[class=wdate]").text() ;

				line.setAuthor(author);
				line.setUrl(url);
				line.setDate(timestamp);
//				line.setNo(number);		//TODO need to change to timeString !!!! --> to effect rowKey value
				line.setNo(this.convertDateStr2Key(timestamp) + "_" + number);
				line.setTitle(anchorTitle);

				titleLines.add(line) ;
			}
			
			webDocBbsList.setTitleLines(titleLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return webDocBbsList;
	}

	private String convertDateStr2Key(String timestamp) {
		try {
			Date date = this.defaultDateFormat.parse(timestamp) ;
			return String.valueOf(Long.MAX_VALUE - date.getTime()) ;
		} catch (ParseException e) {
			e.printStackTrace();
			return "E" + (Long.MAX_VALUE - System.currentTimeMillis()) ;
		}
	}
	
	@Override
	public WebDocBbs getContent(String url) {
		WebDocBbs wdb = null ;
		
		try {
			wdb = new WebDocBbs();
			Document doc = Jsoup.connect(url).get();
			
			wdb.setContentsHtml(doc.select("div[class=articleCont]").toString());
			wdb.setDocTitle(doc.select("title").text()); 
			wdb.setContTitle(doc.select("div[class=article_info] > h3").text());
			wdb.setContentsText(doc.select("div[class=articleCont]").text());
			
			String postId = doc.select("input[id=boardSn]").attr("value");
			String writer = doc.select("input[id=writer]").attr("value");
			
//			wdb.setComment(this.getComments(postId, writer));
			
		} catch(Exception e) {
//			e.printStackTrace();
			log.error("Invalid Link :" + e.getMessage());
			return null ;
		}
		
		return wdb;
	}

	@Override
	public List<DComment> getComments(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSeedId() {
		return this.seedId;
	}

	@Override
	public String getDateFormat() {
		return "yyyy-MM-dd HH:mm:ss";
	}
	
	public static void main(String ... v) {
		NaverStockNews test = new NaverStockNews() ;
		
		WebDocBbsList list = test.getList() ;
		
		List<DbbsTitleLine> titleLines = list.getTitleLines();
		int i = 0;
		for(DbbsTitleLine dtl : titleLines) {
			System.out.println("===========================");
			System.out.println(dtl);
			System.out.println("---------------------------");
			System.out.println(test.getContent(dtl.getUrl()).getContentsText());
			
			if(i++ > 5) break ;
		}
		
	}
}
