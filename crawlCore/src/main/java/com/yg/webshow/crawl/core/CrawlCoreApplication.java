package com.yg.webshow.crawl.core;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.schedule.CrawlJobScheduler;
import com.yg.webshow.crawl.seeds.sample.clien.NewClienParkV2;
import com.yg.webshow.crawl.seeds.sample.nhnnews.NaverStockNews;

@SpringBootApplication
@EnableScheduling
@EnableWebMvc
@ComponentScan("com.yg.webshow")
public class CrawlCoreApplication implements CommandLineRunner {
	private static Logger log = Logger.getLogger(CrawlCoreApplication.class);
	
	@Autowired private SystemProperties systemPropreties = null ;
	@Autowired private CrawlJobScheduler crawlJobScheduler = null ;
	
	@Autowired private CrawlTable crawlTable = null ;
	
	public static void main(String[] args) {
		System.out.println("Boot System ..");
		SpringApplication.run(CrawlCoreApplication.class, args);
	}
	
	private void printProperties() {
		log.info("System Conf Hbase Master :" + this.systemPropreties.getHbaseMaster()) ;
		log.info("System Conf Hbase Quorum :" + this.systemPropreties.getHbaseQuorum()) ;
	}
	
	private void printDataTopN() {

		List<CrawlRow> allData = crawlTable.getAllData();
		
		int i = 0;
		for(CrawlRow crawl : allData) {
			System.out.println(i++ + "\t" + crawl);
		}
		
	}
	
	private void printData() {
		CrawlRow data = crawlTable.getData("clien.park", "2136769918") ;
		
		System.out.println("Single! -> " + data);
		
		List<CrawlRow> latestData = crawlTable.getLatestData(2, "INIT") ;
		for(CrawlRow cr : latestData) {
			System.out.println("LD --> " + cr);
		}
	}
	
	private void initJobs() {
		this.crawlJobScheduler.addNewBbsJob(new NaverStockNews());
		this.crawlJobScheduler.addNewBbsJob(new NewClienParkV2("https://www.clien.net/service/board/park"));
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		this.printProperties();
		
//		this.printDataTopN();
//		this.printData();
//		this.printTonN();
				
		System.out.println("----------------- init jobs ------------------");
		this.initJobs();
		System.out.println("----------------------------------------------");
		System.out.println("Start System ..");
//		this.crawlJobScheduler.startSystem();
	}

	
}
