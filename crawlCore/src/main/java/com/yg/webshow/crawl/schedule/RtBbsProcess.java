package com.yg.webshow.crawl.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.mortbay.log.Log;

import com.yg.webshow.crawl.core.SysConf;
import com.yg.webshow.crawl.data.CrawlDataExBo;
import com.yg.webshow.crawl.data.RtCrawlTable;
import com.yg.webshow.crawl.seeds.ClienAllPark;
import com.yg.webshow.crawl.seeds.IBbsContents;
import com.yg.webshow.crawl.seeds.IBbsList;
import com.yg.webshow.crawl.seeds.IDocWrapper;
import com.yg.webshow.crawl.seeds.NewClienPark;
import com.yg.webshow.crawl.util.FileDownloader;
import com.yg.webshow.crawl.webdoc.template.DbbsTitleLine;
import com.yg.webshow.crawl.webdoc.template.WebDocBbs;
import com.yg.webshow.crawl.webdoc.template.WebDocBbsList;

public class RtBbsProcess {
	
	private int procId ;
	private final String imageDir = "/Users/1002000/dev/temp20/"; 
	
	private RtCrawlTable rtCrawlTable  = null; 
	private IDocWrapper docWrapper = null;
		
	public RtBbsProcess(IDocWrapper docWrapper) {
		this.rtCrawlTable = new RtCrawlTable(SysConf.createNewHbaseConn());
		this.docWrapper = docWrapper ;
	}
	
	public void start() {
		;
	}
	
	//TODO need to get start index and then extract the lines indexed bigger No
	private List<DbbsTitleLine> procExtAnchorList() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(this.docWrapper.getDateFormat());	//TODO need to move seed engine
		WebDocBbsList bbsList = this.docWrapper.getList();
		
		int baseNo = 0;
		List<CrawlDataExBo> latest = this.rtCrawlTable.getLatest(1, null) ;
		if(latest != null && latest.size() == 1) {
			String postId = latest.get(0).getPostId();
			baseNo = Integer.parseInt(postId);
		}
		
		ArrayList<DbbsTitleLine> newLines = new ArrayList<DbbsTitleLine>();
		
		List<DbbsTitleLine> titleLines = bbsList.getTitleLines();
		int i = 0;
		int no = 0;
		for(DbbsTitleLine dtl : titleLines) {
			no = Integer.parseInt(dtl.getNo());
			if(baseNo >= no) continue ;
			
			newLines.add(dtl);
			System.out.println(i++  +"\t" + dtl);
			
			try {
				rtCrawlTable.addInitData(this.docWrapper.getSeedId(), Integer.parseInt(dtl.getNo()), 
						dtl.getAuthor(), dtl.getTitle(), dateFormat.parse(dtl.getDate().trim()).getTime(), dtl.getUrl());
			} catch (NumberFormatException | ParseException e) {
				e.printStackTrace();
			}
		}
		
		return newLines ;
	}
	
	private void procSummarizeDoc(List<DbbsTitleLine> lstNewLines) {
		for(DbbsTitleLine dtl : lstNewLines) {
			try {
				Thread.sleep((long)(Math.random() * 2000));
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			String url = dtl.getUrl();
			
			WebDocBbs content = this.docWrapper.getContent(url);
			List<String> imgUrl = content.getImgUrl();
			
			this.rtCrawlTable.updateExtData(this.docWrapper.getSeedId(), 
					Integer.parseInt(dtl.getNo()), 
					RtCrawlTable.VAL_STATUS_EXTDATA, 
					content.getDocTitle(), 
					content.getContentsText(), 
					content.getContentsHtml(),
					imgUrl);
			
			if(imgUrl != null && imgUrl.size() > 0) {
				int i = 0;
				for(String murl : imgUrl) {
					String postNo = dtl.getNo();
					try {
						String imgType = ".jpg";
						if(murl.contains(".png") || murl.contains(".PNG")) imgType = ".png";
						else if(murl.contains(".gif") || murl.contains("GIF")) imgType = ".gif";
						
						String filename = postNo + "_" + (i++) + imgType;
						FileDownloader.downloadFile(murl, this.imageDir + filename);
						Log.info("Download completed .. " + filename);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	
	public static void main(String ... v) {
		String url = "http://clien.net/cs2/bbs/board.php?bo_table=park";
		url = "https://www.clien.net/service/board/park";	// New Clien
//		ClienAllPark clien = new ClienAllPark(url);
		NewClienPark clien = new NewClienPark(url);
		
//		RtBbsProcess test = new RtBbsProcess("clien.park", clien, clien);
		RtBbsProcess test = new RtBbsProcess(clien);
		List<DbbsTitleLine> lstExtAnchor = test.procExtAnchorList();
		
		System.out.println("----------<NEW>----------");
		int i = 0;
		for(DbbsTitleLine dtl : lstExtAnchor) {
			System.out.println(i++ + "\t" + dtl);
		}
		
		
//		lstExtAnchor = lstExtAnchor.subList(0, 10) ;
		test.procSummarizeDoc(lstExtAnchor);
		
		
	}
}
