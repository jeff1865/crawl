package com.yg.webshow.crawl.core.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yg.webshow.crawl.schedule.CrawlJob;
import com.yg.webshow.crawl.schedule.CrawlJobScheduler;

@RestController
public class MultiCmdController {
	private Logger log = Logger.getLogger(this.getClass()) ;
	
	@Autowired CrawlJobScheduler crawlJobScheduler = null ;
	
	public MultiCmdController() {
		;
	}
	
	@RequestMapping(value="/crawl/page/anchors", method=RequestMethod.GET)
	public String getTitleList(@RequestParam String crawlId) {
		log.info("Extract anchor titles from seedPage " + crawlId) ;
				
		CrawlJob job = this.crawlJobScheduler.getJobRepository().getJob(crawlId);
		if(job != null) {
			int crawlNewPage = job.crawlNewPage() ;
			return "count of anchors from crawling:" + crawlNewPage ;
		} else {
			return "The job doesn't exsit .." ;
		}
	}
	
}
