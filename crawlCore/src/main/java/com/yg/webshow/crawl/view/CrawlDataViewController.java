package com.yg.webshow.crawl.view;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yg.webshow.crawl.data.tables.CrawlComment;
import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.data.tables.ExtDocTable;

@Controller
public class CrawlDataViewController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired private CrawlTable crawlTable = null ;
	@Autowired private ExtDocTable extDocTable = null ;
	
	public CrawlDataViewController() {
		System.out.println("---------------> CrawlController Initialized ..");;
	}
	
	@RequestMapping("/xf/crawl/list")
	public String displayCrawlData(
			@RequestParam(required=false) String siteId,
			@RequestParam(required=false) int pageNo, 
			@RequestParam(required=false) int pageCnt, Model model) {
		log.info("detected crawlData ..");
		model.addAttribute("hello","World at " + System.currentTimeMillis()) ;
		
//		List<CrawlRow> allData = this.crawlTable.getAllData();
		if(pageCnt == 0) {
			pageCnt = 100 ;
		}
		
		log.info("pageIdx/pageCnt = " + pageNo + "/" + pageCnt);
				
		List<CrawlRow> allData = this.crawlTable.getLatestData(siteId, pageNo, pageCnt, null) ;
		model.addAttribute("crawlList", allData) ;
		
		return "listView" ;
	}
	
	@RequestMapping("/xf/crawl/data/{siteId}/{postId}")
	public String displayData(@PathVariable String siteId, @PathVariable int postId, Model model) {
		log.info("Get Page Doctum ..");
		
		CrawlRow doctum = this.crawlTable.getData(siteId, String.valueOf(postId)) ;
		model.addAttribute("doctum", doctum) ;
		
		List<CrawlComment> comments = this.extDocTable.getComments(siteId, postId) ;
		model.addAttribute("comments", comments) ;
		log.info("Count of Comment Detected :" + comments.size());
		
		return "dataView";
	}
	
	
	
}
