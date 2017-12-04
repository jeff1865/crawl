package com.yg.webshow.crawl.data.tables;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.yg.webshow.crawl.core.CrawlCoreApplication;
import com.yg.webshow.crawl.data.base.DefaultHTable;
import com.yg.webshow.crawl.data.base.TableFactory;

@Service
public class ServiceTableFactory {
	private static Logger log = Logger.getLogger(ServiceTableFactory.class);
	
	@Autowired private TableFactory tableFactory = null ;
	private static int cnt = 0;
	
	public ServiceTableFactory() {
		;
	}
	
	@Bean
	public CrawlTable getCrawlTable() {
		log.info("Create CrawlTable : " + cnt++ );
		DefaultHTable defaultHtable = new DefaultHTable();
		defaultHtable.setTableFactory(this.tableFactory);
		return new CrawlTable(defaultHtable);
	}
	
}
