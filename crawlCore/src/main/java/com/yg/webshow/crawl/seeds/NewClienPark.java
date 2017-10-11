package com.yg.webshow.crawl.seeds;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class NewClienPark implements IDocWrapper {
	
	private String seedUrl = null ;
	private final String seedId = "clien.park";
	
	public NewClienPark(String seedUrl) {
		this.seedUrl = seedUrl ;
	}
	
	@Override
	public WebDocBbsList getList() {
		WebDocBbsList webDocBbsList = new WebDocBbsList();
		List<DbbsTitleLine> titleLines = new ArrayList<DbbsTitleLine>();
		
		try {
			Document doc = Jsoup.connect(this.seedUrl).get();
			
			Elements list = doc.select("div[class=board-list post-list] > div[class=list-row symph-row] > div[class=item]");
			
			Iterator<Element> itrElem = list.iterator();
			DbbsTitleLine line = null;
			while(itrElem.hasNext()) {
				Element tag = itrElem.next();
				line = new DbbsTitleLine();

				String url = tag.select("div[class=list-title] > a[class=list-subject]").attr("abs:href");
				line.setNo(url.substring(url.lastIndexOf("/") + 1));
				line.setUrl(url);
				line.setTitle(tag.select("div[class=list-title] > a[class=list-subject]").text());
				String authorName = tag.select("div[class=list-title] > div > a[class=dropdown-toggle nick]").text();
				if(authorName == null || authorName.trim().length() < 1)	// case of IMG nickname 
					authorName = tag.select("div[class=list-title] > div > a[class=dropdown-toggle nick] > img[src]").attr("alt");
				line.setAuthor(authorName);
				line.setDate(tag.select("div[class=list-time] > span > span[class=timestamp]").text());

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
			String contTitle = doc.select("div[data-role=cut-string]").text();
			wdb.setContTitle(contTitle);
			
			String docTitle = doc.select("html > head > title").text();
			wdb.setDocTitle(docTitle);
			
			String contents = doc.select("div[class=post-article fr-view]").text();
			wdb.setContentsText(contents);
			
			contents = doc.select("div[class=post-article fr-view]").html();
			wdb.setContentsHtml(contents);
						
			String contTime = doc.select("div.post-time").text();
			SimpleDateFormat df = new SimpleDateFormat(this.getDateFormat());
			try {
				wdb.setDocDate(df.parse(contTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			// IMG in Attached
			Elements imgs = doc.select("div[class=attached-image] img[src]");
			Iterator<Element> itrImgs = imgs.iterator();
			while(itrImgs.hasNext()) {
				Element img = itrImgs.next();
				String imgUrl = img.attr("abs:src");
				wdb.getImgUrl().add(imgUrl) ;
			}
			
			// IMG in Posted Contents
			imgs = doc.select("div[class=post-article fr-view] img[src]");
			itrImgs = imgs.iterator();
			while(itrImgs.hasNext()) {
				Element img = itrImgs.next();
				String imgUrl = img.attr("abs:src");
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
		// TODO Auto-generated method stub
		return "yyyy-MM-dd HH:mm:ss";
	}
	
	public static void main(String ... v) {
		System.out.println("Start Crawl ..");
		NewClienPark test = new NewClienPark("https://www.clien.net/service/board/park");
		WebDocBbsList list = test.getList();
		
		System.out.println("List of Titles ..");
		int i = 0;
		for(DbbsTitleLine dtl : list.getTitleLines()){ 
			System.out.println(i++ + "\t" + dtl);
		}
		
		String testUrl = "https://www.clien.net/service/board/park/10720251";
		testUrl = "https://www.clien.net/service/board/park/10719290";
		testUrl = "https://www.clien.net/service/board/cm_car/11286165?po=0&od=T31&sk=&sv=&category=&groupCd=&articlePeriod=default";
		
		
		WebDocBbs content = test.getContent(testUrl);
		System.out.println("----------------------");
		System.out.println("Contents >" + content);
	}

}
