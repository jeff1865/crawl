package com.yg.webshow.crawl.core.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yg.webshow.crawl.data.tables.CrawlComment;
import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.data.tables.ExtDocTable;
import com.yg.webshow.crawl.schedule.DefaultCrawlJob;
import com.yg.webshow.crawl.seeds.sample.clien.NewClienPark;

@RestController
public class CmdController {
	private static Logger log = Logger.getLogger(CmdController.class);
	
	@Autowired CrawlTable crawlTable = null ;
	@Autowired ExtDocTable extDocTable = null ;
	
	DefaultCrawlJob crawlJob = null ;
	
	public CmdController() {
		;
	}
	
	@RequestMapping(value="/exec/clien/crawl", method=RequestMethod.GET)
	public String execClienJob(@RequestParam String jobId) {
		log.info("Crawl Request Detected .. " + jobId);
				
		if(this.crawlJob == null) {
			NewClienPark clien = new NewClienPark("https://www.clien.net/service/board/park");
			crawlJob = new DefaultCrawlJob(clien, crawlTable, this.extDocTable);
		}
		
		int cnt = crawlJob.crawlNewPage(); 
		
		return "Crawled " + cnt + " pages, completed in " + System.currentTimeMillis();
	}
	
	@RequestMapping(value="/exec/clien/data", method=RequestMethod.GET)
	public String getData(@RequestParam int topn) {
		log.info("Get CrawledData Request Detected .. " + topn);
		
		StringBuffer sb = new StringBuffer() ;
		List<CrawlRow> latestData = this.crawlTable.getLatestData(topn, CrawlTable.VAL_STATUS_INIT) ;
		
		int i = 0;
		for(CrawlRow crawlRow : latestData) {
			System.out.println(i++ + "\t" + crawlRow);
			sb.append(crawlRow.toString()).append("<br/>\n");
		}
		
		return sb.toString() ;
	}
	
	@RequestMapping(value="/exec/clien/update", method=RequestMethod.GET)
	public String execUpdateData() {
		log.info("Update Data Request Detected .. ");
		
		if(this.crawlJob == null) {
			NewClienPark clien = new NewClienPark("https://www.clien.net/service/board/park");
			crawlJob = new DefaultCrawlJob(clien, crawlTable, this.extDocTable);
		}
		
		return "Update Completed :" + this.crawlJob.updatePage() ;
	}
	
	@RequestMapping(value="/exec/clien/comment", method=RequestMethod.GET)
	public String getCommentData(@RequestParam int topn) {
		log.info("Get Updated crawl data .. " + topn);
		
		List<CrawlComment> lstCc = this.extDocTable.getLatestComments(topn) ;
		
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(CrawlComment cc : lstCc) {
			System.out.println(i++ + "\t" + cc);
			sb.append(cc.toString()).append("<br/>\n");
		}
				
		return sb.toString() ;
	}
	
}
