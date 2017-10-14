package com.yg.webshow.crawl.seeds;

import java.util.List;

import com.yg.webshow.crawl.webdoc.template.DComment;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;

public interface IBbsContents {
	public WebDocBbs getContent(String url) ;
	public List<DComment> getComments(String url) ;
}
