package com.yg.webshow.crawl.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.yg.webshow.crawl.data.tables.CrawlComment;
import com.yg.webshow.crawl.data.tables.CrawlRow;
import com.yg.webshow.crawl.data.tables.CrawlTable;
import com.yg.webshow.crawl.data.tables.ExtDocTable;
import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.webdoc.template.DComment;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class DefaultCrawlJob implements CrawlJob {
	
	private CrawlTable crawlTable = null ;
	private IDocWrapper docWrapper = null ;
	private ExtDocTable extDocTable = null ;
	
	public DefaultCrawlJob(IDocWrapper docWrapper, CrawlTable crawlTable, ExtDocTable extDocTable) {
		this.docWrapper = docWrapper ;
		this.crawlTable = crawlTable ;
		this.extDocTable = extDocTable ;
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
		List<CrawlRow> latestData = this.crawlTable.getLatestData(5, CrawlTable.VAL_STATUS_INIT) ;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int i = 0;
		List<CrawlComment> lstCmt = new ArrayList<CrawlComment>();
		for(CrawlRow crawlRow : latestData) {
			System.out.println(i++ + "\tGetComments: " + crawlRow.getUrl());
			// process context
			WebDocBbs content = this.docWrapper.getContent(crawlRow.getUrl()) ;
			//TODO .... xxxxxxxxxx
			content.getContentsText();
			
			// process comment
			List<DComment> comments = this.docWrapper.getComments(crawlRow.getUrl()) ;
			for(DComment comment : comments) {
				CrawlComment cc = new CrawlComment();
				cc.setCommentNo(comment.getId());
				cc.setContents(comment.getComment());
				cc.setUser(comment.getAuthor());
				try {
					cc.setInitTime(df.parse(comment.getStrDate()).getTime());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				cc.setSiteId(crawlRow.getSiteId());
				cc.setPostId(crawlRow.getPostId());
				
				lstCmt.add(cc);
			}
			
		}
				
		this.extDocTable.appendCommnetData(lstCmt);
				
		return lstCmt.size();
	}

}
