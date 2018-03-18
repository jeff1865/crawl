package com.yg.webshow.crawl.schedule;

import java.util.List;

public interface CrawlJob {
	public String getJobId() ;
	public int crawlNewPage() ;
	public int updatePage(int latestTopN) ;
	//TODO need update comment
	public List<?> getUpUrls() ;
}
