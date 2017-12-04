package com.yg.webshow.crawl.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.yg.webshow.crawl.data.tables.CrawlRow;
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
	public int crawlNewPage() {
		WebDocBbsList list = this.docWrapper.getList() ;
		int i = 0;
		for(DbbsTitleLine dtl : list.getTitleLines()) {
			System.out.println("--->" + i++ +"\t" + dtl);;
			
			CrawlRow crawlRow = new CrawlRow();
			crawlRow.setAnchorTitle(dtl.getTitle());
			crawlRow.setAuthor(dtl.getAuthor());
			
			//TODO need to add interface
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				crawlRow.setDocTs(df.parse(dtl.getDate()).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			crawlRow.setPostId(dtl.getNo());
			crawlRow.setUrl(dtl.getUrl());
			crawlRow.setSiteId("clien.park");
						
			this.crawlTable.upsertData(crawlRow);
		}
		
		return i ;
	}

	@Override
	public int updatePage() {
		// TODO Auto-generated method stub
		return 0;
	}

}
