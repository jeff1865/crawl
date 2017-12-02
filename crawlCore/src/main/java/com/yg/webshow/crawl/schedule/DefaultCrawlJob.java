package com.yg.webshow.crawl.schedule;

import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class DefaultCrawlJob implements CrawlJob {
	
	private CrawlTable crawlTable = null ;
	private IDocWrapper docWrapper = null ;
	
	public DefaultCrawlJob(IDocWrapper docWrapper, CrawlTable crawlTable) {
		this.docWrapper = docWrapper ;
		this.crawlTable = crawlTable ;
	}
	
	
	@Override
	public void crawlNewPage() {
		WebDocBbsList list = this.docWrapper.getList() ;
		int i = 0;
		for(DbbsTitleLine dtl : list.getTitleLines()) {
			System.out.println(i++ +"\t" + dtl);;
		}
	}

	@Override
	public void updatePage() {
		// TODO Auto-generated method stub
		
	}

}
