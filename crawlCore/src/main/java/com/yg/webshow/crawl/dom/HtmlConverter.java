package com.yg.webshow.crawl.dom;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.yg.webshow.crawl.data.CrawlDataExBo;
import com.yg.webshow.crawl.seeds.sample.clien.NewClienPark;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;

public class HtmlConverter {
	
	public HtmlConverter() {
		;
	}
	
	public String convert(String src) {
		Document doc = Jsoup.parse(src) ;
		
		Elements imgs = doc.select("img[src]");
		
		Iterator<Element> itrImg = imgs.iterator();
		
		int i = 0;
		while(itrImg.hasNext()) {
			Element elem = itrImg.next();
			System.out.println("IMG\t" + elem);
			
			elem.attr("src", "img_" + i++) ;
		}
		
		return doc.html();
//		return null ;
	}
	
	
	public static void main(String ... v) {
		System.out.println(" .. ");
		String url = "http://clien.net/cs2/bbs/board.php?bo_table=park";
		url = "https://www.clien.net/service/board/park";	// New Clien
		NewClienPark clien = new NewClienPark(url);
				
		WebDocBbs content = clien.getContent("https://www.clien.net/service/board/park/10719290");
		
//		System.out.println("content on HTML >" + content.getContentsHtml());
		
		HtmlConverter hcon = new HtmlConverter();
		String convert = hcon.convert(content.getContentsHtml());
		
		System.out.println("ConvertedData >" + convert);
	}
}
