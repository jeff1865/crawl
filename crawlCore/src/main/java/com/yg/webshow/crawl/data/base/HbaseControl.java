package com.yg.webshow.crawl.data.base;
import org.apache.hadoop.conf.Configuration;

public class HbaseControl {
	
	private TableFactory tableFactory ;
	private Configuration configuration ;
	
	public TableFactory getTableFactory() {
		return tableFactory;
	}
	public void setTableFactory(TableFactory tableFactory) {
		this.tableFactory = tableFactory;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
}
