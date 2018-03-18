package com.yg.webshow.crawl.schedule;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.data.tables.ExtDocTable;
import com.yg.webshow.crawl.seeds.IDocWrapper;

@Service
public class CrawlJobScheduler {
	private Logger log = Logger.getLogger(CrawlJobScheduler.class) ;
	
//	private List<CrawlJob> jobQueue = new ArrayList<CrawlJob>() ;
	@Autowired private JobRepository jobRepository = null;
	@Autowired private CrawlTable crawlTable = null ;
	@Autowired private ExtDocTable extDocTable = null ;
	
	private static long monCnt = 0 ;
	
	public CrawlJobScheduler() {
		;
	}
	
	public void addNewBbsJob(IDocWrapper dw) {
		this.jobRepository.addJob(new DefaultBbsCrawlJob(dw, this.crawlTable, this.extDocTable));
	}
	
	
	public JobRepository getJobRepository() {
		return this.jobRepository ;
	}
		
	//TODO
	public synchronized void startJob(CrawlJob crawlJob) {
		log.info("Add & Schedule Job :" + crawlJob);
//		this.jobQueue.add(crawlJob) ;
	}
	
	
	public void startSystem() {
		for(int i= 0 ; i<100;i++) {
			System.out.println("Main Process :" + i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Scheduled(fixedDelayString="10000")
	public void printMonitor() {
		if(monCnt % 100 == 0)
			System.out.println("Monitor :" + monCnt ++);
		;
	}
	
}
