package com.yg.webshow.crawl.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.yg.webshow.crawl.data.CrawlDataExBo;
import com.yg.webshow.crawl.data.MrCrawlTable;
import com.yg.webshow.crawl.schedule.CrawlJobScheduler;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.yg.webshow")
public class CrawlCoreApplication implements CommandLineRunner {
	private static Logger log = Logger.getLogger(CrawlCoreApplication.class);
	
	@Autowired private SystemProperties systemPropreties = null ;
	@Autowired private CrawlJobScheduler crawlJobScheduler = null ;
	@Autowired private MrCrawlTable mrCrawlTable = null ;
	
	public static void main(String[] args) {
		System.out.println("Boot System ..");
		SpringApplication.run(CrawlCoreApplication.class, args);
	}
	
	private void printProperties() {
		log.info("System Conf Hbase Master :" + this.systemPropreties.getHbaseMaster()) ;
		log.info("System Conf Hbase Quorum :" + this.systemPropreties.getHbaseQuorum()) ;
	}
	
	@Override
	public void run(String... args) throws Exception {
		this.printProperties();
				
		this.printTonN();
				
		// TODO Auto-generated method stub
		System.out.println("Start System ..");
		this.crawlJobScheduler.startSystem();
	}
	
	private void printTonN() {
		List<CrawlDataExBo> latest = this.mrCrawlTable.getLatest(10, null) ;
		for(CrawlDataExBo crawlDataBo : latest) {
			System.out.println("-->" + crawlDataBo);
		}
	}
	
}
