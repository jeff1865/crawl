package com.yg.webshow.crawl.schedule;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CrawlJobScheduler {
	private Logger log = Logger.getLogger(CrawlJobScheduler.class) ;
	
	private List<CrawlJob> jobQueue = new ArrayList<CrawlJob>() ;
	private static long monCnt = 0 ;
	
	public CrawlJobScheduler() {
		;
	}
	
	public CrawlJob createCrawlJob() {
		return null ;
	}
	
	public synchronized void startJob(CrawlJob crawlJob) {
		log.info("Add & Schedule Job :" + crawlJob);
		this.jobQueue.add(crawlJob) ;
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
