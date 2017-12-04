package com.yg.webshow.crawl.core.api;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.schedule.DefaultCrawlJob;
import com.yg.webshow.crawl.seeds.sample.clien.NewClienPark;

@RestController
public class CmdController {
	private static Logger log = Logger.getLogger(CmdController.class);
	
	@Autowired CrawlTable crawlTable = null ;
	
	public CmdController() {
		;
	}
	
	@RequestMapping(value="/exec/clien/crawl", method=RequestMethod.GET)
	public String execClienJob(@RequestParam String jobId) {
		log.info("Crawl Request Detected .. " + jobId);
		
		NewClienPark clien = new NewClienPark("https://www.clien.net/service/board/park");
		DefaultCrawlJob crawlJob = new DefaultCrawlJob(clien, crawlTable);
		
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
	
}
