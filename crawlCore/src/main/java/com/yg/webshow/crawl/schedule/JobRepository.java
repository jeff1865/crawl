package com.yg.webshow.crawl.schedule;

import java.util.LinkedHashMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class JobRepository {
	private Logger log = Logger.getLogger(this.getClass());
	private LinkedHashMap<String, CrawlJob> jobTable = new LinkedHashMap<String, CrawlJob>() ;
	
	public JobRepository() {
		;
	}
	
	public void addJob(CrawlJob job) {
		log.info("Add CrawlJob :" + job.getJobId());
		this.jobTable.put(job.getJobId(), job) ;
	}
	
	public synchronized CrawlJob getJob(String jobId) {
		
		return this.jobTable.get(jobId);
	}
	
	public static void main(String ... v) {
		;
	}
}
