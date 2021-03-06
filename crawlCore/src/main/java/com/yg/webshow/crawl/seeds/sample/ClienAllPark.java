package com.yg.webshow.crawl.seeds.sample;

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

//public class ClienAllPark implements IBbsContents, IBbsList {
public class ClienAllPark implements IDocWrapper {	
	private String seedUrl = null ;
	private final String seedId = "clien.park";
	
	public ClienAllPark(String seedUrl){
		this.seedUrl = seedUrl ;
	}
	
	@Override
	public WebDocBbsList getList() {
		WebDocBbsList webDocBbsList = new WebDocBbsList();
		List<DbbsTitleLine> titleLines = new ArrayList<DbbsTitleLine>();
		
		try {
			Document doc = Jsoup.connect(this.seedUrl).get();
			
			Elements list = doc.select("tr[class=mytr]");
//			System.out.println(list.toString());;
//			System.out.println("----" + list.text());
			
			Iterator<Element> itrElem = list.iterator();
			
			int i = 0;
			DbbsTitleLine line = null;
			while(itrElem.hasNext()) {
				Element tag = itrElem.next();
				line = new DbbsTitleLine();
//				System.out.println("------------------------------" + i++);
				
//				System.out.println("No :" + tag.select("td:eq(0)").text());
				line.setNo(tag.select("td:eq(0)").text());
				System.out.println("URL :" + tag.select("td[class=post_subject] > a[href]").attr("abs:href"));
				line.setUrl(tag.select("td[class=post_subject] > a[href]").attr("abs:href"));
//				System.out.println("Title :" + tag.select("td[class=post_subject]").text());
				line.setTitle(tag.select("td[class=post_subject] > a[href]").text());
//				System.out.println("User :" + tag.select("td[class=post_name]").text());
				line.setAuthor(tag.select("td[class=post_name]").text());
//				System.out.println("TimeStamp :" + tag.select("td:eq(3) > span[title]").attr("title"));
				line.setDate(tag.select("td:eq(3) > span[title]").attr("title"));

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
//			String contents = doc.select("div[id=resContents]").text();
			String contents = doc.select("div[id=resContents]>span[id=writeContents]").text();
			System.out.println("Contents >" + contents);
			
			Elements imgs = doc.select("div[id=resContents]>div[class=attachedImage]>img[src]");
//			System.out.println("Img >" + imgs.toString());
			
			Iterator<Element> itrImgs = imgs.iterator();
			while(itrImgs.hasNext()) {
				Element img = itrImgs.next();
				String imgUrl = img.attr("abs:src");
//				System.out.println("ImgURL >" + imgUrl);
				wdb.getImgUrl().add(imgUrl) ;
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return wdb;
	}
	
	@Override
	public String getSeedId() {
		return this.seedId;
	}

	@Override
	public String getDateFormat() {
		return "yyyy-MM-dd HH:mm:ss";
	}

	@Override
	public List<DComment> getComments(String url) {
		// TODO Auto-generated method stub
		return null;
	}
}
