package com.yg.webshow.crawl.data.tables;

import org.apache.hadoop.hbase.client.Result;

import com.yg.webshow.crawl.data.base.*;

public class CrawlRowBinder implements ResultMapper<CrawlRow> {

	@Override
	public CrawlRow convert(Result rs) {
		CrawlRow crawlRow = new CrawlRow() ;
		
//		rs.getColumnLatestCell(family, qualifier)
		
		return crawlRow;
	}

}
