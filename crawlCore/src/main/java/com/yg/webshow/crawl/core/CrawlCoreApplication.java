package com.yg.webshow.crawl.core;

import java.util.List;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Table;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.yg.webshow.crawl.data.CrawlDataExBo;
import com.yg.webshow.crawl.data.MrCrawlTable;
import com.yg.webshow.crawl.data.base.DefaultHTable;
import com.yg.webshow.crawl.data.base.TableFactory;
import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.schedule.CrawlJobScheduler;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.yg.webshow")
public class CrawlCoreApplication implements CommandLineRunner {
	private static Logger log = Logger.getLogger(CrawlCoreApplication.class);
	
	@Autowired private SystemProperties systemPropreties = null ;
	@Autowired private CrawlJobScheduler crawlJobScheduler = null ;
	@Autowired private MrCrawlTable mrCrawlTable = null ;
	
	@Autowired private TableFactory tableFactory = null ;
	
	public static void main(String[] args) {
		System.out.println("Boot System ..");
		SpringApplication.run(CrawlCoreApplication.class, args);
	}
	
	private void printProperties() {
		log.info("System Conf Hbase Master :" + this.systemPropreties.getHbaseMaster()) ;
		log.info("System Conf Hbase Quorum :" + this.systemPropreties.getHbaseQuorum()) ;
	}
	
	private void printDataTopN() {
//		Table rtCrawlTbl = this.tableFactory.getTable(TableName.valueOf("rtCrawl")) ;
		DefaultHTable defaultHtable = new DefaultHTable();
		defaultHtable.setTableFactory(this.tableFactory);
				
		CrawlTable crawlTable = new CrawlTable(defaultHtable) ;
		List<CrawlRow> allData = crawlTable.getAllData();
		
		int i = 0;
		for(CrawlRow crawl : allData) {
			System.out.println(i++ + "\t" + crawl);
		}
		
	}
	
	@Override
	public void run(String... args) throws Exception {
		this.printProperties();
		
		this.printDataTopN();
		
//		this.printTonN();
				
		// TODO Auto-generated method stub
		System.out.println("Start System ..");
//		this.crawlJobScheduler.startSystem();
	}
	
//	private void printTonN() {
//		List<CrawlDataExBo> latest = this.mrCrawlTable.getLatest(10, null) ;
//		for(CrawlDataExBo crawlDataBo : latest) {
//			System.out.println("-->" + crawlDataBo);
//		}
//	}
	
}
